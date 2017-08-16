package com.itechart.maleiko.contact_book.business.dao.mysql;

import com.itechart.maleiko.contact_book.business.dao.AttachmentDAO;
import com.itechart.maleiko.contact_book.business.dao.ContactDAO;
import com.itechart.maleiko.contact_book.business.dao.DAOFactory;
import com.itechart.maleiko.contact_book.business.dao.PhoneNumberDAO;

public class MySQLDAOFactory implements DAOFactory {

    @Override
    public AttachmentDAO createAttachmentDAO() {
        return new AttachmentDAOImpl();
    }

    @Override
    public ContactDAO createContactDAO() {
        return new ContactDAOImpl(new AttachmentDAOImpl(), new PhoneNumberDAOImpl());
    }

    @Override
    public PhoneNumberDAO createPhoneNumberDAO() {
        return new PhoneNumberDAOImpl();
    }
}
