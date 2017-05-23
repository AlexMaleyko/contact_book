package com.itechart.maleiko.contact_book.business.command;

import com.itechart.maleiko.contact_book.business.service.ContactController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 02.04.2017.
 */
public class DeleteContact  implements Command{
    private ContactController controller;
    private Connection conn;

    public DeleteContact(Connection conn){
        this.conn=conn;
        controller = new ContactController();
    }

    private static final org.slf4j.Logger LOGGER =
            org.slf4j.LoggerFactory.getLogger(DeleteContact.class);

    public void execute(HttpServletRequest request, HttpServletResponse responce)
            throws ServletException, IOException {
        String[] checkedContacts = request.getParameterValues("checkBoxGroup");
        List<Long> ids= new ArrayList<>();

        for(String contact: checkedContacts){
            ids.add(Long.parseLong(contact));
        }

        controller.deleteContactsById(conn, ids);
        responce.sendRedirect("ContactList");
    }

}
