package com.itechart.maleiko.contact_book.web.command;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.service.exceptions.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import com.itechart.maleiko.contact_book.business.service.ContactController;
import com.itechart.maleiko.contact_book.business.service.EmailSender;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SendEmail implements Command{
    private EmailSender sender;

    SendEmail(){
        sender = new EmailSender();
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmail.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] recipients = request.getParameterValues("recipient");
            String template = request.getParameter("pattern");
            if (recipients == null) {
                LOGGER.error("Recipients array is null");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            List<Long> recipientIds = new ArrayList<>();
            for (int i = 0; i < recipients.length; i++) {
                recipientIds.add(Long.parseLong(recipients[i]));
            }
            if (!StringUtils.isNotBlank(template)) {
                String text = request.getParameter("mailText").trim();
                String subject = request.getParameter("subject").trim();
                sender.sendEmail(recipientIds, subject, text);
            } else {
                String subject = request.getParameter("subject").trim();
                List<Long> list = new ArrayList<>();
                List<ContactDTO> contactDTOList = (new ContactController()).getContactDTOsByIdList(recipientIds);
                STGroup messageTemplates = new STGroupFile("messageTemplates.stg");
                if (template.equals("birthday")) {
                    for (ContactDTO contactDTO : contactDTOList) {
                        list.add(contactDTO.getContactId());
                        ST stTemplate = messageTemplates.getInstanceOf(template);
                        stTemplate.add("name", contactDTO.getName());
                        if (StringUtils.isNotBlank(contactDTO.getPatronymic())) {
                            stTemplate.add("patronymic", contactDTO.getPatronymic());
                        } else {
                            stTemplate.add("patronymic", "");
                        }
                        sender.sendEmail(list, subject, stTemplate.render());
                        list.clear();
                    }
                }
                if (template.equals("birthdayPlans")) {
                    LocalDate localDate = LocalDate.now();
                    for (ContactDTO contactDTO : contactDTOList) {
                        list.add(contactDTO.getContactId());
                        ST stTemplate = messageTemplates.getInstanceOf(template);
                        if (contactDTO.getBirth() == null) {
                            LOGGER.error("For contact (id = {} birth date is not specified. Email wasn't sent",
                                    contactDTO.getContactId());
                            continue;
                        }
                        stTemplate.add("age", localDate.getYear() - contactDTO.getBirth().getYear());
                        sender.sendEmail(list, subject, stTemplate.render());
                        list.clear();
                    }
                }
                if (template.equals("meeting")) {
                    ST stTemplate = messageTemplates.getInstanceOf(template);
                    for (ContactDTO contactDTO : contactDTOList) {
                        list.add(contactDTO.getContactId());
                    }
                    sender.sendEmail(list, subject, stTemplate.render());
                }
            }
        }catch (DAOException | ServiceException e){
            LOGGER.error("{}", e.getMessage());
            response.sendError(500);
            return;
        }
        response.sendRedirect("contacts");
    }
}
