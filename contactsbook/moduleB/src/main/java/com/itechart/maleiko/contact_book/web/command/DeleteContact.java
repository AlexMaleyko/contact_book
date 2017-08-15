package com.itechart.maleiko.contact_book.web.command;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.service.ContactController;
import com.itechart.maleiko.contact_book.business.service.exceptions.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteContact  implements Command{
    private ContactController controller;

    public DeleteContact(){
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

        try {
            controller.deleteContactsByIds(ids);
        } catch (DAOException | ServiceException e){
            LOGGER.error("{}", e.getMessage());
            responce.sendError(500);
            return;
        }
        responce.sendRedirect("contacts");
    }

}
