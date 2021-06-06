package edu.uw.tcss450team2client.ui.contacts;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.Serializable;

public class Invitation implements Serializable {

    /**
     * String of the sender's username
     */
    private final String mSenderUsername;

    public Invitation(final String senderUsername) {
        this.mSenderUsername = senderUsername;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * Invitation object.
     *
     * @param contactRequestUsername the String to be parsed into a Invitation Object.
     * @return a Invitation Object with the details contained in the JSON String.
     * @throws JSONException when contactRequestJSON cannot be parsed into a Invitation Object.
     */
    public static Invitation createFromString(final String contactRequestUsername) {
        return new Invitation(contactRequestUsername);
    }

    /**
     * Check if invites are equal
     * @param other the other object to check for equality
     * @return true if other username matches this username, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof Invitation) {
            result = mSenderUsername.equals(((Invitation) other).getSenderUsername());
        }
        return result;
    }

    /**
     *  Method that returns the sender's username for this contact request
     * @return Value of senderUsername
     */
    public String getSenderUsername() {
        return mSenderUsername;
    }
}
