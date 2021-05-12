package edu.uw.tcss450team2client.ui.contacts;

import java.io.Serializable;

public class Contact implements Serializable {

    private final String mFirstName;
    private final String mLastName;
    private final String mEmail;
    private final String mUserName;


    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */

    public Contact(String mFName, String mLName, String mEmail, String mUserName) {
        this.mFirstName = mFName;
        this.mLastName = mLName;
        this.mEmail = mEmail;
        this.mUserName = mUserName;

    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() { return mLastName; }

    public String getEmail() {
        return mEmail;
    }

    public String getUserName() { return mUserName; }



}
