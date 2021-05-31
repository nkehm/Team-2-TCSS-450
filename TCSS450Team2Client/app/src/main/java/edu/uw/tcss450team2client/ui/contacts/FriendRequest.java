package edu.uw.tcss450team2client.ui.contacts;

/**
 * Friend Request Object
 *
 * @author Caleb Chang
 * @version 05/2021
 */
public class FriendRequest {
    private final String mUserName;
    private final int mMemberID;

    /**
     * Constructor for the FriendRequest object
     * @param username
     * @param mMemberID
     */
    public FriendRequest(String username, int mMemberID) {
        this.mUserName = username;
        this.mMemberID = mMemberID;
    }

    public String getUsername() {
        return mUserName;
    }

    public int getMemberID() {
        return mMemberID;
    }
}
