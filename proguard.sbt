import com.typesafe.sbt.SbtProguard._
import com.typesafe.sbt.SbtProguard.ProguardKeys.proguard

// Modeled after https://github.com/rtimush/sbt-updates/blob/master/proguard.sbt

proguardSettings

javaOptions in (Proguard, proguard) := Seq("-Xmx1024M", "-Dfile.encoding=UTF8")

ProguardKeys.proguardVersion in Proguard := "4.11"

ProguardKeys.options in Proguard ++= Seq(
  "-dontoptimize",
  "-dontusemixedcaseclassnames", // Don't write out i.class and I.class (which won't unjar properly on case-insensitive file systems like on OSX)
  "-keep public class fm.** { *; }",
  "-keepclassmembers class ** extends org.apache.poi.hssf.record.Record { public final static short sid; }",
  "-keepclassmembers class ** extends org.apache.poi.hssf.record.Record { public static *** create(...); }",
  "-keepclassmembers class ** extends org.apache.poi.hssf.record.Record { public <init>(...); }",
  "-keepclassmembers class org.apache.xmlbeans.impl.store.Locale { public static *** streamToNode(...); public static *** nodeTo*(...); }",
  "-keepclassmembers class org.apache.xmlbeans.impl.store.Path { public static *** compilePath(...); }",
  "-keepclassmembers class org.apache.xmlbeans.impl.store.Query { public static *** compileQuery(...); }",
  "-keepclassmembers class org.apache.xmlbeans.impl.schema.PathResourceLoader { public <init>(...); }",
  "-keepclassmembers class org.apache.xmlbeans.impl.schema.BuiltinSchemaTypeSystem { public static *** get(...); public static *** getNoType(...); }",
  "-keepclassmembers class org.apache.xmlbeans.impl.schema.SchemaTypeLoaderImpl { public static *** getContextTypeLoader(...); public static *** build(...); }",
  "-keepclassmembers class org.apache.xmlbeans.impl.schema.SchemaTypeSystemCompiler { public static *** compile(...); }",
  "-keep class org.apache.xmlbeans.XmlObject", // Needed by the schemaorg_apache_xmlbeans.** stuff
  "-keep class org.apache.xmlbeans.SchemaTypeSystem", // Needed by the schemaorg_apache_xmlbeans.** stuff
  "-keep class org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl { public <init>(...); }", // breaks when commented out
  "-keep class schemaorg_apache_xmlbeans.system.sE130CAA0A01A7CDE5A2B4FEB8B311707.TypeSystemHolder { public final static *** typeSystem; }",
  "-keep class org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument { *; }",
  "-keep class org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellStyleXfId { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.STXstring { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.StyleSheetDocument { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument { *; }",
  "-keep class org.openxmlformats.schemas.drawingml.x2006.main.impl.ThemeDocumentImpl { *; }",
  "-keep class org.openxmlformats.schemas.officeDocument.x2006.relationships.impl.STRelationshipIdImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTBorderImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTBordersImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCellStyleXfsImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTCellXfsImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTDxfsImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFillImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFillsImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFontImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTFontsImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTNumFmtImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTNumFmtsImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTRstImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSheetImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTSheetsImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTStylesheetImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTWorkbookImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTXfImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.STCellStyleXfIdImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.STNumFmtIdImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.STXstringImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.StyleSheetDocumentImpl { *; }",
  "-keep class org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.WorkbookDocumentImpl { *; }",
  "-adaptclassstrings org.apache.xmlbeans.**",
  "-adaptclassstrings schemaorg_apache_xmlbeans.**",
  "-repackageclasses fm.flatfile.libs",
  "-keepattributes",
  "-keepparameternames",
  //"-dontnote org.joda.convert.StringConvert,org.joda.convert.AnnotationStringConverterFactory,org.joda.time.DateTimeZone",
  "-dontnote org.apache.**,org.dom4j.**",
  "-dontwarn org.apache.**,org.dom4j.**,com.ctc.**",
  "-dontwarn com.microsoft.**,schemasMicrosoftCom**,org.openxmlformats.**"
)

ProguardKeys.defaultInputFilter in Proguard := Some("!META-INF/**,!javax/**,!org/xml/sax/**,!org/w3c/dom/**,!license/**,!LICENSE.txt,!NOTICE.txt,!font_metrics.properties,!**.xsd,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/index.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/stylesheet5d8bdoctype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctstylesheet4257type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctnumfmtsb58btype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctnumfmt3870type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/stnumfmtid76fbtype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/stxstring1198type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctfonts6623type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctfont14d8type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctfills2c6ftype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctfill550ctype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctborders0d66type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctborderf935type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctcellxfs1322type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctxf97f7type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctcellstylexfsa81ftype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctdxfsb26atype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/themefd26doctype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/workbookec17doctype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctworkbook83c3type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctsheets49fdtype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctsheet4dbetype.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/strelationshipid1e94type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/ctrsta472type.xsb,schemaorg_apache_xmlbeans/system/sE130CAA0A01A7CDE5A2B4FEB8B311707/stcellstylexfid70c7type.xsb,!**.xsb")

ProguardKeys.inputs in Proguard <<= (dependencyClasspath in Embedded, packageBin in Runtime) map {
  (dcp, pb) => Seq(pb) ++ dcp.files
}

Build.publishMinJar <<= (ProguardKeys.proguard in Proguard) map (_.head)

packagedArtifact in (Compile, packageBin) <<= (packagedArtifact in (Compile, packageBin), Build.publishMinJar) map {
  case ((art, _), jar) => (art, jar)
}

dependencyClasspath in Compile <++= dependencyClasspath in Embedded

dependencyClasspath in Test <++= dependencyClasspath in Embedded