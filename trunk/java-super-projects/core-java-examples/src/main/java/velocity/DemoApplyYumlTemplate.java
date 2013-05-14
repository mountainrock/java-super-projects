package velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class DemoApplyYumlTemplate {

	private static Log logger = LogFactory.getLog(DemoApplyYumlTemplate.class);
	VelocityEngine ve = new VelocityEngine();
	String inputYuml = "src/main/resources/yuml-sample.txt";
	String outputDir = "src/main/resources/output-yuml";

	public static void main(String[] args) throws Exception {
		new DemoApplyYumlTemplate().doIt();
		// logger.info(yuml);
	}

	private void doIt() throws IOException, Exception {
		ve.init();
		String yuml = FileUtils.readFileToString(new File(inputYuml));
		Scanner scanner = new Scanner(yuml);
		Map memberVariables = new LinkedHashMap();

		while (scanner.hasNextLine()) {
			VelocityContext context = new VelocityContext();

			context.put("date", new Date().toString());
			context.put("StringUtil", new StringUtils());

			String line = scanner.nextLine();
			String[] entities = StringUtils.substringsBetween(line, "[", "]");
			String entityName = null;
			for (int i = 0; i < entities.length; i++) {
				String entity = entities[i];
				// sanitize
				entity = StringUtils.replace(entity, "{bg:orange}", "");

				logger.info(entity);
				String[] tokens = entity.split("\\|");

				if (tokens.length == 2) {
					entityName = tokens[0];

					String memberVariablesEncoded = tokens[1];
					String[] variableKeyValEncoded = memberVariablesEncoded.split(";");
					for (int j = 0; j < variableKeyValEncoded.length; j++) {
						logger.info("processing : " + variableKeyValEncoded[j]);
						String[] variableKeyVal = variableKeyValEncoded[j].split(":");
						String variableName = variableKeyVal[0];
						String variableType = variableKeyVal.length == 2 ? variableKeyVal[1] : "$TYPE_NOT_FOUND";
						variableName = variableName.replace("+", "");
						memberVariables.put(variableName, variableType);
					}
				} else {
					entityName = tokens[0];
				}
				context.put("className", entityName);
				context.put("memberVariables", memberVariables);
				generateCode(context);
			}
		}
	}

	private void generateCode(VelocityContext context) throws Exception, IOException {
		Template t = ve.getTemplate("src/main/resources/yuml2java.vm");

		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		logger.info(writer.toString());
	}
}
