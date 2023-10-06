package com.rgt.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailClientUtils {
	
	private static Properties properties = null;
	private static Session session = null;
	
	static {
		properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.connectiontimeout", "10000");
		properties.put("mail.smtp.timeout", "10000");
		properties.put("mail.smtp.starttls.enable", "true");
	//	properties.put("mail.smtp.ssl.enable" , "true");
		
		
		 session = Session.getDefaultInstance(properties, new Authenticator() {

            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("1994sandesh@gmail.com", "ewkvclhctaugopyj");
            }
        });
		
		session = Session.getInstance(properties);
	}
	
	
	
	public static void sendMailWithDefaultConf(String title, String bodyMessage, String toAddress) throws Exception {
		String host = "smtp.gmail.com";
		int port = 587;
		String username = "1994sandesh@gmail.com";
		String password = "ewkvclhctaugopyj";
		String from = "RGTSupport@ratnaglobaltech.com";
		sendMail(host, port, username, password, from, toAddress, title, bodyMessage.toString());
	}
	
	public static void sendMail(String host, int port, String username, String password, String from, String to, String title, String bodyMessage) throws Exception {
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from)); // Sender
			String[] receivers = to.split(";");
			InternetAddress[] iaToList = new InternetAddress().parse(getMailList(receivers));
			msg.setRecipients(Message.RecipientType.TO, iaToList);
			msg.setSentDate(new Date());
			msg.setContent(bodyMessage, "text/html");
			msg.setSubject(title); // Theme
			System.out.println("email msg " +msg); // 
			Transport tran = session.getTransport("smtp");
			tran.connect(host, port, username, password);
			tran.sendMessage(msg, msg.getAllRecipients()); // Send
			System.out.println("Transport Send msg"+tran);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	private static String getMailList(String[] mailArray) {
		StringBuffer toList = new StringBuffer();
		int length = mailArray.length;
		if (mailArray != null && length < 2) {
			toList.append(mailArray[0]);
		} else {
			for (int i = 0; i < length; i++) {
				toList.append(mailArray[i]);
				if (i != (length - 1)) {
					toList.append(",");
				}
			}
		}
		return toList.toString();
	}

}
