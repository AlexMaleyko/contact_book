package com.itechart.maleiko.contact_book.web.command;

import com.itechart.maleiko.contact_book.business.model.ContactDTO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CreateContact implements Command{

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ContactDTO contact = new ContactDTO();
        request.setAttribute("goal", "Создание");
        request.setAttribute("formaction", "SaveContact");
        request.setAttribute("contact", contact);
        RequestDispatcher dispatcher = request.getRequestDispatcher("contactForm.jsp");
        dispatcher.forward(request, response);
    }
}
