package velocity;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class VelocityTemplateApplyNIH {

	
	public static void main(String[] args) throws Exception
	{
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		// VelocityContext context = new VelocityContext();

		Template t = ve.getTemplate("src/main/resources/generate-xsl-nodes.vm");
		String genericFlds[] = { "Source of record", "Trial ID", "Original record", "Date trial ID assigned", "Date applied", "Date updated", "ClinicalTrials.gov ID",
				"Date extracted from original register", "Public title", "Scientific title", "Acronym", "Local reference number(s)", "Design/methodology", "Study hypothesis", "Ethics Approval",
				"Country of recruitment", "Disease/Condition", "Participants - inclusion criteria", "Participants - exclusion criteria", "Patient information material", "Anticipated start date",
				"Anticipated end date", "Status of trial", "Target number of participants", "Interventions", "Primary outcome measure(s)", "Secondary outcome measure(s)", "Source of funding",
				"Publications", "Sponsor name", "Sponsor details", "Sponsor telephone", "Sponsor fax", "Sponsor email", "Sponsor website", "Contact name", "Contact details", "Contact telephone",
				"Contact fax", "Contact email" };

		String matchFldsNIH[] = { "Source of record", "ClinicalTrials.gov", "", "", "", "", "ClinicalTrials.gov", "Last updated", "Title of trial/grant title", "", "", "Study ID numbers",
				"Study type and design", "", "", "Locations", "Condition(s)", "Eligibility criteria", "", "Further study details", "Study start", "Expected completion", "Current status of trial", "",
				"Intervention(s)", "Primary outcome", "Secondary outcome", "Information provided by", "Publications", "Sponsors and collaborators", "Sponsor details", "", "", "", "",
				"Overall contact", "Contact details", "", "", "" };

		Map whoMap = new HashMap();
		whoMap.put("Trial ID", "1. Primary Registry and Trial Identifying Number");
		whoMap.put("Date trial ID assigned", "2. Date of Registration in Primary Registry");
		whoMap.put("?", "3. Secondary Identifying Numbers");
		whoMap.put("Source of funding", "4. Source(s) of Monetary or Material Support");
		whoMap.put("Sponsor name", "5. Primary Sponsor");
		whoMap.put("?", "6. Secondary Sponsor(s)");
		whoMap.put("Contact name", "7. Contact for Public Queries");
		whoMap.put("", "8. Contact for Scientific Queries");
		whoMap.put("Public title", "9. Public Title");
		whoMap.put("Scientific title", "10. Scientific Title");
		whoMap.put("Country of recruitment", "11. Countries of Recruitment");
		whoMap.put("Disease/Condition", "12. Health Condition(s) or Problem(s) Studied");
		whoMap.put("Interventions", "13. Intervention(s)");
		whoMap.put("Participants - inclusion criteria", "14. Key Inclusion and Exclusion Criteria");
		whoMap.put("Participants - exclusion criteria", "14. Key Inclusion and Exclusion Criteria");
		whoMap.put("Study hypothesis", "15. Study Type");
		whoMap.put("?", "16. Date of First Enrollment");
		whoMap.put("?", "17. Target Sample Size");
		whoMap.put("Status of trial", "18. Recruitment Status");
		whoMap.put("Primary outcome measure(s)", "19. Primary Outcome(s)");
		whoMap.put("Secondary outcome measure(s)", "20. Key Secondary Outcomes");

		for (int i = 0; i < genericFlds.length; i++) {
			VelocityContext context = new VelocityContext();
			String who = (String) whoMap.get(genericFlds[i]);
			context.put("who", who);
			context.put("genericField", genericFlds[i]);
			context.put("matchFieldTitle", matchFldsNIH[i]);
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			System.out.println(writer.toString());
		}

	}

}
