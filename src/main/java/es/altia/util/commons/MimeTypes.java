package es.altia.util.commons;


import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.util.commons.BasicValidations;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MimeTypes {
    /*_______Constants______________________________________________*/
    private static final Logger _log = Logger.getLogger(MimeTypes.class);

    public static final String[] PDF = {"application/pdf"};
    public static final String[] HTML = {"text/html"};
    public static final String[] TXT = {"text/plain"};
    public static final String[] BINARY = {"application/octet-stream", "application/download"};
    public static final String[] APPTXT = {"application/text"};
    public static final String[] XLS = {"application/vnd.ms-excel"};
    public static final String[] IMG = {"image/jpeg","image/png"};
    public static final String[] XML = {"text/xml", "application/xml"};
    public static final String[] RTF = {"application/rtf"};
    public static final String[] ODT = {"application/vnd.oasis.opendocument.text"};

    public static final String FILEEXTENSION_PDF = "pdf";
    public static final String FILEEXTENSION_HTML = "html";
    public static final String FILEEXTENSION_TXT = "txt";
    public static final String FILEEXTENSION_BINARY = "bin";
    public static final String FILEEXTENSION_XLS = "xls";
    public static final String FILEEXTENSION_DOC = "doc";
    public static final String FILEEXTENSION_PNG = "png";
    public static final String FILEEXTENSION_JPG = "jpeg";
    public static final String FILEEXTENSION_XML = "xml";
    public static final String FILEEXTENSION_RTF = "rtf";
    public static final String FILEEXTENSION_ODT = "odt";
    public static final String FILEEXTENSION_ODP = "odp";
    public static final String FILEEXTENSION_ODS = "ods";
    public static final String FILEEXTENSION_7z = "7z";
    public static final String FILEEXTENSION_PPT = "ppt";
    public static final String FILEEXTENSION_BMP = "bmp";
    public static final String FILEEXTENSION_TIFF = "tiff";
    public static final String FILEEXTENSION_SXW = "sxw";
    public static final String FILEEXTENSION_SDW = "sdw";
    public static final String FILEEXTENSION_ABW = "abw";
    public static final String FILEEXTENSION_ZIP = "zip";
    public static final String FILEEXTENSION_RAR = "rar";
    public static final String FILEEXTENSION_XSIG = "xsig";

    private static final Map<String, String> MIMETYPES_FROM_EXTENSIONS = Collections.synchronizedMap(new HashMap<String, String>());
    static {
        MIMETYPES_FROM_EXTENSIONS.put("3g2", "video/3gpp2");
        MIMETYPES_FROM_EXTENSIONS.put("3gp", "video/3gpp");
        MIMETYPES_FROM_EXTENSIONS.put("7z", "application/x-7z-compressed");
        MIMETYPES_FROM_EXTENSIONS.put("aac", "audio/aac");
        MIMETYPES_FROM_EXTENSIONS.put("abw", "application/x-abiword");
        MIMETYPES_FROM_EXTENSIONS.put("arc", "application/octet-stream");
        MIMETYPES_FROM_EXTENSIONS.put("avi", "video/x-msvideo");
        MIMETYPES_FROM_EXTENSIONS.put("azw", "application/vnd.amazon.ebook");
        MIMETYPES_FROM_EXTENSIONS.put("bin", "application/octet-stream");
        MIMETYPES_FROM_EXTENSIONS.put("bmp", "image/bmp");
        MIMETYPES_FROM_EXTENSIONS.put("bz", "application/x-bzip");
        MIMETYPES_FROM_EXTENSIONS.put("bz2", "application/x-bzip2");
        MIMETYPES_FROM_EXTENSIONS.put("csh", "application/x-csh");
        MIMETYPES_FROM_EXTENSIONS.put("css", "application/octet-stream");
        MIMETYPES_FROM_EXTENSIONS.put("doc", "application/msword");
        MIMETYPES_FROM_EXTENSIONS.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
        MIMETYPES_FROM_EXTENSIONS.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        MIMETYPES_FROM_EXTENSIONS.put("dot", "application/msword");
        MIMETYPES_FROM_EXTENSIONS.put("dvi", "application/x-dvi");
        MIMETYPES_FROM_EXTENSIONS.put("dwf", "model/vnd.dwf");
        MIMETYPES_FROM_EXTENSIONS.put("dwg", "application/acad");
        MIMETYPES_FROM_EXTENSIONS.put("dxf", "application/dxf");
        MIMETYPES_FROM_EXTENSIONS.put("eps", "application/postscript");
        MIMETYPES_FROM_EXTENSIONS.put("epub", "application/x-csh");
        MIMETYPES_FROM_EXTENSIONS.put("fli", "video/fli");
        MIMETYPES_FROM_EXTENSIONS.put("gif", "image/gif");
        MIMETYPES_FROM_EXTENSIONS.put("gtar", "application/x-gtar");
        MIMETYPES_FROM_EXTENSIONS.put("gz", "application/x-gzip");
        MIMETYPES_FROM_EXTENSIONS.put("gzip", "application/x-gzip");
        MIMETYPES_FROM_EXTENSIONS.put("help", "application/x-helpfile");
        MIMETYPES_FROM_EXTENSIONS.put("hlp", "application/x-helpfile");
        MIMETYPES_FROM_EXTENSIONS.put("htm", "text/html");
        MIMETYPES_FROM_EXTENSIONS.put("html", "text/html");
        MIMETYPES_FROM_EXTENSIONS.put("htmls", "text/html");
        MIMETYPES_FROM_EXTENSIONS.put("ico", "image/x-icon");
        MIMETYPES_FROM_EXTENSIONS.put("ics", "text/calendar");
        MIMETYPES_FROM_EXTENSIONS.put("jar", "application/java-archive");
        MIMETYPES_FROM_EXTENSIONS.put("jpe", "image/jpeg");
        MIMETYPES_FROM_EXTENSIONS.put("jpeg", "image/jpeg");
        MIMETYPES_FROM_EXTENSIONS.put("jpg", "image/jpeg");
        MIMETYPES_FROM_EXTENSIONS.put("js", "application/javascript");
        MIMETYPES_FROM_EXTENSIONS.put("json", "application/json");
        MIMETYPES_FROM_EXTENSIONS.put("latex", "application/x-latex");
        MIMETYPES_FROM_EXTENSIONS.put("mid", "audio/midi");
        MIMETYPES_FROM_EXTENSIONS.put("midi", "audio/midi");
        MIMETYPES_FROM_EXTENSIONS.put("mjpg", "video/x-motion-jpeg");
        MIMETYPES_FROM_EXTENSIONS.put("mov", "video/quicktime");
        MIMETYPES_FROM_EXTENSIONS.put("mpe", "video/mpeg");
        MIMETYPES_FROM_EXTENSIONS.put("mpeg", "video/mpeg");
        MIMETYPES_FROM_EXTENSIONS.put("mpg", "video/mpeg");
        MIMETYPES_FROM_EXTENSIONS.put("mpkg", "application/vnd.apple.installer+xml");
        MIMETYPES_FROM_EXTENSIONS.put("mpp", "application/vnd.ms-project");
        MIMETYPES_FROM_EXTENSIONS.put("mpt", "application/x-project");
        MIMETYPES_FROM_EXTENSIONS.put("mpv", "application/x-project");
        MIMETYPES_FROM_EXTENSIONS.put("mpx", "application/x-project");
        MIMETYPES_FROM_EXTENSIONS.put("odb", "application/vnd.oasis.opendocument.database");
        MIMETYPES_FROM_EXTENSIONS.put("odc", "application/vnd.oasis.opendocument.chart");
        MIMETYPES_FROM_EXTENSIONS.put("odf", "application/vnd.oasis.opendocument.formula");
        MIMETYPES_FROM_EXTENSIONS.put("odg", "application/vnd.oasis.opendocument.graphics");
        MIMETYPES_FROM_EXTENSIONS.put("odi", "application/vnd.oasis.opendocument.image");
        MIMETYPES_FROM_EXTENSIONS.put("odm", "application/vnd.oasis.opendocument.text-master");
        MIMETYPES_FROM_EXTENSIONS.put("odp", "application/vnd.oasis.opendocument.presentation");
        MIMETYPES_FROM_EXTENSIONS.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        MIMETYPES_FROM_EXTENSIONS.put("odt", "application/vnd.oasis.opendocument.text");
        MIMETYPES_FROM_EXTENSIONS.put("ody", "application/vnd.oasis.opendocument.text");
        MIMETYPES_FROM_EXTENSIONS.put("oga", "audio/ogg");
        MIMETYPES_FROM_EXTENSIONS.put("ogv", "video/ogg");
        MIMETYPES_FROM_EXTENSIONS.put("ogx", "application/ogg");
        MIMETYPES_FROM_EXTENSIONS.put("otg", "application/vnd.oasis.opendocument.graphics-template");
        MIMETYPES_FROM_EXTENSIONS.put("oth", "application/vnd.oasis.opendocument.text-web");
        MIMETYPES_FROM_EXTENSIONS.put("otp", "application/vnd.oasis.opendocument.presentation-template");
        MIMETYPES_FROM_EXTENSIONS.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
        MIMETYPES_FROM_EXTENSIONS.put("ott", "application/vnd.oasis.opendocument.text-template");
        MIMETYPES_FROM_EXTENSIONS.put("pbm", "image/x-portable-bitmap");
        MIMETYPES_FROM_EXTENSIONS.put("pct", "image/x-pict");
        MIMETYPES_FROM_EXTENSIONS.put("pcx", "image/x-pcx");
        MIMETYPES_FROM_EXTENSIONS.put("pdf", "application/pdf");
        MIMETYPES_FROM_EXTENSIONS.put("pic", "image/pict");
        MIMETYPES_FROM_EXTENSIONS.put("pict", "image/pict");
        MIMETYPES_FROM_EXTENSIONS.put("pm4", "application/x-pagemaker");
        MIMETYPES_FROM_EXTENSIONS.put("pm5", "application/x-pagemaker");
        MIMETYPES_FROM_EXTENSIONS.put("png", "image/png");
        MIMETYPES_FROM_EXTENSIONS.put("pnm", "image/x-portable-anymap");
        MIMETYPES_FROM_EXTENSIONS.put("pot", "application/mspowerpoint");
        MIMETYPES_FROM_EXTENSIONS.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        MIMETYPES_FROM_EXTENSIONS.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
        MIMETYPES_FROM_EXTENSIONS.put("ppm", "image/x-portable-pixmap");
        MIMETYPES_FROM_EXTENSIONS.put("pps", "application/mspowerpoint");
        MIMETYPES_FROM_EXTENSIONS.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
        MIMETYPES_FROM_EXTENSIONS.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        MIMETYPES_FROM_EXTENSIONS.put("ppt", "application/vnd.ms-powerpoint");
        MIMETYPES_FROM_EXTENSIONS.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        MIMETYPES_FROM_EXTENSIONS.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        MIMETYPES_FROM_EXTENSIONS.put("ps", "application/postscript");
        MIMETYPES_FROM_EXTENSIONS.put("qt", "video/quicktime");
        MIMETYPES_FROM_EXTENSIONS.put("rar", "application/x-rar-compressed");
        MIMETYPES_FROM_EXTENSIONS.put("rtf", "application/rtf");
        MIMETYPES_FROM_EXTENSIONS.put("sda", "application/vnd.stardivision.draw");
        MIMETYPES_FROM_EXTENSIONS.put("sdc", "application/vnd.stardivision.calc");
        MIMETYPES_FROM_EXTENSIONS.put("sdd", "application/vnd.stardivision.impress");
        MIMETYPES_FROM_EXTENSIONS.put("sdm", "application/vnd.stardivision.mail");
        MIMETYPES_FROM_EXTENSIONS.put("sdp", "application/vnd.stardivision.impress.packed");
        MIMETYPES_FROM_EXTENSIONS.put("sds", "application/vnd.stardivision.chart");
        MIMETYPES_FROM_EXTENSIONS.put("sdw", "application/vnd.stardivision.writer");
        MIMETYPES_FROM_EXTENSIONS.put("sgl", "application/vnd.stardivision.writer-global");
        MIMETYPES_FROM_EXTENSIONS.put("sgm", "text/sgml");
        MIMETYPES_FROM_EXTENSIONS.put("sh", "application/x-sh");
        MIMETYPES_FROM_EXTENSIONS.put("shtml", "text/html");
        MIMETYPES_FROM_EXTENSIONS.put("sldm", "application/vnd.ms-powerpoint.slide.macroEnabled.12");
        MIMETYPES_FROM_EXTENSIONS.put("sldx", "application/vnd.openxmlformats-officedocument.presentationml.slide");
        MIMETYPES_FROM_EXTENSIONS.put("smf", "application/vnd.stardivision.math");
        MIMETYPES_FROM_EXTENSIONS.put("stc", "application/vnd.sun.xml.calc.template");
        MIMETYPES_FROM_EXTENSIONS.put("std", "application/vnd.sun.xml.draw.template");
        MIMETYPES_FROM_EXTENSIONS.put("sti", "application/vnd.sun.xml.impress.template");
        MIMETYPES_FROM_EXTENSIONS.put("stw", "application/vnd.sun.xml.writer.template");
        MIMETYPES_FROM_EXTENSIONS.put("svg", "image/svg+xml");
        MIMETYPES_FROM_EXTENSIONS.put("swf", "application/x-shockwave-flash");
        MIMETYPES_FROM_EXTENSIONS.put("sxc", "application/vnd.sun.xml.calc");
        MIMETYPES_FROM_EXTENSIONS.put("sxd", "application/vnd.sun.xml.draw");
        MIMETYPES_FROM_EXTENSIONS.put("sxg", "application/vnd.sun.xml.writer.global");
        MIMETYPES_FROM_EXTENSIONS.put("sxi", "application/vnd.sun.xml.impress");
        MIMETYPES_FROM_EXTENSIONS.put("sxm", "application/vnd.sun.xml.math");
        MIMETYPES_FROM_EXTENSIONS.put("sxw", "application/vnd.sun.xml.writer");
        MIMETYPES_FROM_EXTENSIONS.put("tar", "application/x-tar");
        MIMETYPES_FROM_EXTENSIONS.put("tex", "application/x-tex");
        MIMETYPES_FROM_EXTENSIONS.put("texi", "application/x-texinfo");
        MIMETYPES_FROM_EXTENSIONS.put("texinfo", "application/x-texinfo");
        MIMETYPES_FROM_EXTENSIONS.put("text", "text/plain");
        MIMETYPES_FROM_EXTENSIONS.put("tgz", "application/gnutar");
        MIMETYPES_FROM_EXTENSIONS.put("tif", "image/tiff");
        MIMETYPES_FROM_EXTENSIONS.put("tiff", "image/tiff");
        MIMETYPES_FROM_EXTENSIONS.put("ttf", "font/ttf");
        MIMETYPES_FROM_EXTENSIONS.put("txt", "text/plain");
        MIMETYPES_FROM_EXTENSIONS.put("vob", "application/vnd.stardivision.writer");
        MIMETYPES_FROM_EXTENSIONS.put("vor", "application/vnd.stardivision.writer");
        MIMETYPES_FROM_EXTENSIONS.put("vsd", "application/x-visio");
        MIMETYPES_FROM_EXTENSIONS.put("vst", "application/x-visio");
        MIMETYPES_FROM_EXTENSIONS.put("vsw", "application/x-visio");
        MIMETYPES_FROM_EXTENSIONS.put("w60", "application/wordperfect6.0");
        MIMETYPES_FROM_EXTENSIONS.put("w61", "application/wordperfect6.1");
        MIMETYPES_FROM_EXTENSIONS.put("w6w", "application/msword");
        MIMETYPES_FROM_EXTENSIONS.put("wav", "audio/x-wav");
        MIMETYPES_FROM_EXTENSIONS.put("weba", "audio/webm");
        MIMETYPES_FROM_EXTENSIONS.put("webm", "video/webm");
        MIMETYPES_FROM_EXTENSIONS.put("webp", "image/webp");
        MIMETYPES_FROM_EXTENSIONS.put("wiz", "application/msword");
        MIMETYPES_FROM_EXTENSIONS.put("wmf", "windows/metafile");
        MIMETYPES_FROM_EXTENSIONS.put("woff", "font/woff");
        MIMETYPES_FROM_EXTENSIONS.put("woff2", "font/woff2");
        MIMETYPES_FROM_EXTENSIONS.put("word", "application/msword");
        MIMETYPES_FROM_EXTENSIONS.put("wp", "application/wordperfect");
        MIMETYPES_FROM_EXTENSIONS.put("wp5", "application/wordperfect");
        MIMETYPES_FROM_EXTENSIONS.put("wp6", "application/wordperfect");
        MIMETYPES_FROM_EXTENSIONS.put("wpd", "application/wordperfect");
        MIMETYPES_FROM_EXTENSIONS.put("wq1", "application/x-lotus");
        MIMETYPES_FROM_EXTENSIONS.put("wri", "application/mswrite");
        MIMETYPES_FROM_EXTENSIONS.put("xbm", "image/x-xbitmap");
        MIMETYPES_FROM_EXTENSIONS.put("xhtml", "application/xhtml+xml");
        MIMETYPES_FROM_EXTENSIONS.put("xl", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xla", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
        MIMETYPES_FROM_EXTENSIONS.put("xlb", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xlc", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xld", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xlk", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xll", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xlm", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xls", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        MIMETYPES_FROM_EXTENSIONS.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
        MIMETYPES_FROM_EXTENSIONS.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        MIMETYPES_FROM_EXTENSIONS.put("xlt", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        MIMETYPES_FROM_EXTENSIONS.put("xlv", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xlw", "application/vnd.ms-excel");
        MIMETYPES_FROM_EXTENSIONS.put("xml", "text/xml");
        MIMETYPES_FROM_EXTENSIONS.put("xpm", "image/x-xpixmap");
        MIMETYPES_FROM_EXTENSIONS.put("xsig", "text/xml");
        MIMETYPES_FROM_EXTENSIONS.put("xul", "application/vnd.mozilla.xul+xml");
        MIMETYPES_FROM_EXTENSIONS.put("z", "application/x-compress");
        MIMETYPES_FROM_EXTENSIONS.put("zip", "application/zip");
    }//static

    private static final Map<String, String> EXTENSIONS_FROM_MIMETYPES = Collections.synchronizedMap(new HashMap<String, String>());
    static {
        EXTENSIONS_FROM_MIMETYPES.put("application/acad", "dwg");
        EXTENSIONS_FROM_MIMETYPES.put("application/arj", "arj");
        EXTENSIONS_FROM_MIMETYPES.put("application/dxf", "dxf");
        EXTENSIONS_FROM_MIMETYPES.put("application/excel", "xls");
        EXTENSIONS_FROM_MIMETYPES.put("application/gnutar", "tgz");
        EXTENSIONS_FROM_MIMETYPES.put("application/java-archive", "jar");
        EXTENSIONS_FROM_MIMETYPES.put("application/javascript", "js");
        EXTENSIONS_FROM_MIMETYPES.put("application/json", "json");
        EXTENSIONS_FROM_MIMETYPES.put("application/mspowerpoint", "pot");
        EXTENSIONS_FROM_MIMETYPES.put("application/mspowerpoint", "pps");
        EXTENSIONS_FROM_MIMETYPES.put("application/msword", "doc");
        EXTENSIONS_FROM_MIMETYPES.put("application/mswrite", "wri");
        EXTENSIONS_FROM_MIMETYPES.put("application/ogg", "ogx");
        EXTENSIONS_FROM_MIMETYPES.put("application/pdf", "pdf");
        EXTENSIONS_FROM_MIMETYPES.put("application/postscript", "ps");
        EXTENSIONS_FROM_MIMETYPES.put("application/postscript", "eps");
        EXTENSIONS_FROM_MIMETYPES.put("application/powerpoint", "ppt");
        EXTENSIONS_FROM_MIMETYPES.put("application/rtf", "rtf");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.amazon.ebook", "azw");;
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.apple.installer+xml", "mpkg");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.mozilla.xul+xml", "xul");		
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-excel", "xls");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-excel.addin.macroEnabled.12", "xlam");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-excel.sheet.binary.macroEnabled.12", "xlsb");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-excel.sheet.macroEnabled.12", "xlsm");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-powerpoint", "ppt");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-powerpoint.addin.macroEnabled.12", "ppam");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-powerpoint.presentation.macroEnabled.12", "pptm");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-powerpoint.slide.macroEnabled.12", "sldm");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-powerpoint.slideshow.macroEnabled.12", "ppsm");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-project", "mpp");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.ms-word.document.macroEnabled.12", "docm");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.chart", "odc");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.database", "odb");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.formula", "odf");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.graphics", "odg");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.graphics-template", "otg");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.image", "odi");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.presentation", "odp");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.presentation-template", "otp");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.spreadsheet", "ods");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.spreadsheet-template", "ots");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.text", "odt");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.text-master", "odm");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.text-template", "ott");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.text-web", "oth");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.openxmlformats-officedocument.presentationml.slide", "sldx");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "ppsx");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.openxmlformats-officedocument.presentationml.template", "potx");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.openxmlformats-officedocument.spreadsheetml.template", "xltx");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.stardivision.calc", "sdc");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.stardivision.chart", "sds");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.stardivision.draw", "sda");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.stardivision.impress", "sdd");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.stardivision.impress.packed", "sdp");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.stardivision.mail", "sdm");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.stardivision.math", "smf");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.stardivision.writer", "sdw");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.stardivision.writer-global", "sgl");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.calc", "sxc");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.calc.template", "stc");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.draw", "sxd");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.draw.template", "std");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.impress", "sxi");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.impress.template", "sti");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.math", "sxm");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.writer", "sxw");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.writer.global", "sxg");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.sun.xml.writer.template", "stw");
        EXTENSIONS_FROM_MIMETYPES.put("application/wordperfect", "wpd");
        EXTENSIONS_FROM_MIMETYPES.put("application/wordperfect6.0", "w60");
        EXTENSIONS_FROM_MIMETYPES.put("application/wordperfect6.1", "w61");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-7z-compressed", "7z");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-abiword", "abw");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-bzip", "bz");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-bzip2", "bz2");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-compress", "z");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-csh", "csh");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-csh", "epub");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-dvi", "dvi");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-gtar", "gtar");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-gzip", "gz");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-gzip", "gzip" );
        EXTENSIONS_FROM_MIMETYPES.put("application/x-helpfile", "hlp");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-latex", "latex");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-lotus", "wq1");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-pagemaker", "pm4");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-pagemaker", "pm5");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-project", "mpx");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-rar-compressed", "rar");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-shockwave-flash", "swf");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-tar", "tar");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-tex", "tex");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-texinfo", "texi");
        EXTENSIONS_FROM_MIMETYPES.put("application/x-visio", "vsd");
        EXTENSIONS_FROM_MIMETYPES.put("application/xhtml+xml", "xhtml");
        EXTENSIONS_FROM_MIMETYPES.put("application/xml", "xml");
        EXTENSIONS_FROM_MIMETYPES.put("application/zip", "zip");
        EXTENSIONS_FROM_MIMETYPES.put("audio/aac", "aac");
        EXTENSIONS_FROM_MIMETYPES.put("audio/midi", "mid");
        EXTENSIONS_FROM_MIMETYPES.put("audio/midi", "midi");
        EXTENSIONS_FROM_MIMETYPES.put("audio/ogg", "oga");
        EXTENSIONS_FROM_MIMETYPES.put("audio/webm", "weba");
        EXTENSIONS_FROM_MIMETYPES.put("audio/x-wav", "wav");
        EXTENSIONS_FROM_MIMETYPES.put("font/ttf", "ttf");
        EXTENSIONS_FROM_MIMETYPES.put("font/woff", "woff");
        EXTENSIONS_FROM_MIMETYPES.put("font/woff2", "woff2");
        EXTENSIONS_FROM_MIMETYPES.put("image/bmp", "bmp");
        EXTENSIONS_FROM_MIMETYPES.put("image/gif", "gif");
        EXTENSIONS_FROM_MIMETYPES.put("image/jpeg", "jpg");
        EXTENSIONS_FROM_MIMETYPES.put("image/pict", "pic");
        EXTENSIONS_FROM_MIMETYPES.put("image/png", "png");
        EXTENSIONS_FROM_MIMETYPES.put("image/svg+xml", "svg");
        EXTENSIONS_FROM_MIMETYPES.put("image/tiff", "tiff");
        EXTENSIONS_FROM_MIMETYPES.put("image/webp", "webp");
        EXTENSIONS_FROM_MIMETYPES.put("image/x-icon", "ico");
        EXTENSIONS_FROM_MIMETYPES.put("image/x-pcx", "pcx");
        EXTENSIONS_FROM_MIMETYPES.put("image/x-pict", "pct");
        EXTENSIONS_FROM_MIMETYPES.put("image/x-portable-anymap", "pnm");
        EXTENSIONS_FROM_MIMETYPES.put("image/x-portable-bitmap", "pbm");
        EXTENSIONS_FROM_MIMETYPES.put("image/x-portable-pixmap", "ppm");
        EXTENSIONS_FROM_MIMETYPES.put("image/x-xbitmap", "xbm");
        EXTENSIONS_FROM_MIMETYPES.put("image/x-xpixmap", "xpm");
        EXTENSIONS_FROM_MIMETYPES.put("model/vnd.dwf", "dwf");
        EXTENSIONS_FROM_MIMETYPES.put("text/calendar", "ics");
        EXTENSIONS_FROM_MIMETYPES.put("text/html", "html");
        EXTENSIONS_FROM_MIMETYPES.put("text/plain", "txt");
        EXTENSIONS_FROM_MIMETYPES.put("text/sgml", "sgm");
        EXTENSIONS_FROM_MIMETYPES.put("text/xml", "xml");
        EXTENSIONS_FROM_MIMETYPES.put("video/3gpp", "3gp"); //2018/CONC/000003
        EXTENSIONS_FROM_MIMETYPES.put("video/3gpp2", "3g2");
        EXTENSIONS_FROM_MIMETYPES.put("video/avi", "avi");
        EXTENSIONS_FROM_MIMETYPES.put("video/fli", "fli");
        EXTENSIONS_FROM_MIMETYPES.put("video/mpeg", "mpg");
        EXTENSIONS_FROM_MIMETYPES.put("video/ogg", "ogv");
        EXTENSIONS_FROM_MIMETYPES.put("video/quicktime", "mov");
        EXTENSIONS_FROM_MIMETYPES.put("video/quicktime", "qt");
        EXTENSIONS_FROM_MIMETYPES.put("video/webm", "webm");
        EXTENSIONS_FROM_MIMETYPES.put("video/x-motion-jpeg", "mjpg");
        EXTENSIONS_FROM_MIMETYPES.put("video/x-ms-asf", "asf");
        EXTENSIONS_FROM_MIMETYPES.put("video/x-msvideo", "avi");
        EXTENSIONS_FROM_MIMETYPES.put("windows/metafile", "wmf");
    }//static

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/
    private MimeTypes() {}

	/**
	 * Metodo que tranforma comprueba si recibe un String.
	 * Si lo recibe un mimeType lo devuelve, si es una extension lo que recibe lo transforma en mimeType
	 * @param mimeTypeOrExtension
	 * @return 
	 */
	public static String getExtension(String extensionOrMimeType) {
		if (extensionOrMimeType.contains(ConstantesDatos.BARRA)) {
			return guessExtensionFromMimeType(extensionOrMimeType);
		}else{
			return extensionOrMimeType;
		}
	}
	
	/**
	 * Metodo que tranforma comprueba si recibe un String.
	 * Si lo recibe una extension la devuelve, si es un mimeType lo que recibe lo transforma en extension
	 * @param mimeTypeOrExtension
	 * @return 
	 */
	public static String getMimeType(String mimeTypeOrExtension) {
		if (!mimeTypeOrExtension.contains(ConstantesDatos.BARRA)) {
			return guessMimeTypeFromExtension(mimeTypeOrExtension);
		}else{
			return mimeTypeOrExtension;
		}
	}
	
	
    public static String guessMimeType(String suggestedMimeType, String fileName) {
        return guessMimeType(suggestedMimeType, fileName, null);
    }

    public static String guessMimeType(String suggestedMimeType, String fileName, byte[] fileContents) {
        String result = null;
        if (fileContents!=null) {
            /* try to analyze magic numbers */
        }//if

        if (result!=null) return result;

        String mimeType = suggestedMimeType;
        if (mimeType!=null && mimeType!="application/x-7z-compressed") {
            mimeType = mimeType.trim().toLowerCase();
        }//if
        String extension = null;
        if (fileName!=null) {
            int idx = fileName.lastIndexOf(".");
            if (idx>=0) {
                extension = fileName.substring(idx + 1);
                if (extension!=null) extension = extension.trim().toLowerCase();
            }//if
        }//if

        if (!BasicValidations.isEmpty(extension)) {
            /* Try to guess something from file extension */
            result = guessMimeTypeFromExtension(extension);
            if (result==null) {
                result = suggestedMimeType;
            }
        } else {
            /*Believe in suggested mime type*/
            result = suggestedMimeType;
        }//if

        if (result==null) {
            result = BINARY[0];
        }//if
        return result;
    }//guessMimeType
    
    private static String internalGetExtensionFromMimeType (String mimeType){
    	String resultado="";
    	if(mimeType!=null){
    		if(mimeType.contains("excel")){
    			resultado=FILEEXTENSION_XLS;
    		}
    		if(mimeType.contains("rtf")){
    			resultado=FILEEXTENSION_RTF;
    		}
    		if(mimeType.contains("msword")){
    			resultado=FILEEXTENSION_DOC;
    		}
    		if(mimeType.contains("rar")){
    			resultado=FILEEXTENSION_RAR;
    		}
    		if(mimeType.contains("zip")){
    			resultado=FILEEXTENSION_ZIP;
    		}
    		if(mimeType.contains("pdf")){
    			resultado=FILEEXTENSION_PDF;
    		}
    		if(mimeType.contains("plain")){
    			resultado=FILEEXTENSION_XSIG;
    		}
            if(mimeType.contains("text/xml")){
                resultado=FILEEXTENSION_XSIG;
            }
    		if(mimeType.contains("opendocument.text")){
    			resultado=FILEEXTENSION_ODT;
    		}
    		if(mimeType.contains("opendocument.presentation")){
    			resultado=FILEEXTENSION_ODP;
    		}
    		if(mimeType.contains("opendocument.spreadsheet")){
    			resultado=FILEEXTENSION_ODS;
    		}
    		if(mimeType.contains("7z")){
    			resultado=FILEEXTENSION_7z;
    		}
    		if(mimeType.contains("powerpoint")){
    			resultado=FILEEXTENSION_PPT;
    		}
    		if(mimeType.contains("bmp")){
    			resultado=FILEEXTENSION_BMP;
    		}
    		if(mimeType.contains("jpeg")){
    			resultado="jpg";
    		}
    		if(mimeType.contains("tif")){
    			resultado=FILEEXTENSION_TIFF;
    		}
    		if(mimeType.contains("sun.xml.writer")){
    			resultado=FILEEXTENSION_SXW;
    		}
    		if(mimeType.contains("stardivision.writer")){
    			resultado=FILEEXTENSION_SDW;
    		}
    		if(mimeType.contains("abiword")){
    			resultado=FILEEXTENSION_ABW;
    		}
            if(mimeType.contains("openxmlformats-officedocument.wordprocessingml.document")) {
                resultado="docx";
            }
            if(mimeType.contains("openxmlformats-officedocument.spreadsheetml.sheet")) {
                resultado="xlsx";
            }
            if(mimeType.contains("openxmlformats-officedocument.presentationml.presentation")) {
                resultado="pptx";
            }
    	}

    	if(!resultado.equals("")){
    		resultado=".".concat(resultado);
    	} else {
            resultado = "";
        }
        
    	return resultado;
    }

	/**
	 * Este metodo recibe un fichero (que tiene extension). LLama al metodo guessExtensionFromName()
	 * Que devuelve solo la extension del archivo, y con esta extension llamamos al metodo
	 * guessMimeTypeFromExtension(), que con la extension nos devuelve el tipo de fichero
	 * que es.
	 * @param fileName Nombre del fichero con extension
	 * @return 
	 */
	public static String guessMimeTypeFromFileName(String fileName) {
		String extension = guessExtensionFromName(fileName);
		if(extension != null){
			return guessMimeTypeFromExtension(extension);
		}
		return null;
	}
	
    public static String guessMimeTypeFromExtension(String extension){
        String result = null;
        if (extension!=null) {
            extension = extension.trim().toLowerCase();
            result = MIMETYPES_FROM_EXTENSIONS.get(extension);
        }//if
        if (result==null) {
            result = BINARY[0];
        }//if
        return result;
    }//guessMimeTypeFromExtension

    public static String guessExtensionFromMimeType(String mimeType) {
        String result = null;
        if (mimeType!=null) {
            mimeType = mimeType.trim().toLowerCase();
            result = EXTENSIONS_FROM_MIMETYPES.get(mimeType);

            if (result==null) {
                result = internalGetExtensionFromMimeType(mimeType);
            }//if
        }//if
        if (result==null) {
            result = "";
        }//if
        return result;
    }//guessExtensionFromMimeType

    public static String guessExtensionFromName(String fileName) {
        String extension = null;
        if (fileName!=null) {
            int idx = fileName.lastIndexOf(".");
            if (idx>=0) {
                extension = fileName.substring(idx + 1);
                if (extension!=null) extension = extension.trim().toLowerCase();
            }//if
        }//if
        return extension;
    }//guessExtensionFromName

    public static String guessNameWithoutExtension(String fileName) {
        String nameAlone = null;
        if (fileName!=null) {
            int idx = fileName.lastIndexOf(".");
            if (idx>=0) {
                nameAlone = fileName.substring(0,idx);
            }//if
        }//if
        return nameAlone;
    }//guessExtensionFromName

    
}//class
/*______________________________EOF_________________________________*/
