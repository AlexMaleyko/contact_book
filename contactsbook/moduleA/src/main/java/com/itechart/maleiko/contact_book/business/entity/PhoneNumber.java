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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneNumber number1 = (PhoneNumber) o;

        if (numberId != number1.numberId) return false;
        if (contactId != number1.contactId) return false;
        if (countryCode != null ? !countryCode.equals(number1.countryCode) : number1.countryCode != null) return false;
        if (operatorCode != null ? !operatorCode.equals(number1.operatorCode) : number1.operatorCode != null)
            return false;
        if (number != null ? !number.equals(number1.number) : number1.number != null) return false;
        if (type != null ? !type.equals(number1.type) : number1.type != null) return false;
        return comment != null ? comment.equals(number1.comment) : number1.comment == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (numberId ^ (numberId >>> 32));
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (operatorCode != null ? operatorCode.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (int) (contactId ^ (contactId >>> 32));
        return result;
    }
}
