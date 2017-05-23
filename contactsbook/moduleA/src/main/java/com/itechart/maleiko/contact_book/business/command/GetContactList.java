package com.itechart.maleiko.contact_book.business.command;

import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import org.apache.commons.lang3.StringUtils;
import com.itechart.maleiko.contact_book.business.service.ContactController;
import com.itechart.maleiko.contact_book.business.service.TemplateMessage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;


public class GetContactList implements Command {
    private ContactController controller;
    private Connection conn;

    public GetContactList(Connection conn){
        this.conn=conn;
        controller = new ContactController();
    }

    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(SaveContact.class);
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("method: execute");
        controller= new ContactController();
        int currentPage = 0;
        int clickedPage = 0;
        int skipTotal = 0;
        int clientLimit = 10;
        if(StringUtils.isNotBlank(request.getParameter("currentPage"))) {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        }
        if(StringUtils.isNotBlank(request.getParameter("clickedPage"))) {
            clickedPage = Integer.parseInt(request.getParameter("clickedPage"));
        }
        if(StringUtils.isNotBlank(request.getParameter("skipTotal"))) {
            skipTotal = Integer.parseInt(request.getParameter("skipTotal"));
        }
        if(StringUtils.isNotBlank(request.getParameter("clientLimit"))){
            clientLimit = Integer.parseInt(request.getParameter("clientLimit"));
        }
        if(currentPage != clickedPage){
            skipTotal += clientLimit * (clickedPage - currentPage);
            if(skipTotal < 0){
                skipTotal = 0;
            }
        }

        int numberOfContacts = controller.getNumberOfContacts(conn);
        int pageTotal = (numberOfContacts-skipTotal)/clientLimit + skipTotal/clientLimit;
        if(numberOfContacts%clientLimit > 0){
            pageTotal+=1;
        }
        if(skipTotal%clientLimit > 0){
            pageTotal+=1;
        }

        request.setAttribute("skipTotal", skipTotal);
        request.setAttribute("currentPage", clickedPage);
        request.setAttribute("clientLimit", clientLimit);
        request.setAttribute("pageTotal", pageTotal);
        request.setAttribute("msgTmpl", TemplateMessage.getAllTemplates());
        List<ContactDTO> contacts =controller.getAllContactDTO(conn, skipTotal, clientLimit);
        request.setAttribute("paginationFormAction", "ContactList");
        request.setAttribute("contacts",contacts);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}
