package com.itechart.maleiko.contact_book.business.dao;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Alexey on 15.03.2017.
 */
public interface PhoneNumberDAO extends DAO{
    void save(List<PhoneNumber> phoneNumber) throws DAOException;

    void update(List<PhoneNumber> phoneNumber) throws DAOException;

    List<PhoneNumber> findByContactId(long contact_id) throws DAOException;

    void deleteByContactId(long id) throws DAOException;

    void deleteById(List<Long> ids) throws DAOException;
}
