package convertor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

public class XhtmlURLToPDF {
    /**
     * xhtml sites: http://w3csites.com/sites_thumbs.asp
     */
    public static void main(String[] args) 
            throws IOException, DocumentException {
    	String url="http://google.com";
    	String outFile="src/main/resources/result.xhtml";
    	String errorFile="src/main/resources/error.out";
    	
    	Html2Xhtml t = new Html2Xhtml(url, outFile, errorFile);
    	ByteArrayOutputStream out= new ByteArrayOutputStream();
		t.convert(out);
    	
        //String inputFile = "src/main/resources/sample.xhtml";
        String filePath = new File(outFile).toURI().toURL().toString();
        //String url = new URL("http://www.alainmikli.cz/").toString();
        
    	
        System.out.println(filePath);
        String outputFile = "src/main/resources/htmlToPdf.pdf";
        OutputStream os = new FileOutputStream(outputFile);
        
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(new String(out.toByteArray()));
        renderer.layout();
        renderer.createPDF(os);
        
        os.close();
    }
}
