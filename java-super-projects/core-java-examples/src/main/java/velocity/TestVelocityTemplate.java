package velocity;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class TestVelocityTemplate {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		Velocity.init();
		VelocityContext ctx = new VelocityContext();

		// Bind a map to an identifier in the context.
		String idUsedByVelocityToLogErrorMessagesInParsingOrEvaluatingThisTemplate = "EnrollmentDistributionByPlanType:chartTitle";
		Map map = new HashMap();
		map.put("planType", "HMO");
		map.put("anotherType", "Sandy");
		ctx.put("input", map);
		Writer output = new StringWriter();
		String template = "Enrollment Distribution for $input.planType $input.anotherType $input.planType";

		Velocity.evaluate(ctx, output, idUsedByVelocityToLogErrorMessagesInParsingOrEvaluatingThisTemplate, template);
		System.out.println(output);

		// Rebind a java bean to the identifier used in the template.
		ctx.put("input", new Bean());
		output = new StringWriter();
		Velocity.evaluate(ctx, output, idUsedByVelocityToLogErrorMessagesInParsingOrEvaluatingThisTemplate, template);
		System.out.println(output);
	}

	/**
	 * This class must be public so that Velocity can introspect to obtain properties
	 */
	public static class Bean {
		String planType = "defaultValue";

		String anotherType = "mandy";

		
		public String getAnotherType()
		{
			return anotherType;
		}

		
		public void setAnotherType(String anotherType)
		{
			this.anotherType = anotherType;
		}

		
		public String getPlanType()
		{
			return planType;
		}

		
		public void setPlanType(String property)
		{
			this.planType = property;
		}

	}
}
