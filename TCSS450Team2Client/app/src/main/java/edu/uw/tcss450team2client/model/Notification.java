package edu.uw.tcss450team2client.model;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.ui.chat.ChatMessage;
import edu.uw.tcss450team2client.ui.contacts.Invitation;

/**
 * Container object for store any of the three types of notifications this app currently uses.
 */
public class Notification {

    /**
     * Thie invitation request type
     */
    public static final String TYPE_INVITATION = "Invitation";

    /**
     * The chat message type
     */
    public static final String TYPE_CHAT_MESSAGE = "New Chat Message";

    /**
     * The Invitation object storing all relevant information for contact requests
     */
    private Invitation mInvitation;

    /**
     * Chat Message object that stores all information for new messages received
     */
    private ChatMessage mChatMessageFragment;

    /**
     * Notification object stores one of the different types of notifications, they must be assigned using setData()
     */
    public Notification() {

    }

    /**
     * Container is assigned an invitation, this will overwrite any other data this container may have been set to hold.
     * @param invitation The invite to assign to this container
     */
    public void setData(Invitation invitation) {
        this.mInvitation = invitation;
        this.mChatMessageFragment = null;
    }

    /**
     * Container is assigned a chat message, this will overwrite any other data this container may have been set to hold.
     * @param chatMessageFragment The message to assign to this container
     */
    public void setData(ChatMessage chatMessageFragment) {
        this.mChatMessageFragment = chatMessageFragment;
        this.mInvitation = null;
    }

    /**
     * Gets the message that the notification card should display, if the data has not been set, it will return null
     * @return A string representation of the notification message or null
     */
    public String getNotificationMessage() {
        if (this.mInvitation != null) return this.mInvitation.getSenderUsername() + " wants to be your buddy";
        else if (this.mChatMessageFragment != null) return this.mChatMessageFragment.getMessage();
        else return null;
    }

    /**
     * Gets the title that the notification card should display, if the data has not been set, it will return null
     * @return  string representation of the notification title or null
     */
    public String getNotificationTitle() {
        if (this.mInvitation != null) return "Contact request from: " + this.mInvitation.getSenderUsername();
        else if (this.mChatMessageFragment != null)  {
            String title =  "New message from: "
                    + this.mChatMessageFragment.getSender();
            return title;
        }
        else return null;
    }

    /**
     * Gets the notification that this object containers
     * @return an Object that must be cast for its respective type
     */
    public Object getData() {
        if (this.mInvitation != null) return this.mInvitation;
        else if (this.mChatMessageFragment != null) return this.mChatMessageFragment;
        else return null;
    }

    /**
     * Gets the type of the object contained within
     * @return String representation of the type of object in the form of the constant values
     */
    public String getType() {
        if (this.mInvitation != null) return TYPE_INVITATION;
        else if (this.mChatMessageFragment != null) return TYPE_CHAT_MESSAGE;
        else return null;
    }

    public boolean equals(Object theOther) {
        if (theOther == null) return false;
        if (this.mInvitation != null) {
            if(!((Notification) theOther).getType().equals(TYPE_INVITATION)) return false;
            else if(this.mInvitation.getSenderUsername().equals(((Notification) theOther).mInvitation.getSenderUsername())) return true;
            else return false;
        } else if (this.mChatMessageFragment != null) {
            if(!((Notification) theOther).getType().equals(TYPE_CHAT_MESSAGE)) return false;
            else if (this.mChatMessageFragment.getMessageId() == ((Notification) theOther).mChatMessageFragment.getMessageId()) return true;
            else return false;
        }
        else return false;
    }

}
