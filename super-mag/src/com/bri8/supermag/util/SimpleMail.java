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

import com.bri8.supermag.model.MailContent;

public class SimpleMail {
	private static SimpleMail _instance = new SimpleMail();

	private SimpleMail() {
		Properties mailProperties = new Properties();
		session = Session.getDefaultInstance(mailProperties, null);
	}

	private final Session session;
	private static Log logger = LogFactory.getLog(SimpleMail.class);

	public void sendMessageTo(MailContent mailContent) throws MessagingException {
		Message msg = new MimeMessage(session);
		// set headers
		msg.setFrom(InternetAddress.parse(mailContent.getFrom(), false)[0]);
		// msg.setHeader("X-Mailer", mailer);
		msg.setSentDate(new Date());
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailContent.getTo(), false));

		msg.setSubject(mailContent.getSubject());
		msg.setText(mailContent.getBody());

		Transport.send(msg);
		log(String.format("message sent [from:%s, to :%s]", mailContent.getFrom(), mailContent.getTo()));

	}

	public static SimpleMail get_instance() {
		return _instance;
	}

	void log(String str) {
		logger.info(new SimpleDateFormat("yyy-mm-dd").format(new Date()) + str);
	}

}
