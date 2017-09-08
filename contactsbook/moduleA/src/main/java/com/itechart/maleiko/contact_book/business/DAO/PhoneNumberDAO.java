package com.itechart.maleiko.contact_book.business.dao;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.PhoneNumber;

import java.util.List;

public interface PhoneNumberDAO extends DAO{
    void save(List<PhoneNumber> phoneNumber) throws DAOException;

    void update(List<PhoneNumber> phoneNumber) throws DAOException;

    List<PhoneNumber> findByContactId(long contact_id) throws DAOException;

    void deleteByContactId(long id) throws DAOException;

    void deleteByIds(List<Long> ids) throws DAOException;
}
