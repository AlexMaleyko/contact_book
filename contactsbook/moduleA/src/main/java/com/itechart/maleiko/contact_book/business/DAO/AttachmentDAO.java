package com.itechart.maleiko.contact_book.business.dao;

import com.itechart.maleiko.contact_book.business.dao.exceptions.DAOException;
import com.itechart.maleiko.contact_book.business.entity.Attachment;

import java.util.List;

public interface AttachmentDAO extends DAO{
    void save(List<Attachment> attachments) throws DAOException;

    void update(List<Attachment> attachments) throws DAOException;

    List<Attachment> findByContactId(long contactId) throws DAOException;

    void deleteByContactId(long id) throws DAOException;

    void deleteByIds(List<Long> ids) throws DAOException;

    Attachment getFile(long attachmentId) throws DAOException;
}
