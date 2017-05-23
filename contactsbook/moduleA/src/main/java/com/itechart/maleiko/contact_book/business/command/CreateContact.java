package com.itechart.maleiko.contact_book.business.command;

import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.service.ContactController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Created by Alexey on 03.04.2017.
 */
public class CreateContact implements Command{
    private ContactController controller;
    private Connection conn;
    private Properties properties;
    private String propFileName = "fileStorage.properties";

    public CreateContact(Connection conn){
        this.conn=conn;
        controller = new ContactController();
    }
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(SaveContact.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("method: execute");
        properties = new Properties();
        InputStream inputStream = ContactController.class.getClassLoader().getResourceAsStream(propFileName);
        properties.load(inputStream);

        ContactDTO contact = new ContactDTO();
        contact.setProfilePictureName(properties.getProperty("defaultPicturePath"));
        request.setAttribute("goal", "Создание");
        request.setAttribute("formaction", "SaveContact");
        request.setAttribute("contact", contact);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/contactForm.jsp");
        dispatcher.forward(request, response);
    }
}
