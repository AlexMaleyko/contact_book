package com.itechart.maleiko.contact_book.business.command;

import com.itechart.maleiko.contact_book.business.DAO.PairResultSize;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import com.itechart.maleiko.contact_book.business.service.ContactController;
import com.itechart.maleiko.contact_book.business.service.TemplateMessage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

/**
 * Created by Alexey on 03.04.2017.
 */
public class SearchContacts implements Command {
    private ContactController controller;
    private Connection conn;
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(SearchContacts.class);


    public SearchContacts(Connection conn){
        this.conn=conn;
        controller = new ContactController();
    }
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("method: execute");

        List<ContactDTO> contactDTOList = null;
        PairResultSize pair = null;
        if(Integer.parseInt(request.getParameter("isSameSearch")) == 0) {

            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<String> keySet = request.getParameterMap().keySet();
            for (String s : keySet) {
            }
            List<String> filledParamNames = new ArrayList<>();

            for (String s : keySet) {
                if (StringUtils.isNotBlank(parameterMap.get(s)[0])) {
                    filledParamNames.add(s);
                }
            }
            if (keySet.isEmpty()) {
                throw new RuntimeException("You didn't fill any input field.");
            }

            LocalDate date = null;

            if (filledParamNames.contains("day") ||
                    filledParamNames.contains("month") ||
                    filledParamNames.contains("year")) {

                if (filledParamNames.contains("day") &&
                        filledParamNames.contains("month") &&
                        filledParamNames.contains("year")) {
                    date = new LocalDate(Integer.parseInt(parameterMap.get("year")[0]),
                            Integer.parseInt(parameterMap.get("month")[0]),
                            Integer.parseInt(parameterMap.get("day")[0]));
                    filledParamNames.remove("day");
                    filledParamNames.remove("month");
                    filledParamNames.remove("year");
                } else {
                    //redirect to error page
                    throw new RuntimeException("You didn't complete all date fields");
                }
            }
            Map<String, Object> fieldValue = new HashMap<>();
            for (String s : filledParamNames) {
                fieldValue.put(s, (parameterMap.get(s)[0]).trim());//mysql doesn't ignore whitespaces in beginning of the string
            }
            if (date != null) {
                fieldValue.put("birth", date);
            } else {
                fieldValue.remove("comparator");
            }
            fieldValue.remove("isSameSearch");
            pair = controller.findContactDTOs(conn, fieldValue);
            contactDTOList = pair.getContactDTOList();
            HttpSession session = request.getSession(true);
            session.setAttribute("contacts", contactDTOList);
            session.setAttribute("resultSetSize", pair.getResultSetSize());
        }

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
        HttpSession session = request.getSession(false);
        long numberOfContacts = (long)session.getAttribute("resultSetSize");
        long pageTotal = (numberOfContacts-skipTotal)/clientLimit + skipTotal/clientLimit;
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
        request.setAttribute("isSearchPage", 1);
        request.setAttribute("paginationFormAction", "Search");

        contactDTOList = (List< ContactDTO>)session.getAttribute("contacts");
        List<ContactDTO> displayedResult = new ArrayList<>();
        for(int i = skipTotal; i < skipTotal + clientLimit; i++){
            if(i == contactDTOList.size()){
                break;
            }
            displayedResult.add(contactDTOList.get(i));
        }
        request.setAttribute("contacts",displayedResult);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);

    }
}
