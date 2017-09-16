package com.itechart.maleiko.contact_book.web.command;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.model.AttachmentDTO;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.model.PhoneNumberDTO;
import com.itechart.maleiko.contact_book.business.service.exceptions.ServiceException;
import com.itechart.maleiko.contact_book.business.utils.PropertiesLoader;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import com.itechart.maleiko.contact_book.business.service.ContactController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class EditContact implements Command {
    private ContactController controller;

    public EditContact(){
        controller = new ContactController();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(EditContact.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            LOGGER.error("Form doesn't have enctype = multipart/form-data.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Error: Form must has enctype = multipart/form-data.");
            return;
        }
        Properties properties;
        try{
            properties = PropertiesLoader.load("fileStorage.properties");
        }catch (DAOException e){
            LOGGER.error("{}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(Long.parseLong(properties.getProperty("maxFileSize")));
        upload.setSizeMax(Long.parseLong(properties.getProperty("maxUploadSize")));

        try {
            List<String> countryCodes = new ArrayList<>();
            List<String> operatorCodes = new ArrayList<>();
            List<String> numbers = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<String> phoneIds = new ArrayList<>();
            List<String> phoneComments = new ArrayList<>();
            List<String> attachComments = new ArrayList<>();
            List<String> attachNames = new ArrayList<>();
            List<String> attachIds = new ArrayList<>();
            List<FileItem> files = new ArrayList<>();
            List<Long> phoneNumberDeleteList = new ArrayList<>();
            List<Long> attachDeleteList = new ArrayList<>();
            FileItem profileImage = null;

            ContactDTO contactDTO = new ContactDTO();
            String dd = null;
            String mm = null;
            String yyyy = null; //for contactDTO birth field
            List<PhoneNumberDTO> numberDTOList = new ArrayList<>();
            List<AttachmentDTO> attachmentDTOList = new ArrayList<>();

            List<FileItem> formItems = upload.parseRequest(request);
            if(formItems != null && formItems.size() > 0){
                for(FileItem item : formItems){
                    if(!item.isFormField() && item.getSize() != 0){
                        if(item.getFieldName().equals("profileImg")){
                            profileImage = item;
                        }
                        else{
                            files.add(item);
                        }
                    }
                    else{

                        if(item.getFieldName().equals("fileName")){
                            attachNames.add(item.getString("UTF-8"));
                        }
                        if(item.getFieldName().equals("attachComment")){
                            attachComments.add(item.getString("UTF-8"));
                        }
                        if(item.getFieldName().equals("checkBoxGroup1")){
                            attachIds.add(item.getString());
                        }
                        if(item.getFieldName().equals("countryCode")){
                            countryCodes.add(item.getString("UTF-8"));
                        }
                        if(item.getFieldName().equals("operatorCode")){
                            operatorCodes.add(item.getString("UTF-8"));
                        }
                        if(item.getFieldName().equals("number")){
                            numbers.add(item.getString("UTF-8"));
                        }
                        if(item.getFieldName().equals("type")){
                            types.add(item.getString("UTF-8"));
                        }
                        if(item.getFieldName().equals("checkBoxGroup")){
                            phoneIds.add(item.getString("UTF-8"));
                        }
                        if(item.getFieldName().equals("phoneComment")){
                            phoneComments.add(item.getString("UTF-8"));
                        }
                        //Fill delete lists
                        if(item.getFieldName().equals("deleteAttach") && Long.parseLong(item.getString()) != 0){
                            attachDeleteList.add(Long.parseLong(item.getString()));
                        }
                        if(item.getFieldName().equals("deletePhone") && Long.parseLong(item.getString()) != 0){
                            phoneNumberDeleteList.add(Long.parseLong(item.getString()));
                        }
                        //Fill contactDTO fields
                        if(item.getFieldName().equals("contactId")){
                            if(!StringUtils.isNotBlank(item.getString("UTF-8"))){
                                LOGGER.error("Unable to update contact info. contactId is missing");
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                                return;
                            }
                            contactDTO.setContactId(Long.parseLong(item.getString("UTF-8")));
                        }

                        if(item.getFieldName().equals("fname")){
                            contactDTO.setName(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("lname")){
                            contactDTO.setSurname(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("patronymic")){
                            contactDTO.setPatronymic(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("day")){
                            dd = item.getString("UTF-8").trim();
                        }
                        if(item.getFieldName().equals("month")){
                            mm = item.getString("UTF-8").trim();
                        }
                        if(item.getFieldName().equals("year")){
                            yyyy = item.getString("UTF-8").trim();
                        }
                        if(item.getFieldName().equals("gender")){
                            contactDTO.setGender(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("citizenship")){
                            contactDTO.setCitizenship(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("status")){
                            contactDTO.setMaritalStatus(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("email")){
                            contactDTO.setEmail(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("web")){
                            contactDTO.setWebsite(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("job")){
                            contactDTO.setJob(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("country")){
                            contactDTO.setCountry(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("city")){
                            contactDTO.setCity(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("street")){
                            contactDTO.setStreet(item.getString("UTF-8").trim());
                        }
                        if(item.getFieldName().equals("index")){
                            contactDTO.setPostalCode(item.getString("UTF-8").trim());
                        }
                    }
                }
            }

            LocalDate birth = null;
            if( StringUtils.isNotBlank(dd) && StringUtils.isNotBlank(mm) && StringUtils.isNotBlank(yyyy)){
                birth = new LocalDate(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd));
                contactDTO.setBirth(birth);
            }
            else if(StringUtils.isNotBlank(dd) || StringUtils.isNotBlank(mm) || StringUtils.isNotBlank(yyyy)){
                LOGGER.error("Not all date fields were filled");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if(profileImage != null) {
                if (profileImage.getSize() > Long.parseLong(properties.getProperty("maxImageSize"))) {
                    LOGGER.error("Image size exceeded");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                contactDTO.setProfileImage(profileImage);
            }

            //Fill phoneNumberDTOList
                for (int i = 0; i < phoneIds.size(); i++) {
                    PhoneNumberDTO number = new PhoneNumberDTO();
                    //delete all whitespaces
                    String countryCode = countryCodes.get(i + 1).replaceAll("\\s+", "");
                    String operatorCode = operatorCodes.get(i + 1).replaceAll("\\s+", "");
                    String phoneNumber = numbers.get(i + 1).replaceAll("\\s+", "");
                    String type = types.get(i + 1).replaceAll("\\s+", "");
                    String phoneComment = phoneComments.get(i + 1).trim();
                    number.setNumberId(Long.parseLong(phoneIds.get(i)));
                    if (countryCode.length() > 10) {
                        LOGGER.error("Country code exceeds maximum length. Saving aborted");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    } else {
                        number.setCountryCode(countryCode);
                    }
                    if (operatorCode.length() > 10) {
                        LOGGER.error("Operator code exceeds maximum length. Saving aborted");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    } else {
                        number.setOperatorCode(operatorCode);
                    }
                    //check if number is empty string or contains not only digits or longer than 45 digits
                    if (phoneNumber.equals("") || !phoneNumber.matches("[0-9]+") || phoneNumber.length() > 45) {
                        LOGGER.error("Invalid phone number. Saving aborted.");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    } else {
                        number.setNumber(phoneNumber);
                    }
                    if (type.length() > 1) {
                        LOGGER.error("Number type exceeds maximum length. Saving aborted");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    } else {
                        number.setType(type);
                    }
                    if (phoneComment.length() > 200) {
                        LOGGER.error("Phone comment size exceeds maximum length. Saving aborted");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    } else {
                        number.setComment(phoneComment);
                    }
                    numberDTOList.add(number);
                }

            //Fill attachmentDTOList
            Iterator<FileItem> iterator = files.iterator();
            for(int i = 0; i< attachIds.size(); i++){
                AttachmentDTO attachmentDTO = new AttachmentDTO();
                String attachName = attachNames.get(i+1).trim();
                String attachComment = attachComments.get(i+1).trim();
                attachmentDTO.setAttachmentId(Long.parseLong(attachIds.get(i)));
                if(attachName.length() > 100){
                    LOGGER.error("File name size exceeds maximum length. Saving aborted");
                    response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
                    return;
                }
                else{
                    attachmentDTO.setFileName(attachName);
                }
                if(attachComment.length() > 200){
                    LOGGER.error("File comment size exceeds maximum length. Saving aborted");
                    response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
                    return;
                }
                else{
                    attachmentDTO.setComment(attachComment);
                }
                if(Long.parseLong(attachIds.get(i)) == 0) {
                    attachmentDTO.setFile(iterator.next());
                }
                attachmentDTOList.add(attachmentDTO);
            }

            contactDTO.setNumberDTOList(numberDTOList);
            contactDTO.setPhoneNumberDeleteList(phoneNumberDeleteList);
            contactDTO.setAttachmentDTOList(attachmentDTOList);
            contactDTO.setAttachDeleteList(attachDeleteList);
            controller.updateContact(contactDTO);
        }catch (FileUploadException | DAOException | ServiceException e) {
            LOGGER.error("{}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        response.sendRedirect("contacts");
    }
}
