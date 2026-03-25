package Tck.Utilities;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import Tck.Base.BaseClass;

public class EmailUtils extends BaseClass {

	public static void sendEmailWithReport(String reportPath, String logPath)
	        throws Exception {

	    final String fromEmail = Config.getProperty("senderEmail");
	    final String password = Config.getProperty("senderPassword");
	    final String toEmail = Config.getProperty("receiverEmail");

	    Properties props = new Properties();

	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");

	    Session session = Session.getInstance(
	            props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(fromEmail, password);
	                }
	            });

	    Message message = new MimeMessage(session);

	    message.setFrom(new InternetAddress(fromEmail));
	    message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(toEmail));

	    message.setSubject("Automation Execution Report ✅");

	    BodyPart messageBodyPart = new MimeBodyPart();

	    messageBodyPart.setText(
	            "Hello Team,\n"
	            + "Automation execution completed.\n"
	            + "Attached:\n"
	            + "1. Extent Report\n"
	            + "2. Log4j Execution Logs\n\n"
	            + "Regards,\nAutomation Framework");

	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(messageBodyPart);

	    // Extent Report
	    MimeBodyPart extentAttachment = new MimeBodyPart();
	    extentAttachment.setDataHandler(new DataHandler(new FileDataSource(reportPath)));
	    extentAttachment.setFileName(new File(reportPath).getName());
	    multipart.addBodyPart(extentAttachment);

	    // Log4j Report
	    MimeBodyPart logAttachment = new MimeBodyPart();
	    logAttachment.setDataHandler(new DataHandler(new FileDataSource(logPath)));
	    logAttachment.setFileName(new File(logPath).getName());
	    multipart.addBodyPart(logAttachment);

	    message.setContent(multipart);

	    Transport.send(message);

	    System.out.println("✅ Email Sent with Extent + Log4j Reports!");
	}
}