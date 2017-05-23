package com.itechart.maleiko.contact_book.web;

import com.itechart.maleiko.contact_book.business.command.Command;
import com.itechart.maleiko.contact_book.business.command.CommandFactory;
import com.itechart.maleiko.contact_book.business.service.ConnectionClass;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexey on 13.04.2017.
 */

public class FrontController extends HttpServlet {

    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(FrontController.class);

    private Connection conn;
    private Map<String, String> commandMapper;

    @Override
    public void init(ServletConfig config){

        try {
            super.init(config);
            conn = ConnectionClass.getConnection();
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

        } catch (SQLException e) {
            LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
        } catch (ServletException e) {
           LOGGER.error("{}", e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("method doPost");
        CommandFactory commandFactory = new CommandFactory();
        String commandName = commandMapper.get(request.getPathInfo());
        LOGGER.info("commandName: {} pathInfo: {}", commandName, request.getPathInfo());
        Command command = commandFactory.getCommand(conn, commandName);
        command.execute(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("method doGet");

        String pathInfo = request.getPathInfo();
        LOGGER.info("{}", pathInfo);

        if (pathInfo != null && (pathInfo.startsWith("/resources/") || pathInfo.startsWith("/pictures/"))) {
            request.getRequestDispatcher(pathInfo).forward(request, response);
        } else {
            String commandName = commandMapper.get(request.getPathInfo());
            CommandFactory commandFactory = new CommandFactory();
            LOGGER.info("commandName: {} pathInfo: {}", commandName, request.getPathInfo());
            Command command = commandFactory.getCommand(conn, commandName);
            command.execute(request, response);
        }
    }
}
