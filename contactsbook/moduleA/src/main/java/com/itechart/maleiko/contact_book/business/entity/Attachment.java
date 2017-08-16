package com.itechart.maleiko.contact_book.business.entity;

import org.apache.commons.fileupload.FileItem;
import java.sql.Timestamp;

public class Attachment {
    private long attachmentId;
    private String filePath;
    private String fileName;
    private FileItem file;
    private Timestamp uploadDate;
    private String comment;
    private byte[] bytes;
    private String length;
    private long contactId;

    public Attachment(String fileName, String length, byte[] bytes){
        this.fileName = fileName;
        this.length = length;
        this.bytes = bytes;
    }
    public Attachment(){}

    public byte[] getBytes() {
        return bytes;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public FileItem getFile() {
        return file;
    }

    public void setFile(FileItem file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "com.itechart.maleiko.contact_book.business.entity.Attachment{" +
                "attachmentId=" + attachmentId +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", uploadDate=" + uploadDate +
                ", comment='" + comment + '\'' +
                ", contactId=" + contactId +
                '}' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attachment that = (Attachment) o;

        if (attachmentId != that.attachmentId) return false;
        if (contactId != that.contactId) return false;
        if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (file != null ? !file.equals(that.file) : that.file != null) return false;
        if (uploadDate != null ? !uploadDate.equals(that.uploadDate) : that.uploadDate != null) return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (attachmentId ^ (attachmentId >>> 32));
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (uploadDate != null ? uploadDate.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (int) (contactId ^ (contactId >>> 32));
        return result;
    }
}
