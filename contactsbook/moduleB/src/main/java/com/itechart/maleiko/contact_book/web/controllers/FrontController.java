package com.itechart.maleiko.contact_book.web.controllers;

import com.itechart.maleiko.contact_book.web.command.Command;
import com.itechart.maleiko.contact_book.web.command.CommandFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FrontController extends HttpServlet {

    private static final org.slf4j.Logger LOGGER =
            org.slf4j.LoggerFactory.getLogger(FrontController.class);

    private Map<String, String> commandMapper;

    @Override
    public void init(ServletConfig config) {

        try {
            super.init(config);
            commandMapper = new HashMap<>();
            commandMapper.put("/SendEmail", "SendEmail");
            commandMapper.put("/EditContact", "EditContact");
            commandMapper.put("/SaveContact", "SaveContact");
            commandMapper.put("/Search", "SearchContacts");
            commandMapper.put("/CreateContactForm", "CreateContact");
            commandMapper.put("/ContactForm", "GetContact");
            commandMapper.put("/DeleteContact", "DeleteContact");
            commandMapper.put("/ContactList", "GetContactList");
            commandMapper.put("/UpdateContact", "UpdateContact");
            commandMapper.put("/image", "GetImage");
            commandMapper.put("/attachment", "GetAttachment");

        } catch (ServletException e) {
            LOGGER.error("{}", e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("method doPost");
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("method doGet");
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String pathInfo = request.getPathInfo();
        LOGGER.info("{}", pathInfo);

        if (pathInfo != null &&
                (pathInfo.contains("/resources/") || pathInfo.contains("/pictures/") || pathInfo.endsWith(".jsp"))) {
            if(pathInfo.endsWith(".jsp")){
               pathInfo =  resolveView(pathInfo);
            }
            request.getRequestDispatcher(pathInfo).forward(request, response);
        } else {
           // String commandName = commandMapper.get(request.getPathInfo());
            CommandFactory commandFactory = new CommandFactory();
            LOGGER.info("pathInfo: {}", request.getPathInfo());
            Command command = commandFactory.getCommand(request.getPathInfo());
            command.execute(request, response);
        }
    }

    private String resolveView(String pathInfo){
        String[] splitedPathInfo = pathInfo.split("/");
        pathInfo = "/WEB-INF/views/" + splitedPathInfo[splitedPathInfo.length - 1];
        return pathInfo;
    }
}
