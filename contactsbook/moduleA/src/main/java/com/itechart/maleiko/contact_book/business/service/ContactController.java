package com.itechart.maleiko.contact_book.business.service;

import com.itechart.maleiko.contact_book.business.dao.*;
import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.Attachment;
import com.itechart.maleiko.contact_book.business.entity.Contact;
import com.itechart.maleiko.contact_book.business.entity.Image;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;
import com.itechart.maleiko.contact_book.business.service.exceptions.ServiceException;
import com.itechart.maleiko.contact_book.business.utils.ConnectionController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContactController {

    private static final org.slf4j.Logger LOGGER =
            org.slf4j.LoggerFactory.getLogger(ContactController.class);

    private ContactDAO contactDAO;
    private AttachmentDAO attachmentDAO;
    private PhoneNumberDAO phoneNumberDAO;
    private ModelEntityConverter modelEntityConverter;
    private EntityModelConverter entityModelConverter;
    private ConnectionController connectionController;

    public ContactController() {
        DAOFactoryProducer factoryProducer = DAOFactoryProducer.getInstance();
        DAOFactory daoFactory = factoryProducer.createDAOFactory();
        this.contactDAO = daoFactory.createContactDAO();
        this.attachmentDAO = daoFactory.createAttachmentDAO();
        this.phoneNumberDAO = daoFactory.createPhoneNumberDAO();
        this.modelEntityConverter = new ModelEntityConverter();
        this.entityModelConverter = new EntityModelConverter();
        this.connectionController = ConnectionController.getInstance();
    }

    public List<ContactDTO> getAllContactDTO(int skip, int limit) throws DAOException{
        LOGGER.info("method: getAllContactDTO()");

        List<ContactDTO> contactDTOList = new ArrayList<>();
        List<Contact> contactList;
        Connection connection = null;
        try {
            connection = connectionController.provideConnection();
            contactDAO.setConnection(connection);
            contactList = contactDAO.getAll(skip, limit);
            for (Contact contact : contactList)
                contactDTOList.add(entityModelConverter.convertEntityToModel(contact));
        }finally {
            connectionController.closeConnection(connection);
        }
        return contactDTOList;
    }

    public int getNumberOfContacts() throws DAOException{
        LOGGER.info("method: getNumberOfContactPages()");
        int numberOfContacts;
        Connection connection = null;
        try {
            connection = connectionController.provideConnection();
            contactDAO.setConnection(connection);
            numberOfContacts = contactDAO.getNumberOfContacts();
        }finally {
            connectionController.closeConnection(connection);
        }
        return numberOfContacts;
    }

    public PairResultSize findContactDTOs(Map<String, Object> fieldValue) throws DAOException{
        LOGGER.info("method: findContactDTOs({})", fieldValue.getClass().getSimpleName());

        PairResultSize newPair = new PairResultSize();
        PairResultSize receivedPair;
        List<ContactDTO> contactDTOList = new ArrayList<>();
        List<Contact> contactList;
        Connection connection = null;
        try {
            connection = connectionController.provideConnection();
            contactDAO.setConnection(connection);
            receivedPair = contactDAO.findByGivenParameters(fieldValue);
            contactList = receivedPair.getContactList();
            for (Contact contact : contactList) {
                contactDTOList.add(entityModelConverter.convertEntityToModel(contact));
            }
            newPair.setContactDTOList(contactDTOList);
            newPair.setResultSetSize(receivedPair.getResultSetSize());
        }finally {
            connectionController.closeConnection(connection);
        }
        return newPair;
    }

    public ContactDTO getContactDTOById(long id) throws DAOException{
        LOGGER.info("method: getContactDTOById({})", id);

        ContactDTO contactDTO;
        Connection connection = null;
        try {
            connection = connectionController.provideConnection();
            contactDAO.setConnection(connection);
            contactDTO = entityModelConverter.convertEntityToModel(contactDAO.findById(id));
        }
        finally {
            connectionController.closeConnection(connection);
        }
        return contactDTO;
    }

    public List<ContactDTO> getContactDTOsByIdList(List<Long> ids) throws DAOException{
        LOGGER.info("method: getContactDTOsByIdList({})", ids.getClass().getSimpleName());

        List<ContactDTO> contactDTOList = new ArrayList<>();
        List<Contact> contactList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionController.provideConnection();
            contactDAO.setConnection(connection);
            for (long id : ids) {
                contactList.add(contactDAO.findById(id));
            }
            for (Contact contact : contactList) {
                contactDTOList.add(entityModelConverter.convertEntityToModel(contact));
            }
        }finally {
            connectionController.closeConnection(connection);
        }
        return contactDTOList;
    }

    public void updateContact(ContactDTO contactDTO) throws DAOException, ServiceException{

        Connection connection = null;
        try {
            connection = connectionController.provideConnection();
            connection.setAutoCommit(false);
            contactDAO.setConnection(connection);
            attachmentDAO.setConnection(connection);
            phoneNumberDAO.setConnection(connection);
            Contact contact = modelEntityConverter.convertModelToEntity(contactDTO);
            contactDAO.update(contact);

            //save and update numbers
            if (!contact.getPhoneNumbers().isEmpty()) {
                List<PhoneNumber> numbersForSave = new ArrayList<>();
                List<PhoneNumber> numbersForUpdate = new ArrayList<>();
                for (PhoneNumber number : contact.getPhoneNumbers()) {
                    if (number.getNumberId() != 0) {
                        numbersForUpdate.add(number);
                    } else {
                        numbersForSave.add(number);
                    }
                }
                if (!numbersForUpdate.isEmpty()) {
                    phoneNumberDAO.update(numbersForUpdate);
                }
                if (!numbersForSave.isEmpty()) {
                    phoneNumberDAO.save(numbersForSave);
                }
            }
            //save and update attachments
            if (!contact.getAttachments().isEmpty()) {
                List<Attachment> attachmentsForUpdate = new ArrayList<>();
                List<Attachment> attachmentsForSave = new ArrayList<>();
                for (Attachment attachment : contact.getAttachments()) {
                    if (attachment.getAttachmentId() != 0) {
                        attachmentsForUpdate.add(attachment);
                    } else {
                        attachmentsForSave.add(attachment);
                    }
                }
                if(!attachmentsForUpdate.isEmpty()) {
                    attachmentDAO.update(attachmentsForUpdate);
                }
                if(!attachmentsForSave.isEmpty()) {
                    attachmentDAO.save(attachmentsForSave);
                }
            }

            //delete numbers
            List<Long> phoneNumberDeleteList = contactDTO.getPhoneNumberDeleteList();
            if (!phoneNumberDeleteList.isEmpty()) {
                phoneNumberDAO.deleteById(phoneNumberDeleteList);
            }

            //delete attachments
            List<Long> attachDeleteList =
                    contactDTO.getAttachDeleteList().stream().filter(id -> id != 0).collect(Collectors.toList());
            if (!attachDeleteList.isEmpty()) {
                attachmentDAO.deleteByIds(attachDeleteList);
            }

            connection.commit();
        } catch (SQLException e) {
            String message = "SQLState: " + e.getSQLState() + " ErrorCode: " + e.getErrorCode() +
                    "Message: {}" + e.getMessage();
            connectionController.rollback(connection);
            throw new ServiceException(message, e);
        } catch (DAOException e) {
            connectionController.rollback(connection);
            throw e;
        }finally {
            connectionController.closeConnection(connection);
        }
    }

    public void deleteContactsByIds(List<Long> ids) throws ServiceException, DAOException{
        LOGGER.info("method: deleteContactsByIds({})", ids);

        Connection connection = null;
        try {
            connection = connectionController.provideConnection();
            connection.setAutoCommit(false);
            contactDAO.setConnection(connection);
            contactDAO.deleteByContactIds(ids);
            connection.commit();
        } catch (SQLException e) {
            String message = "SQLState: " + e.getSQLState() + " ErrorCode: " + e.getErrorCode() +
                    "Message: {}" + e.getMessage();
            connectionController.rollback(connection);
            throw new ServiceException(message, e);
        }finally {
            connectionController.closeConnection(connection);
        }
    }

    public void saveContact(ContactDTO contactDTO) throws ServiceException, DAOException{
        LOGGER.info("method: saveContact({}, {})", contactDTO.getClass().getSimpleName());

        Contact contact = modelEntityConverter.convertModelToEntity(contactDTO);
        Connection connection = null;
        try {
            connection = connectionController.provideConnection();
            connection.setAutoCommit(false);
            contactDAO.setConnection(connection);
            contactDAO.save(contact);

            connection.commit();
        } catch (SQLException e) {
            String message = "SQLState: " + e.getSQLState() + " ErrorCode: " + e.getErrorCode() +
                    "Message: {}" + e.getMessage();
            connectionController.rollback(connection);
            throw new ServiceException(message, e);
        } catch (DAOException e) {
            connectionController.rollback(connection);
            throw e;
        }finally {
            connectionController.closeConnection(connection);
        }
    }

    public Image getProfileImageByContactId(long id) throws DAOException{
        Connection connection = null;
        try{
            connection = connectionController.provideConnection();
            contactDAO.setConnection(connection);
            return contactDAO.getProfileImageByContactId(id);
        }finally{
            connectionController.closeConnection(connection);
        }
    }

    public Attachment getAttachmentById(long id) throws DAOException{
        Connection connection = null;
        try{
            connection = connectionController.provideConnection();
            attachmentDAO.setConnection(connection);
            return attachmentDAO.getFile(id);
        }finally{
            connectionController.closeConnection(connection);
        }
    }
}
