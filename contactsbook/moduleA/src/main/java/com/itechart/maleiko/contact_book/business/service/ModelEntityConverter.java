package com.itechart.maleiko.contact_book.business.service;

import com.itechart.maleiko.contact_book.business.entity.Attachment;
import com.itechart.maleiko.contact_book.business.entity.Contact;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;
import com.itechart.maleiko.contact_book.business.model.AttachmentDTO;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.model.PhoneNumberDTO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

class ModelEntityConverter {

    private List<PhoneNumber> convertModelToEntityPhoneNumber(List<PhoneNumberDTO> models){
        List<PhoneNumber> entities = new ArrayList<>();
        for(PhoneNumberDTO model : models) {
            PhoneNumber entity = new PhoneNumber();
            entity.setNumberId(model.getNumberId());
            entity.setCountryCode(model.getCountryCode());
            entity.setOperatorCode(model.getOperatorCode());
            entity.setNumber(model.getNumber());
            entity.setType(model.getType());
            entity.setComment(model.getComment());
            entities.add(entity);
        }
        return entities;
    }

    private List<Attachment> convertModelToEntityAttachment(List<AttachmentDTO> models){
        List<Attachment> entities = new ArrayList<>();
        for(AttachmentDTO model : models) {
            Attachment entity = new Attachment();
            entity.setAttachmentId(model.getAttachmentId());
            entity.setFilePath(model.getFilePath());
            entity.setFileName(model.getFileName());
            entity.setUploadDate(model.getUploadDate());
            entity.setComment(model.getComment());
            entity.setFile(model.getFile());
            entities.add(entity);
        }
        return entities;
    }

    Contact convertModelToEntity(ContactDTO model){
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
        entity.setProfilePicturePath(model.getProfilePicturePath());
        entity.setProfileImage(model.getProfileImage());
        entity.setAttachments(convertModelToEntityAttachment(model.getAttachmentDTOList()));
        entity.setPhoneNumbers(convertModelToEntityPhoneNumber(model.getNumberDTOList()));
        return entity;
    }
}
