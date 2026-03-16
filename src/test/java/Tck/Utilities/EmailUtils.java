package Tck.Utilities;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import Tck.Base.BaseClass;

public class EmailUtils extends BaseClass {

    public static void sendEmailWithReport(String reportPath)
            throws Exception {

        final String fromEmail = Config.getProperty("senderEmail");
        final String password = Config.getProperty("senderPassword");
        final String toEmail = Config.getProperty("receiverEmail");
       // final String password = "qmnvbgukflmsxnqf";
       // final String toEmail = "lalit@dreamcast.co";

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(
                props,
                new Authenticator() {
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                fromEmail, password);
                    }
                });

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail));

        message.setSubject(
                "Automation Execution Report ✅");

        // Email Body
        BodyPart messageBodyPart =
                new MimeBodyPart();

        messageBodyPart.setText(
                "Hello Team,\n"
                + "The automation test execution has completed successfully.\n"
                + "Please find the attached Extent Report for detailed results.\n"
                + "Regards, \n" + "Automation Framework");

        Multipart multipart =
                new MimeMultipart();

        multipart.addBodyPart(messageBodyPart);

        // Attach Report
        MimeBodyPart attachmentPart =
                new MimeBodyPart();

        FileDataSource source =
                new FileDataSource(reportPath);

        attachmentPart.setDataHandler(
                new DataHandler(source));

        attachmentPart.setFileName(
                new File(reportPath).getName());

        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);

        Transport.send(message);

        System.out.println("✅ Email Sent Successfully");
    }
}