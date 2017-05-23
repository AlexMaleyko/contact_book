package com.itechart.maleiko.contact_book.business.command;

import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.service.ContactController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * Created by Alexey on 02.04.2017.
 */
public class GetContact implements Command {
    private ContactController controller;
    private Connection conn;

    public GetContact(Connection conn){
        this.conn=conn;
        controller = new ContactController();
    }

    private static final org.slf4j.Logger LOGGER =
            org.slf4j.LoggerFactory.getLogger(DeleteContact.class);

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("execute");

        long contactId=Long.parseLong(request.getParameter("contactId"));
        ContactDTO contactDTO = controller.getContactDTOById(conn, contactId);

        request.setAttribute("contact",contactDTO);
        request.setAttribute("goal", "Просмотр/Редактирование");
        request.setAttribute("formaction", "EditContact");
        if(contactDTO.getBirth() != null) {
            request.setAttribute("dd", contactDTO.getBirth().getDayOfMonth());
            request.setAttribute("mm", contactDTO.getBirth().getMonthOfYear());
            request.setAttribute("yyyy", contactDTO.getBirth().getYear());
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/contactForm.jsp");
        dispatcher.forward(request, response);
    }
}
