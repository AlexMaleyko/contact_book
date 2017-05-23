package com.itechart.maleiko.contact_book.business.entity;

/**
 * Created by Alexey on 15.03.2017.
 */
public class PhoneNumber {
    private long numberId;
    private String countryCode;
    private String operatorCode;
    private String number;
    private String type;
    private String comment;
    private long contactId;

    public long getNumberId() {
        return numberId;
    }

    public void setNumberId(long numberId) {
        this.numberId = numberId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getContactId() { return contactId; }

    public void setContactId(long contactId) { this.contactId = contactId;}

    @Override
    public String toString() {
        return "com.itechart.maleiko.contact_book.business.entity.PhoneNumber{" +
                "numberId=" + numberId +
                ", countryCode='" + countryCode + '\'' +
                ", operatorCode='" + operatorCode + '\'' +
                ", number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", contactId=" + contactId +
                '}';
    }
}
