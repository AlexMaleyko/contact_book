package com.itechart.maleiko.contact_book.business.model;

import org.apache.commons.fileupload.FileItem;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by Alexey on 21.03.2017.
 */
public class ContactDTO {
    private long contactId;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birth;
    private String gender;
    private String citizenship;
    private String maritalStatus;
    private String website;
    private String email;
    private String job;
    private String country;
    private String city;
    private String street;
    private String postalCode;
    private String profilePictureName;
    private FileItem profileImage;
    private List<PhoneNumberDTO> numberDTOList;
    private List<AttachmentDTO> attachmentDTOList;
    private List<Long> phoneNumberDeleteList;
    private List<Long> attachDeleteList;

    public List<PhoneNumberDTO> getNumberDTOList() {
        return numberDTOList;
    }

    public void setNumberDTOList(List<PhoneNumberDTO> numberDTOList) {
        this.numberDTOList = numberDTOList;
    }

    public List<AttachmentDTO> getAttachmentDTOList() {
        return attachmentDTOList;
    }

    public void setAttachmentDTOList(List<AttachmentDTO> attachmentDTOList) {
        this.attachmentDTOList = attachmentDTOList;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProfilePictureName() {
        return profilePictureName;
    }

    public void setProfilePictureName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
    }

    public FileItem getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(FileItem profileImage) {
        this.profileImage = profileImage;
    }

    public List<Long> getPhoneNumberDeleteList() {
        return phoneNumberDeleteList;
    }

    public void setPhoneNumberDeleteList(List<Long> phoneNumberDeleteList) {
        this.phoneNumberDeleteList = phoneNumberDeleteList;
    }

    public List<Long> getAttachDeleteList() {
        return attachDeleteList;
    }

    public void setAttachDeleteList(List<Long> attachDeleteList) {
        this.attachDeleteList = attachDeleteList;
    }
}
