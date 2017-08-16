package com.itechart.maleiko.contact_book.business.service;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.service.exceptions.ServiceException;
import com.itechart.maleiko.contact_book.business.utils.PropertiesLoader;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class EmailSender {
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(ContactController.class);

    public void sendEmail(List<Long> recipientIds, String subject, String text) throws DAOException, ServiceException{
        ContactController controller = new ContactController();
        Properties mailBoxProperties = PropertiesLoader.load("email.properties");

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.port", "465");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailBoxProperties.getProperty("username"),
                                mailBoxProperties.getProperty("password"));
                    }
                });
        List<ContactDTO> contactDTOList = controller.getContactDTOsByIdList(recipientIds);
        if(contactDTOList.isEmpty()){
            LOGGER.error("recipient list is empty");
            throw new ServiceException("Recipient List is Empty");
        }

        try {
            MimeMessage message;
            for(ContactDTO contactDTO : contactDTOList) {
                if(StringUtils.isBlank(contactDTO.getEmail())){
                    LOGGER.error("Contact (id = {}) email field is empty", contactDTO.getContactId());
                }
                message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailBoxProperties.getProperty("username")));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(contactDTO.getEmail()));
                message.setSubject(subject, "UTF-8");
                message.setText(text, "UTF-8");
                Transport.send(message);
            }
        }catch (MessagingException e) {
            LOGGER.error("{}", e.getMessage());
        }
    }

}
