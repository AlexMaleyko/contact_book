package com.itechart.maleiko.contact_book.business.service;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.service.exceptions.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;


public class BirthdayMailJob implements Job {
    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Job started");
        try {
            ContactController controller = new ContactController();
            List<ContactDTO> contactDTOList = controller.getAllContactDTO(0, controller.getNumberOfContacts());
            if (contactDTOList.isEmpty()) {
                LOGGER.info("No records in datasource. Job finished.");
                return;
            }
            List<ContactDTO> hasBirthday = new ArrayList<>();
            for (ContactDTO contactDTO : contactDTOList) {
                LocalDate birth = contactDTO.getBirth();
                LocalDate now = LocalDate.now();
                if (birth != null) {
                    if (birth.getDayOfMonth() == now.getDayOfMonth() && birth.getMonthOfYear() == now.getMonthOfYear()) {
                        hasBirthday.add(contactDTO);
                    }
                }
            }
            if (hasBirthday.isEmpty()) {
                LOGGER.info("No person has birthday today");
                return;
            }
            EmailSender sender = new EmailSender();
            List<Long> list = new ArrayList<>();
            STGroup messageTemplates = new STGroupFile("messageTemplates.stg");
            for (ContactDTO contactDTO : hasBirthday) {
                ST template1 = messageTemplates.getInstanceOf("birthday");
                list.add(contactDTO.getContactId());
                template1.add("name", contactDTO.getName());
                if (StringUtils.isNotBlank(contactDTO.getPatronymic())) {
                    template1.add("patronymic", contactDTO.getPatronymic());
                } else {
                    template1.add("patronymic", "");
                }
                sender.sendEmail(list, "С праздником!", template1.render());
                list.clear();
            }
            LOGGER.info("email sent to {} people", hasBirthday.size());
        }catch (DAOException e){
            throw new JobExecutionException(e.getMessage(), e);
        }
        finally {
            //ToDo perform resource logic
            System.out.println("finally");
        }
    }
}
