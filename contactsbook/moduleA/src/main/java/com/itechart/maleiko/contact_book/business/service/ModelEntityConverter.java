package com.itechart.maleiko.contact_book.business.service;

import com.itechart.maleiko.contact_book.business.entity.Attachment;
import com.itechart.maleiko.contact_book.business.entity.Contact;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;
import com.itechart.maleiko.contact_book.business.model.AttachmentDTO;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.model.PhoneNumberDTO;

import java.sql.Date;

/**
 * Created by Alexey on 21.03.2017.
 */
public class ModelEntityConverter {

    private static final org.slf4j.Logger LOGGER =
            org.slf4j.LoggerFactory.getLogger(ModelEntityConverter.class);

    public PhoneNumber convertModelToEntity(PhoneNumberDTO model,long contactId){

        LOGGER.info("method: convertModelToEntity({}, {})", model.getClass().getSimpleName(), contactId);

        PhoneNumber entity=new PhoneNumber();
        entity.setNumberId(model.getNumberId());
        entity.setCountryCode(model.getCountryCode());
        entity.setOperatorCode(model.getOperatorCode());
        entity.setNumber(model.getNumber());
        entity.setType(model.getType());
        entity.setComment(model.getComment());
        entity.setContactId(contactId);
        return entity;
    }

    public Attachment convertModelToEntity(AttachmentDTO model,long contactId){

        LOGGER.info("method: convertModelToEntity({}, {})",  model.getClass().getSimpleName(), contactId);

        Attachment entity=new Attachment();
        entity.setAttachmentId(model.getAttachmentId());
        entity.setFilePath(model.getFilePath());
        entity.setFileName(model.getFileName());
        entity.setUploadDate(model.getUploadDate());
        entity.setComment(model.getComment());
        entity.setFile(model.getFile());
        entity.setContactId(contactId);
        return entity;
    }

    public Contact convertModelToEntity(ContactDTO model){

        LOGGER.info("method: convertModelToEntity({})", model.getClass().getSimpleName());

        Contact entity = new Contact(model.getName(),model.getSurname());
        entity.setContactId(model.getContactId());
        entity.setPatronymic(model.getPatronymic());
        if(model.getBirth() != null){
            entity.setBirth(new Date(model.getBirth().toDateTimeAtStartOfDay().getMillis()));
        }
        else{
            entity.setBirth(null);
        }
        entity.setGender(model.getGender());
        entity.setCitizenship(model.getCitizenship());
        entity.setMaritalStatus(model.getMaritalStatus());
        entity.setWebsite(model.getWebsite());
        entity.setEmail(model.getEmail());
        entity.setJob(model.getJob());
        entity.setCountry(model.getCountry());
        entity.setCity(model.getCity());
        entity.setStreet(model.getStreet());
        entity.setPostalCode(model.getPostalCode());
        entity.setProfilePictureName(model.getProfilePictureName());
        entity.setProfileImage(model.getProfileImage());
        return entity;
    }
}
