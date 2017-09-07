package com.example.user.test;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 9/7/2017.
 */

public class UserApp {

    private int userID;
    private String statusMessage;
    private String userStatus;
    private String userPassword;
    private String userVisibility;

    public UserApp(){}


    public int getUserID() {return userID;}
    public void setUserID(int userID) { this.userID = userID; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }

    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

    public String getUserStatus() { return userStatus; }
    public void setUserStatus(String userStatus) { this.userStatus = userStatus; }

    public String getUserVisibility() { return userVisibility; }
    public void setUserVisibility(String userVisibility) { this.userVisibility = userVisibility; }

}
