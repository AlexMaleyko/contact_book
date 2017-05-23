package com.itechart.maleiko.contact_book.business.service;

import com.itechart.maleiko.contact_book.business.DAO.*;
import com.itechart.maleiko.contact_book.business.entity.Attachment;
import com.itechart.maleiko.contact_book.business.entity.Contact;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;
import com.itechart.maleiko.contact_book.business.model.AttachmentDTO;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.model.PhoneNumberDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class ContactController {

    private static final org.slf4j.Logger LOGGER=
            org.slf4j.LoggerFactory.getLogger(ContactController.class);

    private Properties properties;
    private String propFileName = "fileStorage.properties";

    public List<ContactDTO> getAllContactDTO(Connection conn, int skip, int limit) {

        LOGGER.info("method: getAllContactDTO()");

        List<ContactDTO> contactDTOList = new ArrayList<>();
        List<Contact> contactList = null;
        try {
            contactList = (new ContactDAOImpl()).getAll(conn, skip, limit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        EntityModelConverter converter = new EntityModelConverter();
        for (Contact contact : contactList)
            contactDTOList.add(converter.convertEntityToModel(conn, contact));
        return contactDTOList;
    }

    public int getNumberOfContacts(Connection conn){
        LOGGER.info("method: getNumberOfContactPages({})", conn);
        int numberOfContacts = 0;
        try {
            numberOfContacts = (new ContactDAOImpl()).getNumberOfContacts(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfContacts;
    }

    public PairResultSize findContactDTOs(Connection conn, Map<String, Object> fieldValue) {

        LOGGER.info("method: findContactDTOs({}, {})", conn, fieldValue.getClass().getSimpleName());
        LOGGER.info("{}, {}", fieldValue.toString(), fieldValue.get("fname"));
        ContactDAO contactDAO = new ContactDAOImpl();
        PairResultSize newPair = new PairResultSize();
        PairResultSize receivedPair = null;
        List<ContactDTO> contactDTOList = new ArrayList<>();
        List<Contact> contactList = null;
        long resultSize = 0;
        try {
            receivedPair = contactDAO.findByGivenParameters(conn, fieldValue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contactList = receivedPair.getContactList();
        EntityModelConverter converter = new EntityModelConverter();
        for (Contact contact : contactList) {
            contactDTOList.add(converter.convertEntityToModel(conn, contact));
        }
        newPair.setContactDTOList(contactDTOList);
        newPair.setResultSetSize(receivedPair.getResultSetSize());
        return newPair;
    }


    public ContactDTO getContactDTOById(Connection conn, long id){

        LOGGER.info("method: getContactDTOById({}, {})", conn, id);

        ContactDAO contactDAO = new ContactDAOImpl();

        ContactDTO contactDTO = null;

        try {
            contactDTO = (new EntityModelConverter()).convertEntityToModel(conn, contactDAO.findById(conn, id));
            properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            properties.load(inputStream);

            if (StringUtils.isNotBlank(contactDTO.getProfilePictureName())) {
                Path source = Paths.get(contactDTO.getProfilePictureName());
                Path destination = Paths.get(properties.getProperty("profileImageServerFolder"));
                if (Files.exists(destination.toAbsolutePath())) {
                    FileUtils.cleanDirectory(destination.toAbsolutePath().toFile());
                }
                FileUtils.copyFileToDirectory(source.toFile(), destination.toAbsolutePath().toFile());
                FileUtils.forceDeleteOnExit(destination.toAbsolutePath().toFile());
                contactDTO.setProfilePictureName(properties.getProperty("imageRelativePath") + source.getFileName());
            } else {
                    contactDTO.setProfilePictureName(properties.getProperty("defaultPicturePath"));
            }
            List<AttachmentDTO> attachmentDTOList = contactDTO.getAttachmentDTOList();
            Path destination = Paths.get(properties.getProperty("contactFilesServerFolder"));
            if (!contactDTO.getAttachmentDTOList().isEmpty()) {
                if (Files.exists(destination.toAbsolutePath())) {
                    FileUtils.cleanDirectory(destination.toAbsolutePath().toFile());
                }
                for (AttachmentDTO attachmentDTO : attachmentDTOList) {
                    if (StringUtils.isNotBlank(attachmentDTO.getFilePath())) {
                        Path source = Paths.get(attachmentDTO.getFilePath());
                        FileUtils.copyFileToDirectory(source.toFile(), destination.toAbsolutePath().toFile());
                        FileUtils.forceDeleteOnExit(destination.toAbsolutePath().toFile());
                        attachmentDTO.setFilePath(properties.getProperty("fileRelativePath") + source.getFileName());
                    }
                }
            }
        }catch (IOException e) {
                LOGGER.error(e.getMessage());
            }catch (SQLException e) {
            LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
        }

        return contactDTO;
    }

    public List<ContactDTO> getContactDTOsByIdList(Connection conn, List<Long> ids){

        LOGGER.info("method: getContactDTOsByIdList({}, {})", conn, ids.getClass().getSimpleName());

        List<ContactDTO> contactDTOList = new ArrayList<>();
        List<Contact> contactList = new ArrayList<>();
        try {
            ContactDAOImpl contactDAO = new ContactDAOImpl();
            for(long id: ids) {
                contactList.add(contactDAO.findById(conn, id));
            }
            } catch (SQLException e) {
            LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
        }

        EntityModelConverter converter = new EntityModelConverter();

        for(Contact contact : contactList){
            contactDTOList.add(converter.convertEntityToModel(conn, contact));
        }
        return contactDTOList;
    }

    public void updateContact(Connection conn, ContactDTO contactDTO){
        LOGGER.info("method: updateContact({}, {})", conn, contactDTO);

        try {
            ContactDAO contactDAO = new ContactDAOImpl();
            PhoneNumberDAO numberDAO = new PhoneNumberDAOImpl();
            AttachmentDAO attachmentDAO = new AttachmentDAOImpl();
            ModelEntityConverter converter = new ModelEntityConverter();

            conn.setAutoCommit(false);

            Contact contact=converter.convertModelToEntity(contactDTO);
            contactDAO.update(conn, contact);

            List<PhoneNumberDTO> numberDTOList = contactDTO.getNumberDTOList();
            if(!numberDTOList.isEmpty()){
                for(PhoneNumberDTO numberDTO : numberDTOList){
                    if(numberDTO.getNumberId() != 0){
                        PhoneNumber number = converter.convertModelToEntity(numberDTO, contactDTO.getContactId());
                        numberDAO.update(conn, number);
                    }
                    else{
                        PhoneNumber number = converter.convertModelToEntity(numberDTO, contactDTO.getContactId());
                        numberDAO.save(conn, number);
                    }
                }
            }

            List<AttachmentDTO> attachmentDTOList = contactDTO.getAttachmentDTOList();
            if(!attachmentDTOList.isEmpty()){
                for(AttachmentDTO attachmentDTO : attachmentDTOList){
                    if(attachmentDTO.getAttachmentId() != 0){
                        Attachment attachment =
                                converter.convertModelToEntity(attachmentDTO, contactDTO.getContactId());
                        attachmentDAO.update(conn, attachment);
                    }
                    else{
                        Attachment attachment =
                                converter.convertModelToEntity(attachmentDTO, contactDTO.getContactId());
                        attachmentDAO.save(conn, attachment);
                    }
                }
            }

            List<Long> phoneNumberDeleteList = contactDTO.getPhoneNumberDeleteList();
            if(!phoneNumberDeleteList.isEmpty()){
                for(long id : phoneNumberDeleteList){
                    if(id != 0){
                        numberDAO.deleteById(conn, id);
                    }
                }
            }

            List<Long> attachDeleteList = contactDTO.getAttachDeleteList();
            if(!attachDeleteList.isEmpty()){
                for(long id : attachDeleteList){
                    if (id != 0) {
                        attachmentDAO.deleteById(conn, id);
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
            try {
                LOGGER.error("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException e1) {
                LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                        e.getSQLState(), e.getErrorCode(), e.getMessage());
            }
            throw new RuntimeException("File upload failed. Contact info wasn't saved");
        } catch (Exception e) {
            LOGGER.error("{} {}", e.getClass(), e.getMessage());
            try {
                LOGGER.error("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException e1) {
                LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                        e1.getSQLState(), e1.getErrorCode(), e1.getMessage());
            }
            throw new RuntimeException("File upload failed. Contact info wasn't saved");
        }
    }

    public void deleteContactsById(Connection conn, List<Long> ids) {

        LOGGER.info("method: deleteContactsById( {}, List.size() = {})", conn, ids.size());

        try {
            conn.setAutoCommit(false);

            PhoneNumberDAOImpl phoneNumberDAO = new PhoneNumberDAOImpl();
            AttachmentDAOImpl attachmentDAO = new AttachmentDAOImpl();
            ContactDAOImpl contactDAO = new ContactDAOImpl();

            for (long id : ids) {
                phoneNumberDAO.deleteByContactId(conn, id);
                attachmentDAO.deleteByContactId(conn, id);
                contactDAO.deleteByContactId(conn, id);
            }

            conn.commit();
        } catch (SQLException e) {
            LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
                try {
                    LOGGER.error("Transaction is being rolled back");
                    conn.rollback();
                } catch (SQLException e1) {
                    LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                            e.getSQLState(), e.getErrorCode(), e.getMessage());
                }
        } catch (IOException e){
            LOGGER.error("file deletion failed. Message: {}", e.getMessage());
        }
    }

    public void saveContact(Connection conn, ContactDTO contactDTO){

        LOGGER.info("method: saveContact({}, {})", conn, contactDTO.getClass().getSimpleName());
        ModelEntityConverter converter = new ModelEntityConverter();
        Contact contact= converter.convertModelToEntity(contactDTO);
        ContactDAO contactDAO = new ContactDAOImpl();

        try {
            conn.setAutoCommit(false);
            PhoneNumberDAO numberDAO = new PhoneNumberDAOImpl();
            AttachmentDAO attachmentDAO = new AttachmentDAOImpl();

            contactDAO.save(conn, contact);

            long contactId = contactDAO.getMaxContactId(conn);
            List<PhoneNumberDTO> numberDTOList = contactDTO.getNumberDTOList();
            if(!numberDTOList.isEmpty()){
                for(PhoneNumberDTO numberDTO: numberDTOList){
                    PhoneNumber number = converter.convertModelToEntity(numberDTO, contactId);
                    numberDAO.save(conn, number);
                }
            }

            List<AttachmentDTO> attachmentDTOList = contactDTO.getAttachmentDTOList();
            if(!attachmentDTOList.isEmpty()){
                for(AttachmentDTO attachmentDTO: attachmentDTOList){
                    Attachment attachment = converter.convertModelToEntity(attachmentDTO, contactId);
                    attachmentDAO.save(conn, attachment);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                    e.getSQLState(), e.getErrorCode(), e.getMessage());
            try {
                properties = new Properties();
                InputStream inputStream = ContactController.class.getClassLoader().getResourceAsStream(propFileName);
                properties.load(inputStream);

                LOGGER.error("Transaction is being rolled back");
                conn.rollback();
                Path deletePath = Paths.get(properties.getProperty("profileImageContainer")
                        + contactDAO.getLastContactId(conn));
                FileUtils.deleteQuietly(new File(deletePath.toAbsolutePath().toString()));
                deletePath = Paths.get(properties.getProperty("contactFilesContainer")
                        + contactDAO.getLastContactId(conn));
                FileUtils.deleteQuietly(new File(deletePath.toAbsolutePath().toString()));
            } catch (SQLException e1) {
                LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                        e.getSQLState(), e.getErrorCode(), e.getMessage());
            } catch (IOException e1) {
                LOGGER.error(e1.getMessage());
            }
            throw new RuntimeException("File upload failed. Contact info wasn't saved");
        } catch (Exception e) {
            LOGGER.error("{} {}", e.getClass(), e.getMessage());
            try {
                LOGGER.error("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException e1) {
                LOGGER.error("SQLState: {} ErrorCode: {} Message: {}",
                        e1.getSQLState(), e1.getErrorCode(), e1.getMessage());
            }
            throw new RuntimeException("File upload failed. Contact info wasn't saved");
        }
    }
}
