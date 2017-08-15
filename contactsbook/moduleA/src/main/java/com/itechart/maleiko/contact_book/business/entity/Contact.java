package com.itechart.maleiko.contact_book.business.entity; /**
 * Created by Alexey on 15.03.2017.
 */

import org.apache.commons.fileupload.FileItem;

import java.util.List;

public class Contact {
    private long contactId;
    private String name;
    private String surname;
    private String patronymic;
    private java.sql.Date birth;
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
    private FileItem profileImage;
    private String profilePicturePath;
    List<Attachment> attachments;
    List<PhoneNumber> phoneNumbers;

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        if(attachments != null) {
            attachments.forEach(attachment -> attachment.setContactId(this.contactId));
            this.attachments = attachments;
        }
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        if(phoneNumbers != null) {
            phoneNumbers.forEach(phoneNumber -> phoneNumber.setContactId(this.contactId));
            this.phoneNumbers = phoneNumbers;
        }
    }

    public Contact() { }
    public Contact(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
        setPhoneNumbers(phoneNumbers);
        setAttachments(attachments);
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

    public java.sql.Date getBirth() {
        return birth;
    }

    public void setBirth(java.sql.Date birth) {
       /* SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
        java.util.Date date=null;
        java.sql.Date sqlDate= null;
        try {
           date = sdf.parse(birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sqlDate=new Date(date.getTime());*/
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

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public FileItem getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(FileItem profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "com.itechart.maleiko.contact_book.business.entity.Contact{" +
                "contactId=" + contactId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birth=" + birth +
                ", gender='" + gender + '\'' +
                ", citizenship='" + citizenship + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", job='" + job + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", profilePicturePath='" + profilePicturePath + '\'' +
                '}' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (contactId != contact.contactId) return false;
        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        if (surname != null ? !surname.equals(contact.surname) : contact.surname != null) return false;
        if (patronymic != null ? !patronymic.equals(contact.patronymic) : contact.patronymic != null) return false;
        if (birth != null ? !birth.equals(contact.birth) : contact.birth != null) return false;
        if (gender != null ? !gender.equals(contact.gender) : contact.gender != null) return false;
        if (citizenship != null ? !citizenship.equals(contact.citizenship) : contact.citizenship != null) return false;
        if (maritalStatus != null ? !maritalStatus.equals(contact.maritalStatus) : contact.maritalStatus != null)
            return false;
        if (website != null ? !website.equals(contact.website) : contact.website != null) return false;
        if (email != null ? !email.equals(contact.email) : contact.email != null) return false;
        if (job != null ? !job.equals(contact.job) : contact.job != null) return false;
        if (country != null ? !country.equals(contact.country) : contact.country != null) return false;
        if (city != null ? !city.equals(contact.city) : contact.city != null) return false;
        if (street != null ? !street.equals(contact.street) : contact.street != null) return false;
        if (postalCode != null ? !postalCode.equals(contact.postalCode) : contact.postalCode != null) return false;
        if (profileImage != null ? !profileImage.equals(contact.profileImage) : contact.profileImage != null)
            return false;
        return profilePicturePath != null ? profilePicturePath.equals(contact.profilePicturePath) : contact.profilePicturePath == null;
    }

    public boolean equalsWithoutIdBirthImage(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        if (surname != null ? !surname.equals(contact.surname) : contact.surname != null) return false;
        if (patronymic != null ? !patronymic.equals(contact.patronymic) : contact.patronymic != null) return false;
       // if (birth != null ? !birth.equals(contact.birth) : contact.birth != null) return false;
        if (gender != null ? !gender.equals(contact.gender) : contact.gender != null) return false;
        if (citizenship != null ? !citizenship.equals(contact.citizenship) : contact.citizenship != null) return false;
        if (maritalStatus != null ? !maritalStatus.equals(contact.maritalStatus) : contact.maritalStatus != null)
            return false;
        if (website != null ? !website.equals(contact.website) : contact.website != null) return false;
        if (email != null ? !email.equals(contact.email) : contact.email != null) return false;
        if (job != null ? !job.equals(contact.job) : contact.job != null) return false;
        if (country != null ? !country.equals(contact.country) : contact.country != null) return false;
        if (city != null ? !city.equals(contact.city) : contact.city != null) return false;
        if (street != null ? !street.equals(contact.street) : contact.street != null) return false;
        return postalCode != null ? postalCode.equals(contact.postalCode) : contact.postalCode == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (contactId ^ (contactId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
        result = 31 * result + (birth != null ? birth.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (citizenship != null ? citizenship.hashCode() : 0);
        result = 31 * result + (maritalStatus != null ? maritalStatus.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (job != null ? job.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (profileImage != null ? profileImage.hashCode() : 0);
        result = 31 * result + (profilePicturePath != null ? profilePicturePath.hashCode() : 0);
        return result;
    }
}
