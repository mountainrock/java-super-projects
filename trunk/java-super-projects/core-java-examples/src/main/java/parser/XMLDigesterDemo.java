package parser;

import java.io.StringBufferInputStream;

import org.apache.commons.digester.Digester;

public class XMLDigesterDemo {
	protected Object parsesampleXML(String fullText) throws Exception
	{
		Digester sampleDigester = new Digester();
		sampleDigester.addObjectCreate("root", Object.class);
		sampleDigester.addCallMethod("root/test/testb/source", "setSource", 0);

		sampleDigester.addCallMethod("root/test/dochead", "setType", 0);
		sampleDigester.addCallMethod("root/test/testb/url", "setUrl", 0);
		sampleDigester.addCallMethod("root/test/cpyrt/collab", "setCopyright", 0);
		sampleDigester.addCallMethod("root/test/cpyrt/year", "setCopyrightYear", 0);

		// for publication date.
		sampleDigester.addCallMethod("root/test/history/pub/date/day", "setTempPublicationDay", 0);
		sampleDigester.addCallMethod("root/test/history/pub/date/month", "setTempPublicationMonth", 0);
		sampleDigester.addCallMethod("root/test/history/pub/date/year", "setTempPublicationYear", 0);// this calls setPublicationDate

		// set credit
		sampleDigester.addObjectCreate("root/test/testb/testaug/au", Object.class);
		sampleDigester.addCallMethod("root/test/testb/testaug/au/fnm", "setFName", 0);
		sampleDigester.addCallMethod("root/test/testb/testaug/au/mi", "setMName", 0);
		sampleDigester.addCallMethod("root/test/testb/testaug/au/snm", "setSurName", 0);
		sampleDigester.addCallMethod("root/test/testb/testaug/au/email", "setEmail", 0);

		sampleDigester.addSetNext("root/test/testb/testaug/au", "addAuth");// sets credit, auth and authemail

		// image details
		sampleDigester.addObjectCreate("root/testbody/sec/fig", Object.class);
		sampleDigester.addSetProperties("root/testbody/sec/fig", "id", "imageId");
		sampleDigester.addSetProperties("root/testbody/sec/fig/graphic", "file", "fileName");
		sampleDigester.addCallMethod("root/testbody/sec/fig/title/p", "setDocHead", 0);
		sampleDigester.addCallMethod("root/testbody/sec/fig/caption/p", "setCaption", 0);
		sampleDigester.addCallMethod("root/testbody/sec/fig/text/p", "setText", 0);

		sampleDigester.addSetNext("root/testbody/sec/fig", "addImage");

		sampleDigester.addObjectCreate("root/testbody/sec/sec/fig", Object.class);
		sampleDigester.addSetProperties("root/testbody/sec/sec/fig", "id", "imageId");
		sampleDigester.addSetProperties("root/testbody/sec/sec/fig/graphic", "file", "fileName");
		sampleDigester.addCallMethod("root/testbody/sec/sec/fig/title/p", "setDocHead", 0);
		sampleDigester.addCallMethod("root/testbody/sec/sec/fig/caption/p", "setCaption", 0);
		sampleDigester.addCallMethod("root/testbody/sec/sec/fig/text/p", "setText", 0);

		sampleDigester.addSetNext("root/testbody/sec/sec/fig", "addImage");

		String xpath = "sample/test";
		Object fieldName = "mytestfield";
		// An example to show passing field params from xml...****
		sampleDigester.addCallMethod(xpath, "addField", 2);
		sampleDigester.addObjectParam(xpath, 0, fieldName);
		sampleDigester.addCallParam(xpath, 1);

		StringBufferInputStream xml = new StringBufferInputStream(fullText);
		Object sampleXMLTO = null;

		sampleXMLTO = (Object) sampleDigester.parse(xml);

		return sampleXMLTO;
	}

}
