package velocity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import utils.VelocityUtil;

public class XmlGenerator {
	
	Map<String,String> outputXmls=new HashMap<String, String>();
	String source="SAP";////BF / SAP
	String outputDir="C:/dev/trunk/modules/com.xyz.testautomation/datapack/qa/ST5/CRM_FEED/branch_feed/marklogic-feed";
	
	String removeMandatoryAttributes[]=new String[]{"advert:suppressaddress", "advert:suppressseo", "advert:suppressmap", "advert:suppressbip", "advert:suppressmerchant"};
	String removeMandatoryNodes[]=new String[]{"orgname", "postcode","postcodearea","postcodedistrict","postcodesector","postcodeunit","address"};
	String badEmails[]=new String[]{"..","longemail","@@",".","&amp;"};
	String templateLocation="src/main/resources/templates/listing-template.vm";
	VelocityContext context= new VelocityContext();

	public static void main(String[] args) throws Exception {
		new XmlGenerator().doIt();
	
	}

	private void doIt() throws Exception {
		long uniqueIdL=14541680;
	    String uniqueId= String.format("%010d", uniqueIdL);
	    
		String email="test@test.com";
		String displayable="true";
		context.put("source", source); 
		context.put("uniqueId", uniqueId);
		context.put("displayable", displayable);
		context.put("email", email);
		
		String branchXml=VelocityUtil.applyTemplate(templateLocation, context);
		//System.out.println(result);
		
		generateCombinations(uniqueId, branchXml);
		
		store();
	}

	private void store() {
		System.out.println(outputXmls);
		for (Map.Entry<String, String> entry : outputXmls.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			try {
				FileUtils.writeStringToFile(new File(outputDir, key), value);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}
	}

	private void generateCombinations(String uniqueId,String branchXml) throws Exception {
		Builder builder = new Builder();
		generateMissingNodeBasedSamples(uniqueId, branchXml, builder);
		generateMissingAttributeBasedSamples(uniqueId, branchXml, builder);
		generateBadEmailSamples(uniqueId, builder);
	}
	private void generateBadEmailSamples(String uniqueId, Builder builder) throws ParsingException, ValidityException, IOException {
		for (String email : badEmails) {
			String fileName=""+uniqueId+"_"+source+"_badEmail"+StringUtils.capitalize(email)+".xml";
			context.put("email", email);
			String branchXml=VelocityUtil.applyTemplate(templateLocation, context);
			System.out.println("Generating : "+ fileName);
			Document document = builder.build(new ByteArrayInputStream(branchXml.getBytes()));
			outputXmls.put(fileName,document.toXML());
		}
	}
	
	private void generateMissingNodeBasedSamples(String uniqueId, String branchXml, Builder builder) throws ParsingException, ValidityException, IOException {
		for (String nodeName : removeMandatoryNodes) {
			String fileName=""+uniqueId+"_"+source+"_missingElement"+StringUtils.capitalize(nodeName)+".xml";
			Document document = builder.build(new ByteArrayInputStream(branchXml.getBytes()));
			Nodes nodes = document.query("//"+nodeName);
			nodes.get(0).detach();
			outputXmls.put(fileName,document.toXML());
		}
	}
	private void generateMissingAttributeBasedSamples(String uniqueId, String branchXml, Builder builder) throws ParsingException, ValidityException, IOException {
		for (String encodedAttribute : removeMandatoryAttributes) {
			String node=encodedAttribute.substring(0,encodedAttribute.indexOf(":"));
			String attribute= encodedAttribute.substring(encodedAttribute.indexOf(":")+1);
			String fileName="CREATE_400_"+uniqueId+"_"+source+"-missingAttribute-"+StringUtils.capitalize(node)+"-"+attribute+".xml";
			Document document = builder.build(new ByteArrayInputStream(branchXml.getBytes()));
			Nodes nodes = document.query("//"+node);
			
			Element element = (Element) nodes.get(0);
			element.getAttribute(attribute).detach();//removeAttribute(new Attribute(attribute,"false"));
			outputXmls.put(fileName,document.toXML());
		}
	}
	
}
