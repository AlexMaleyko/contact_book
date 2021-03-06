package com.itechart.maleiko.contact_book.business.dao;

import com.itechart.maleiko.contact_book.business.entity.Contact;
import com.itechart.maleiko.contact_book.business.model.ContactDTO;

import java.util.List;

public class PairResultSize {
    private List<Contact> contactList;
    private List<ContactDTO> contactDTOList;
    private long resultSetSize;


    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public List<ContactDTO> getContactDTOList() {
        return contactDTOList;
    }

    public void setContactDTOList(List<ContactDTO> contactDTOList) {
        this.contactDTOList = contactDTOList;
    }

    public long getResultSetSize() {
        return resultSetSize;
    }

    public void setResultSetSize(long resultSetSize) {
        this.resultSetSize = resultSetSize;
    }
}
