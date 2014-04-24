/*
 * Copyright 2014 Frugal Mechanic (http://frugalmechanic.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fm.flatfile

import fm.common.Implicits._
import fm.common.{InputStreamResource, Resource, SingleUseResource}
import fm.lazyseq.LazySeq
import com.frugalmechanic.optparse._
import scala.util.{Failure, Success, Try}
import java.io.{BufferedInputStream, File, InputStream, Reader}
import org.joda.time.LocalDate

object FlatFileReader extends FlatFileReaderFactory {
  import fm.flatfile.plain.PlainFlatFileReader
  import fm.flatfile.excel.ExcelFlatFileReader

  def apply(resource: InputStreamResource, options: FlatFileReaderOptions): FlatFileReader = new AutoDetectingFlatFileReader(resource, options)
  
  //
  // java.io.Reader is always assumed to be for the PlainFlatFileReader since ExcelFlatFileReader needs an InputStream
  //
  def apply(reader: Reader): FlatFileReader = apply(SingleUseResource(reader))
  def apply(reader: Reader, options: FlatFileReaderOptions): FlatFileReader = apply(SingleUseResource(reader), options)
  
  def apply(reader: Resource[Reader]): FlatFileReader = apply(reader, FlatFileReaderOptions())
  def apply(reader: Resource[Reader], options: FlatFileReaderOptions): FlatFileReader = new FlatFileReaderForImpl[Reader](reader, options, PlainFlatFileReader)
  
  private class AutoDetectingFlatFileReader(resource: InputStreamResource, options: FlatFileReaderOptions) extends FlatFileReader {
    final val withTries: FlatFileReaderWithTries = new AutoDetectingFlatFileReaderWithTries(resource, options)
    def foreach[U](f: FlatFileRow => U): Unit = withTries.map{ _.get }.foreach(f)
  }
  
  private class AutoDetectingFlatFileReaderWithTries(resource: InputStreamResource, options: FlatFileReaderOptions) extends FlatFileReaderWithTries {
    def foreach[U](f: Try[FlatFileRow] => U): Unit = resource.buffered().use { is: BufferedInputStream =>
      val impl: FlatFileReaderImpl[_] = 
        if (ExcelFlatFileReader.isExcelFormat(is)) ExcelFlatFileReader
        else if (isXML(is)) throw new FlatFileReaderException.InvalidFlatFile("Looks like an XML File and NOT a flat file")
        else PlainFlatFileReader
        
      impl.foreach(is, options)(f)
    }
  }
  
  // NOTE: Duplicated in XmlReader
  private def isXML(f: File): Boolean = InputStreamResource.forFile(f).buffered().use{ isXML }
  
  // NOTE: Duplicated in XmlReader
  private def isXML(is: InputStream): Boolean = {
    import javax.xml.stream.XMLInputFactory
    import javax.xml.stream.XMLStreamConstants.{START_ELEMENT, CHARACTERS}
    import com.ctc.wstx.stax.WstxInputFactory
    import org.codehaus.stax2.XMLStreamReader2

    require(is.markSupported, "Need an InputStream that supports mark()/reset()")
    is.mark(1024)
    
    val inputFactory = new WstxInputFactory()
    inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false)
    inputFactory.configureForSpeed()

    var xmlStreamReader: XMLStreamReader2 = null
    
    val res: Boolean = try {
      xmlStreamReader = inputFactory.createXMLStreamReader(is).asInstanceOf[XMLStreamReader2]
      while(xmlStreamReader.getEventType != START_ELEMENT) xmlStreamReader.next()
      // If we found a START_ELEMENT then this looks like XML
      xmlStreamReader.getEventType == START_ELEMENT
    } catch {
      case ex: Exception => false
    } finally {
      xmlStreamReader.close()
    }
    
    is.reset()

    res
  }
  
  def parseExcelDate(dateStr: String): LocalDate = ExcelFlatFileReader.parseExcelDate(dateStr)
  

  //
  // Main Method and Options
  //
  object Options extends OptParse {
    val file = StrOpt()
    val skipLines = IntOpt()
    val debug = BoolOpt()
    val head = IntOpt(desc = "Only take N numbers of rows from the file")
  }

  def main(args: Array[String]) {
    Options.parse(args)

    val file = new File(Options.file.get)
    val skipLines = Options.skipLines.getOrElse(0)

    require(file.isFile && file.canRead, "File does not exist or is not readable: "+file.getPath)

    val options: FlatFileReaderOptions = new FlatFileReaderOptions(skipLines=skipLines)

    val reader: FlatFileReader = apply(file, options)

    var first = true
    
    var tmp: LazySeq[FlatFileRow] = reader
    
    if (Options.head) tmp = tmp.take(Options.head.get)
    
    tmp.foreach{ row: FlatFileRow =>
      if (first) {
        println(row.headers.mkString(", "))
        first = false
      }
      if (Options.debug) row.debugPrint() else println(row.lineNumber+": "+row.values.mkString(", "))
    }
  }

}

abstract class FlatFileReader extends LazySeq[FlatFileRow] {
  def withTries: FlatFileReaderWithTries
  
  def mapExceptions(f: Throwable => Throwable): LazySeq[FlatFileRow] = {
    withTries.mapExceptions(f).map{ _.get }
  }
}

abstract class FlatFileReaderWithTries extends LazySeq[Try[FlatFileRow]] {
  def mapExceptions(f: Throwable => Throwable): LazySeq[Try[FlatFileRow]] = map{ _.mapFailure(f) }
}
