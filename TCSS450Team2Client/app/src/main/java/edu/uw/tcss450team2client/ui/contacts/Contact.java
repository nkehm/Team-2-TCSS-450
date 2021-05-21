package edu.uw.tcss450team2client.ui.contacts;

import java.io.Serializable;

/**
 * Contact class for contact object
 *
 * @author Caleb Chang
 * @version 05/2021
 */
public class Contact implements Serializable {

    private final String mFirstName;
    private final String mLastName;
    private final String mEmail;
    private final String mUserName;
    private final int mMemberID;

    public Contact(final String mEmail, final String mFName, final String mLName, final String mUserName, final int id) {
        this.mFirstName = mFName;
        this.mLastName = mLName;
        this.mEmail = mEmail;
        this.mUserName = mUserName;
        this.mMemberID = id;

    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() { return mLastName; }

    public String getEmail() {
        return mEmail;
    }

    public String getUserName() { return mUserName; }

    public int getMemberID() { return mMemberID; }
}
