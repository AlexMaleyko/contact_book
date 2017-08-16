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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String pathInfo = request.getPathInfo();

        if (pathInfo != null &&
                (pathInfo.contains("/resources/") || pathInfo.contains("/pictures/") || pathInfo.endsWith(".jsp"))) {
            if(pathInfo.endsWith(".jsp")){
               pathInfo =  resolveView(pathInfo);
            }
            request.getRequestDispatcher(pathInfo).forward(request, response);
        } else {
           // String commandName = commandMapper.get(request.getPathInfo());
            CommandFactory commandFactory = new CommandFactory();
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
