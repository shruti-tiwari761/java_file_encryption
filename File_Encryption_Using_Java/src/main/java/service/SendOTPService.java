package service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendOTPService {

    // Method to send OTP to a recipient's email
    public static void sendOTP(String recipientEmail, String genOTP) {
        // Validate recipient email
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient email cannot be null or empty.");
        }

        // Sender's email ID (must be valid)
        String from = "shrutitiwari4618@gmail.com";  // Replace with a real sender email
        String password = "qojk fhdt dogd clpy";  // Use an app-specific password for Gmail

        // SMTP server configuration
        String host = "smtp.gmail.com";

        // System properties for the mail session
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");  // SMTP SSL port
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Create a mail session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        // Enable debug mode for SMTP troubleshooting
        session.setDebug(true);

        try {
            // Create a MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set From field
            message.setFrom(new InternetAddress(from));

            // Set To field
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            // Set email subject
            message.setSubject("Your OTP for File Enc App");

            // Set email content
            message.setText("Your One-Time Password for the File Enc app is: " + genOTP);

            System.out.println("Sending OTP email...");

            // Send the message
            Transport.send(message);
            System.out.println("OTP email sent successfully!");

        } catch (MessagingException mex) {
            mex.printStackTrace();  // Log the exception for debugging
        }
    }
}
