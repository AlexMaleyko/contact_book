package com.itechart.maleiko.contact_book.business.service;


import com.itechart.maleiko.contact_book.business.DAO.AttachmentDAOImpl;
import com.itechart.maleiko.contact_book.business.DAO.PhoneNumberDAOImpl;
import com.itechart.maleiko.contact_book.business.entity.Attachment;
import com.itechart.maleiko.contact_book.business.entity.Contact;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;
import com.itechart.maleiko.contact_book.business.model.AttachmentDTO;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.model.PhoneNumberDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 21.03.2017.
 */
public class EntityModelConverter {

    private static final org.slf4j.Logger LOGGER =
            org.slf4j.LoggerFactory.getLogger(EntityModelConverter.class);

    public PhoneNumberDTO convertEntityToModel(PhoneNumber entity){

        LOGGER.info("method: convertEntityToModel({})",entity.getClass().getSimpleName());

        PhoneNumberDTO model=new PhoneNumberDTO();
        model.setNumberId(entity.getNumberId());
        model.setCountryCode(entity.getCountryCode());
        model.setOperatorCode(entity.getOperatorCode());
        model.setNumber(entity.getNumber());
        model.setType(entity.getType());
        model.setComment(entity.getComment());
        return model;
    }

    public AttachmentDTO convertEntityToModel(Attachment entity){

        LOGGER.info("method: convertEntityToModel({})",entity.getClass().getSimpleName());

        AttachmentDTO model=new AttachmentDTO();
        model.setAttachmentId(entity.getAttachmentId());
        model.setFilePath(entity.getFilePath());
        model.setFileName(entity.getFileName());
        model.setUploadDate(entity.getUploadDate());
        model.setComment(entity.getComment());
        return model;
    }

    public ContactDTO convertEntityToModel(Connection conn, Contact entity){

        LOGGER.info("method: convertEntityToModel({})", entity.getClass().getSimpleName());

        ContactDTO model=new ContactDTO();
        model.setContactId(entity.getContactId());
        model.setName(entity.getName());
        model.setSurname(entity.getSurname());
        model.setPatronymic(entity.getPatronymic());
        if(entity.getBirth() != null){
            model.setBirth(new org.joda.time.LocalDate(entity.getBirth().getTime()));
        }
        else{
            model.setBirth(null);
        }
        model.setGender(entity.getGender());
        model.setCitizenship(entity.getCitizenship());
        model.setMaritalStatus(entity.getMaritalStatus());
        model.setWebsite(entity.getWebsite());
        model.setEmail(entity.getEmail());
        model.setJob(entity.getJob());
        model.setCountry(entity.getCountry());
        model.setCity(entity.getCity());
        model.setStreet(entity.getStreet());
        model.setPostalCode(entity.getPostalCode());
        model.setProfilePictureName(entity.getProfilePictureName());
        /*Setting phoneNumberDTOList*/
        List<PhoneNumberDTO> numberDTOList=new ArrayList<>();
        List<PhoneNumber> numberEntityList=null;

        try {
            numberEntityList= (new PhoneNumberDAOImpl()).findByContactId(
                    conn,entity.getContactId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(PhoneNumber number:numberEntityList) {
            numberDTOList.add(convertEntityToModel(number));
        }
        model.setNumberDTOList(numberDTOList);
        /*Setting attachmentDTOList*/
        List<AttachmentDTO> attachmentDTOList=new ArrayList<>();
        List<Attachment> attachmentEntityList=null;
        try {
            attachmentEntityList=(new AttachmentDAOImpl()).findByContactId(
                    conn,entity.getContactId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Attachment attachment: attachmentEntityList) {
            attachmentDTOList.add(convertEntityToModel(attachment));
        }
        model.setAttachmentDTOList(attachmentDTOList);
        return model;
    }
}
