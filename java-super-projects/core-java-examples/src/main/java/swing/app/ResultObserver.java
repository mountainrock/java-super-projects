package swing.app;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ResultObserver implements Observer {
	private static Log logger = LogFactory.getLog(ResultObserver.class.getName());
	ProcessResult processResult;
	JTextArea statusArea;

	public ResultObserver(ProcessResult extractionResult, JTextArea statusArea) {
		this.processResult = extractionResult;
		this.statusArea = statusArea;
	}

	public void update(Observable o, Object arg)
	{
		if (o != null) {
			final String msg = processResult.getStatusMessage().toString();

			statusArea.append(msg);
			statusArea.setCaretPosition(statusArea.getDocument().getLength());
			// statusArea.updateUI();
			if (logger.isDebugEnabled()) {
				logger.debug("observer " + msg);
			}
		}
	}
}