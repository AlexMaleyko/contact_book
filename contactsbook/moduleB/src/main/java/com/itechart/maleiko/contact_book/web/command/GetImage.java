package com.itechart.maleiko.contact_book.web.command;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.Image;
import com.itechart.maleiko.contact_book.business.service.ContactController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;

public class GetImage implements Command{
    private static final Logger LOGGER = LoggerFactory.getLogger(GetImage.class);
    private ContactController contactController;

    public GetImage(ContactController contactController) {
        this.contactController = contactController;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contactId = request.getParameter("contactId");
        Image image;
        if(StringUtils.isBlank(contactId)){ return; }
        try {
           image = contactController.getProfileImageByContactId(Long.parseLong(contactId));
        } catch (DAOException e) {
            LOGGER.error("{}", e.getMessage());
            response.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }catch(Exception e){
            LOGGER.error("Unable to parse contactId request parameter: {}", e.getMessage());
            return;
        }
        String contentType = request.getServletContext().getMimeType(image.getName());
        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Length", image.getLength());
        //response.setHeader("Content-Disposition", "attachment; filename=\"" + image.getName() + "\"");
        response.getOutputStream().write(image.getByteRepresentation());
    }
}
