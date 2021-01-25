/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sparkapp.ShotAvailabilityChecker;
import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*;
/**
 *
 * @author rowta
 */
public class Mailer {

    public static void send(String from, String password, String to, String sub, String msg) {
        //Get properties object    
        Properties props = new Properties();
        props.put("mail.smtp.host", Configuration.getInstance().getSmtphost());
        props.put("mail.smtp.socketFactory.port", Configuration.getInstance().getSmtpport());
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", Configuration.getInstance().getSmtpauth().toLowerCase());
        props.put("mail.smtp.port", Configuration.getInstance().getSmtpport());
        //get Session   
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        //compose message    
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(sub);
            message.setText(msg);
            //send message  
            Transport.send(message);
            System.out.println("Message Sent Successfully to :"+to);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
