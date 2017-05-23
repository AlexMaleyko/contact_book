package com.itechart.maleiko.contact_book.business.service;

import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * Created by Alexey on 10.04.2017.
 */
public class EmailSender {
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(ContactController.class);

    public void sendEmail(Connection conn, List<Long> recipientIds, String subject, String text){
        ContactController controller = new ContactController();
        Properties properties = new Properties();
        String propFileName = "email.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("{}", e.getMessage());
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.getProperty("username"),
                                properties.getProperty("password"));
                    }
                });
        List<ContactDTO> contactDTOList = controller.getContactDTOsByIdList(conn, recipientIds);
        if(contactDTOList.isEmpty()){
            LOGGER.error("recipient list is empty");
            throw new RuntimeException("You didn't choose any recipient");
        }

        try {
            MimeMessage message = null;
            for(ContactDTO contactDTO : contactDTOList) {
                if(!StringUtils.isNotBlank(contactDTO.getEmail())){
                    LOGGER.error("email field is empty");
                    throw new RuntimeException("Email field for the recipient " + contactDTO.getName() +
                            " " +  contactDTO.getSurname() + " " + contactDTO.getPatronymic() +
                    " hasn't been provided");
                }
                message = new MimeMessage(session);
                message.setFrom(new InternetAddress(properties.getProperty("username")));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(contactDTO.getEmail()));
                message.setSubject(subject, "UTF-8");
                message.setText(text, "UTF-8");
                Transport.send(message);
            }
        } catch (AddressException e) {
            LOGGER.error("{}", e.getMessage());
        } catch (MessagingException e) {
            LOGGER.error("{}", e.getMessage());
        }
    }

}
