package com.example.user.test;

/**
 * Created by user on 9/5/2017.
 */

public class Contact {

    private String contactId;
    private String contactNickname;
    private String contactName;
    private String contactCompany;
    private String contactEmail;
    private String contactPhoneNumber;
    private String contactWebsite;
    private String contactJobName;
    private boolean contactisFriend;
    private boolean contactisBlocked;
    private String contactNationality;
    private String contactAge;
    private String contactDateOfBirth;
    private String contactGender;
    private String contactStatusMsg;
    private String contactVisibilityList;
    private String contactInterests;
    private String contactInterestsOther;
    private String contactInterestsRating;
    private String contactLastSeenDate;
    private String contactimageString;
    private int contactStatus;

    /*Defaul Cnstructor*/
    public Contact(){ }

    /*
    *   Create get and set methods for all fields
    * */
    public String getContactId() { return contactId; }

    public void setContactId(String contactId) { this.contactId = contactId; }

    public String getContactNickname() { return contactNickname; }

    public void setContactNickname(String contactNickname) { this.contactNickname = contactNickname; }

    public String getContactName() { return contactName; }

    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactCompany() { return contactCompany; }

    public void setContactCompany(String contactCompany) { this.contactCompany = contactCompany; }

    public String getContactEmail() { return contactEmail; }

    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhoneNumber() { return contactPhoneNumber; }

    public void setContactPhoneNumber(String contactPhoneNumber) { this.contactPhoneNumber = contactPhoneNumber; }

    public String getContactWebsite() { return contactWebsite; }

    public void setContactWebsite(String contactWebsite) {  this.contactWebsite = contactWebsite; }

    public String getContactJobName() { return contactJobName; }

    public void setContactJobName(String contactJobName) { this.contactJobName = contactJobName; }

    public boolean isContactisFriend() {return contactisFriend;}

    public void setContactisFriend(boolean contactisFriend) { this.contactisFriend = contactisFriend; }

    public boolean isContactisBlocked() { return contactisBlocked; }

    public void setContactisBlocked(boolean contactisBlocked) { this.contactisBlocked = contactisBlocked; }

    public String getContactNationality() { return contactNationality; }

    public void setContactNationality(String contactNationality) { this.contactNationality = contactNationality; }

    public String getContactAge() { return contactAge; }

    public void setContactAge(String contactAge) { this.contactAge = contactAge; }

    public String getContactDateOfBirth() { return contactDateOfBirth; }

    public void setContactDateOfBirth(String contactDateOfBirth) { this.contactDateOfBirth = contactDateOfBirth; }

    public String getContactGender() { return contactGender; }

    public void setContactGender(String contactGender) { this.contactGender = contactGender; }

    public String getContactStatusMsg() { return contactStatusMsg; }

    public void setContactStatusMsg(String contactStatusMsg) { this.contactStatusMsg = contactStatusMsg; }

    public String getContactVisibilityList() { return contactVisibilityList; }

    public void setContactVisibilityList(String contactVisibilityList) { this.contactVisibilityList = contactVisibilityList; }

    public String getContactInterests() { return contactInterests; }

    public void setContactInterests(String contactInterests) { this.contactInterests = contactInterests; }

    public String getContactInterestsOther() { return contactInterestsOther; }

    public void setContactInterestsOther(String contactVisibilityList) { this.contactVisibilityList = contactVisibilityList; }

    public String getContactInterestsRating() { return contactInterestsRating; }

    public void setContactInterestsRating(String contactInterestsRating) { this.contactInterestsRating = contactInterestsRating; }

    public String getContactLastSeenDate() { return contactLastSeenDate; }

    public void setContactLastSeenDate(String contactLastSeenDate) { this.contactLastSeenDate = contactLastSeenDate; }

    public String getContactimageString() { return contactimageString; }

    public void setContactimageString(String contactimageString) { this.contactimageString = contactimageString; }

    public int getContactStatus() { return contactStatus; }

    public void setContactStatus(int contactStatus) { this.contactStatus = contactStatus; }
}
