package com.itechart.maleiko.contact_book.business.dao;

/**
 * Created by Alexey on 25.07.2017.
 */
public interface DAOFactory {
    AttachmentDAO createAttachmentDAO();
    ContactDAO createContactDAO();
    PhoneNumberDAO createPhoneNumberDAO();
}
