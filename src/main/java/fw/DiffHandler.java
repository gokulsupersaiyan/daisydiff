package fw;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.outerj.daisy.diff.DaisyDiff;
import org.outerj.daisy.diff.HtmlCleaner;
import org.outerj.daisy.diff.XslFilter;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class DiffHandler implements RequestHandler<DiffHandler.RequestClass, DiffHandler.ResponseClass> {

    @Override
    public ResponseClass handleRequest(RequestClass input, Context context) {
        ResponseClass responseClass = new ResponseClass();
        responseClass.result = " Result :: " + input.source  + "" + input.destination;
        return responseClass;
    }

    public static class RequestClass {
        public String source;
        public String destination;
    }


    public static class ResponseClass {
        public String result;
    }

    public static void main(String[] args) throws Exception {
        mainDemo(new String[] {"sample/example1.htm","sample/example2.htm" });
    }


    static boolean quietMode = false;

    public static void mainDemo(String[] var0) throws URISyntaxException {
        if (var0.length < 2) {
            help();
        }

        boolean var1 = true;
        boolean var2 = true;
        String var3 = "daisydiff.htm";
        String[] var4 = new String[0];

        try {
            for(int var5 = 2; var5 < var0.length; ++var5) {
                String[] var6 = var0[var5].split("=");
                if (var6[0].equalsIgnoreCase("--file")) {
                    var3 = var6[1];
                } else if (var6[0].equalsIgnoreCase("--type")) {
                    if (var6[1].equalsIgnoreCase("tag")) {
                        var1 = false;
                    }
                } else if (var6[0].equalsIgnoreCase("--css")) {
                    var4 = var6[1].split(";");
                } else if (var6[0].equalsIgnoreCase("--output")) {
                    if (var6[1].equalsIgnoreCase("xml")) {
                        var2 = false;
                    }
                } else if (var6[0].equals("--q")) {
                    quietMode = true;
                } else {
                    help();
                }
            }

            if (!quietMode) {
                System.out.println("            ______________");
                System.out.println("           /Daisy Diff 1.2\\");
                System.out.println("          /________________\\");
                System.out.println();
                System.out.println(" -= http://code.google.com/p/daisydiff/ =-");
                System.out.println("");
                System.out.println();
                System.out.println("Comparing documents:");
                System.out.println("  " + var0[0]);
                System.out.println("and");
                System.out.println("  " + var0[1]);
                System.out.println();
                if (var1) {
                    System.out.println("Diff type: html");
                } else {
                    System.out.println("Diff type: tag");
                }

                System.out.println("Writing " + (var2 ? "html" : "xml") + " output to " + var3);
            }

            if (var4.length > 0) {
                if (!quietMode) {
                    System.out.println("Adding external css files:");
                }

                String[] var23 = var4;
                int var25 = var4.length;

                for(int var7 = 0; var7 < var25; ++var7) {
                    String var8 = var23[var7];
                    System.out.println("  " + var8);
                }
            }

            if (!quietMode) {
                System.out.println("");
                System.out.print(".");
            }

            SAXTransformerFactory var24 = (SAXTransformerFactory)TransformerFactory.newInstance();
            TransformerHandler var26 = var24.newTransformerHandler();
            var26.setResult(new StreamResult(new File(var3)));
            Object var27;
            if (var0[0].startsWith("http://")) {
                var27 = (new URI(var0[0])).toURL().openStream();
            } else {
                var27 = new FileInputStream(var0[0]);
            }

            Object var28;
            if (var0[1].startsWith("http://")) {
                var28 = (new URI(var0[1])).toURL().openStream();
            } else {
                var28 = new FileInputStream(var0[1]);
            }

            XslFilter var9 = new XslFilter();
            Object var10;
            if (var1) {
                var10 = var2 ? var9.xsl(var26, "org/outerj/daisy/diff/htmlheader.xsl") : var26;
                Locale var11 = Locale.getDefault();
                String var12 = "diff";
                HtmlCleaner var13 = new HtmlCleaner();
                InputSource var14 = new InputSource((InputStream)var27);
                InputSource var15 = new InputSource((InputStream)var28);
                DomTreeBuilder var16 = new DomTreeBuilder();
                var13.cleanAndParse(var14, var16);
                System.out.print(".");
                TextNodeComparator var17 = new TextNodeComparator(var16, var11);
                DomTreeBuilder var18 = new DomTreeBuilder();
                var13.cleanAndParse(var15, var18);
                System.out.print(".");
                TextNodeComparator var19 = new TextNodeComparator(var18, var11);
                ((ContentHandler)var10).startDocument();
                ((ContentHandler)var10).startElement("", "diffreport", "diffreport", new AttributesImpl());
                doCSS(var4, (ContentHandler)var10);
                ((ContentHandler)var10).startElement("", "diff", "diff", new AttributesImpl());
                HtmlSaxDiffOutput var20 = new HtmlSaxDiffOutput((ContentHandler)var10, var12);
                HTMLDiffer var21 = new HTMLDiffer(var20);
                var21.diff(var17, var19);
                System.out.print(".");
                ((ContentHandler)var10).endElement("", "diff", "diff");
                ((ContentHandler)var10).endElement("", "diffreport", "diffreport");
                ((ContentHandler)var10).endDocument();
            } else {
                var10 = var2 ? var9.xsl(var26, "org/outerj/daisy/diff/tagheader.xsl") : var26;
                ((ContentHandler)var10).startDocument();
                ((ContentHandler)var10).startElement("", "diffreport", "diffreport", new AttributesImpl());
                ((ContentHandler)var10).startElement("", "diff", "diff", new AttributesImpl());
                System.out.print(".");
                DaisyDiff.diffTag(new BufferedReader(new InputStreamReader((InputStream)var27)), new BufferedReader(new InputStreamReader((InputStream)var28)), (ContentHandler)var10);
                System.out.print(".");
                ((ContentHandler)var10).endElement("", "diff", "diff");
                ((ContentHandler)var10).endElement("", "diffreport", "diffreport");
                ((ContentHandler)var10).endDocument();
            }
        } catch (Throwable var22) {
            if (quietMode) {
                System.out.println(var22);
            } else {
                var22.printStackTrace();
                if (var22.getCause() != null) {
                    var22.getCause().printStackTrace();
                }

                if (var22 instanceof SAXException) {
                    ((SAXException)var22).getException().printStackTrace();
                }

                help();
            }
        }

        if (quietMode) {
            System.out.println();
        } else {
            System.out.println("done");
        }

    }

    private static void doCSS(String[] var0, ContentHandler var1) throws SAXException {
        var1.startElement("", "css", "css", new AttributesImpl());
        String[] var2 = var0;
        int var3 = var0.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            AttributesImpl var6 = new AttributesImpl();
            var6.addAttribute("", "href", "href", "CDATA", var5);
            var6.addAttribute("", "type", "type", "CDATA", "text/css");
            var6.addAttribute("", "rel", "rel", "CDATA", "stylesheet");
            var1.startElement("", "link", "link", var6);
            var1.endElement("", "link", "link");
        }

        var1.endElement("", "css", "css");
    }

    private static void help() {
        System.out.println("==========================");
        System.out.println("DAISY DIFF HELP:");
        System.out.println("java -jar daisydiff.jar [oldHTML] [newHTML]");
        System.out.println("--file=[filename] - Write output to the specified file.");
        System.out.println("--type=[html/tag] - Use the html (default) diff algorithm or the tag diff.");
        System.out.println("--css=[cssfile1;cssfile2;cssfile3] - Add external CSS files.");
        System.out.println("--output=[html/xml] - Write html (default) or xml output.");
        System.out.println("--q  - Generate less console output.");
        System.out.println("");
        System.out.println("EXAMPLES: ");
        System.out.println("(1)");
        System.out.println("java -jar daisydiff.jar http://web.archive.org/web/20070107145418/http://news.bbc.co.uk/ http://web.archive.org/web/20070107182640/http://news.bbc.co.uk/ --css=http://web.archive.org/web/20070107145418/http://news.bbc.co.uk/nol/shared/css/news_r5.css");
        System.out.println("(2)");
        System.out.println("java -jar daisydiff.jar http://cocoondev.org/wiki/291-cd/version/15/part/SimpleDocumentContent/data http://cocoondev.org/wiki/291-cd/version/17/part/SimpleDocumentContent/data --css=http://cocoondev.org/resources/skins/daisysite/css/daisy.css --output=xml --file=daisysite.htm");
        System.out.println("==========================");
        System.exit(0);
    }
}
