package com.itechart.maleiko.contact_book.business.dao;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.Contact;
import com.itechart.maleiko.contact_book.business.entity.Image;

import java.util.List;
import java.util.Map;

public interface ContactDAO extends DAO {
    List<Contact> getAll(int skip, int limit) throws DAOException;

    Contact findById(long id) throws DAOException;

    void save(Contact contact) throws DAOException;

    void update(Contact contact) throws DAOException;

    void deleteByContactIds(List<Long> ids) throws DAOException;

    PairResultSize findByGivenParameters(Map<String, Object> fieldValue) throws DAOException;

    int getNumberOfContacts() throws DAOException;

    Image getProfileImageByContactId(long id) throws DAOException;
}
