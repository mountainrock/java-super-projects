/**
 * 
 */
package com.bri8.supermag.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleMail {
	private static SimpleMail _instance = new SimpleMail();

	private SimpleMail() {
		Properties mailProperties = new Properties();
		session = Session.getDefaultInstance(mailProperties, null);
	}

	private final Session session;
	private static Log logger = LogFactory.getLog(SimpleMail.class);
	private final String FROM = "sandeep.maloth@gmail.com";

	public void sendMessageTo(String to, MailContent mailContent) throws MessagingException {
		Message msg = new MimeMessage(session);
		// set headers
		msg.setFrom(InternetAddress.parse(FROM, false)[0]);
		// msg.setHeader("X-Mailer", mailer);
		msg.setSentDate(new Date());
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

		msg.setSubject(mailContent.getSubject());
		msg.setText(mailContent.getBody());

		Transport.send(msg);
		log("message sent to :" + to);

	}

	public static SimpleMail get_instance() {
		return _instance;
	}

	
	public void sendMail(final String sendTo, final String subject, final String messageBody) throws MessagingException {
		MailContent mailContent = new MailContent(subject, messageBody);
		sendMessageTo(sendTo, mailContent);
	}

	void log(String str) {
		logger.info(new SimpleDateFormat("yyy-mm-dd").format(new Date()) + str);
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

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

	}
}
