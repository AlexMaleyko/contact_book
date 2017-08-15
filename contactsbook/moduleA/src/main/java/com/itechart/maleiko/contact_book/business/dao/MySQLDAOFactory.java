package com.itechart.maleiko.contact_book.business.dao;

import com.itechart.maleiko.contact_book.business.dao.mysql.AttachmentDAOImpl;
import com.itechart.maleiko.contact_book.business.dao.mysql.ContactDAOImpl;
import com.itechart.maleiko.contact_book.business.dao.mysql.PhoneNumberDAOImpl;

/**
 * Created by Alexey on 25.07.2017.
 */
public class MySQLDAOFactory implements DAOFactory{

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
