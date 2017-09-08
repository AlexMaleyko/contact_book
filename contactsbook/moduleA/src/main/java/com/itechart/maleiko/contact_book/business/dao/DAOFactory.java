package com.itechart.maleiko.contact_book.business.dao;

public interface DAOFactory {
    AttachmentDAO createAttachmentDAO();
    ContactDAO createContactDAO();
    PhoneNumberDAO createPhoneNumberDAO();
}
