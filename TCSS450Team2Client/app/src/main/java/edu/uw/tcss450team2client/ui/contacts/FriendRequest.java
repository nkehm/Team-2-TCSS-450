package edu.uw.tcss450team2client.ui.contacts;

public class FriendRequest {

    private final String mUserName;

    private final int mMemberID;

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
