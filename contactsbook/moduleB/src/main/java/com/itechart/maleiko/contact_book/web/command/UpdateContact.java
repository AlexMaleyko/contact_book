package com.itechart.maleiko.contact_book.web.command;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.service.ContactController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UpdateContact implements Command {
    private ContactController controller;
    private final Logger LOGGER = LoggerFactory.getLogger(UpdateContact.class);

    public UpdateContact(){
        controller = new ContactController();
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String[] checkedContacts = request.getParameterValues("checkBoxGroup");
        long id = Long.parseLong(checkedContacts[0]);
        ContactDTO contactDTO;
        try {
            contactDTO = controller.getContactDTOById(id);
        } catch (DAOException e) {
            LOGGER.error("{}", e.getMessage());
            response.sendError(500);
            return;
        }
        request.setAttribute("contact",contactDTO);
        request.setAttribute("goal", "Просмотр/Редактирование");
        request.setAttribute("formaction", "EditContact");
        if(contactDTO.getBirth() != null) {
            request.setAttribute("dd", contactDTO.getBirth().getDayOfMonth());
            request.setAttribute("mm", contactDTO.getBirth().getMonthOfYear());
            request.setAttribute("yyyy", contactDTO.getBirth().getYear());
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("contactForm.jsp");
        dispatcher.forward(request, response);
    }
}
