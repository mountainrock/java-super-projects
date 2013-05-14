/**
 * 
 */
package email;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SimpleMail {
	private String smtpPort = null;
	private String smtpServer = null;
	// private final String USERNAME = "Sandeep.Maloth";
	// private final String PASSWORD = "***";//your password goes here.
	private final Transport transport;
	private final Session session;
	private final String FROM = "sandeep.maloth@test.com";

	SimpleMail(String smtpServer, String smtpPort) throws MessagingException {
		Properties mailProperties = new Properties();
		mailProperties.put("mail.smtp.host", smtpServer);
		mailProperties.put("mail.smtp.port", smtpPort);

		mailProperties.put("mail.smtp.auth", "false");

		session = Session.getInstance(mailProperties);
		transport = session.getTransport("smtp");
		transport.connect(smtpServer, "", "");// , USERNAME, PASSWORD);
		this.smtpServer = smtpServer;
		this.smtpPort = smtpPort;

	}

	public void sendMessageTo(String to, MailContent mailContent) throws MessagingException
	{
		Message msg = new MimeMessage(session);
		// set headers
		msg.setFrom(InternetAddress.parse(FROM, false)[0]);
		// msg.setHeader("X-Mailer", mailer);
		msg.setSentDate(new Date());
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

		// set title and body

		msg.setSubject(mailContent.getSubject());
		msg.setText(mailContent.getBody());

		// off goes the message...
		transport.sendMessage(msg, msg.getAllRecipients());
		log("message sent to :" + to);

	}

	
	public static void main(String[] args) throws MessagingException
	{
		if (args.length == 0) {
			System.out.println("Usage SimpleMail <SMTP HOST> <SMTP_PORT>  ");
			return;
		}

		final String subject = "test mail";
		final String messageBody = "this is a test mail";
		final String sendTo = "sandeep.maloth@test.com";
		SimpleMail sm = new SimpleMail(args[0], args[1]);
		sm.sendMail(sendTo, subject, messageBody);
	}

	public void sendMail(final String sendTo, final String subject, final String messageBody) throws MessagingException
	{
		MailContent mailContent = new MailContent(subject, messageBody);
		sendMessageTo(sendTo, mailContent);
	}

	void log(String str)
	{
		System.out.println(new SimpleDateFormat("yyy-mm-dd").format(new Date()) + str);
	}

	class MailContent {
		String subject;
		String body;

		MailContent() {
		}

		MailContent(String body) {
			this.body = body;
		}

		MailContent(String sub, String body) {
			this.subject = sub;
			this.body = body;
		}

		public String getBody()
		{
			return body;
		}

		public void setBody(String body)
		{
			this.body = body;
		}

		public String getSubject()
		{
			return subject;
		}

		public void setSubject(String subject)
		{
			this.subject = subject;
		}

	}
}
