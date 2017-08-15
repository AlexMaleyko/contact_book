package com.itechart.maleiko.contact_book.business.service;


import com.itechart.maleiko.contact_book.business.dao.*;
import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.Attachment;
import com.itechart.maleiko.contact_book.business.entity.Contact;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;
import com.itechart.maleiko.contact_book.business.model.AttachmentDTO;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.model.PhoneNumberDTO;
import com.itechart.maleiko.contact_book.business.utils.ConnectionController;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class EntityModelConverter {
    private AttachmentDAO attachmentDAO;
    private PhoneNumberDAO phoneNumberDAO;
    private ConnectionController connectionController;

    public EntityModelConverter(){
        DAOFactoryProducer factoryProducer = DAOFactoryProducer.getInstance();
        DAOFactory daoFactory = factoryProducer.createDAOFactory();
        this.attachmentDAO = daoFactory.createAttachmentDAO();
        this.phoneNumberDAO = daoFactory.createPhoneNumberDAO();
        this.connectionController = ConnectionController.getInstance();
    }

    private static final org.slf4j.Logger LOGGER =
            org.slf4j.LoggerFactory.getLogger(EntityModelConverter.class);

    public PhoneNumberDTO convertEntityToModel(PhoneNumber entity){

        LOGGER.info("method: convertEntityToModel({})", entity.getClass().getSimpleName());

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

    public ContactDTO convertEntityToModel(Contact entity) throws DAOException{

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
        model.setProfilePicturePath(entity.getProfilePicturePath());
        /*Setting phoneNumberDTOList*/
        List<PhoneNumberDTO> numberDTOList=new ArrayList<>();
        List<PhoneNumber> numberEntityList;

        Connection connection = null;
        try {
            connection = connectionController.provideConnection();
            phoneNumberDAO.setConnection(connection);
            attachmentDAO.setConnection(connection);
            numberEntityList= phoneNumberDAO.findByContactId(entity.getContactId());
            for(PhoneNumber number:numberEntityList) {
                numberDTOList.add(convertEntityToModel(number));
            }
            model.setNumberDTOList(numberDTOList);
        /*Setting attachmentDTOList*/
            List<AttachmentDTO> attachmentDTOList=new ArrayList<>();
            List<Attachment> attachmentEntityList;
            attachmentEntityList=attachmentDAO.findByContactId(entity.getContactId());
            for(Attachment attachment: attachmentEntityList) {
                attachmentDTOList.add(convertEntityToModel(attachment));
            }
            model.setAttachmentDTOList(attachmentDTOList);
        }finally {
            connectionController.closeConnection(connection);
        }
        return model;
    }
}
