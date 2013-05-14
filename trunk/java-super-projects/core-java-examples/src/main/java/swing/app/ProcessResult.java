/**
 * 
 */
package swing.app;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProcessResult extends Observable {

	private List<String> failed = new ArrayList<String>();

	private List<String> success = new ArrayList<String>();

	List listArticleXMLTO = new ArrayList();

	private static Log logger = LogFactory.getLog(ProcessResult.class.getName());

	private String statusMessage;

	public void reset()
	{
		failed.clear();
		success.clear();
		listArticleXMLTO.clear();
		statusMessage = "";
	}

	public String getStatusMessage()
	{
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage)
	{
		this.statusMessage = statusMessage;
		setChanged();
		notifyObservers();
	}

	public void addObserver(Observer ob)
	{
		super.addObserver(ob);
	}

	public List getListArticleXMLTO()
	{
		return listArticleXMLTO;
	}

	public void setListArticleXMLTO(List listArticleXMLTO)
	{
		this.listArticleXMLTO = listArticleXMLTO;
	}

	public List<String> getFailed()
	{
		return failed;
	}

	public void setFailed(List<String> failed)
	{
		this.failed = failed;
	}

	public List<String> getSuccess()
	{
		return success;
	}

	public void setSuccess(List<String> success)
	{
		this.success = success;
	}

	@Override
	public String toString()
	{

		return ReflectionToStringBuilder.toString(this);
	}

}
