package edu.uw.tcss450team2client.ui.chat;

import java.io.Serializable;

public class Message implements Serializable {

    private final String mMessageName;
    private final int mChatID;

    public Message(String messageName, int chatID) {
        mMessageName = messageName;
        mChatID = chatID;
    }

    public String getMessageName() {
        return mMessageName;
    }

    public int getChatID() {
        return mChatID;
    }
}
