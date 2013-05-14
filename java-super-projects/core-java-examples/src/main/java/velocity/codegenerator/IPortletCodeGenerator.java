package velocity.codegenerator;

import java.util.List;

import org.apache.velocity.VelocityContext;

public interface IPortletCodeGenerator {
	public String applyTemplates(List templateList, VelocityContext context) throws Exception;
}
