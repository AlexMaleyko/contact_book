package com.itechart.maleiko.contact_book.web.command;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.Attachment;
import com.itechart.maleiko.contact_book.business.service.ContactController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetAttachment implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(GetAttachment.class);
    private ContactController contactController;

    GetAttachment(ContactController contactController) {
        this.contactController = contactController;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String attachmentId = request.getParameter("attachmentId");
        Attachment attachment;
        if(StringUtils.isBlank(attachmentId)){ return; }
        try {
            attachment = contactController.getAttachmentById(Long.parseLong(attachmentId));
        } catch (DAOException e) {
            LOGGER.error("{}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }catch(Exception e){
            LOGGER.error("Unable to parse attachmentId request parameter {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to parse attachmentId request parameter");
            return;
        }
        String contentType = request.getServletContext().getMimeType(attachment.getFileName());
        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Length", attachment.getLength());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"");
        response.getOutputStream().write(attachment.getBytes());
    }
}
