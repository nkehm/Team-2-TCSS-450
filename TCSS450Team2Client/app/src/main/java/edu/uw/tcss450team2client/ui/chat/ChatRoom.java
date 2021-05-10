package edu.uw.tcss450team2client.ui.chat;

import java.io.Serializable;

public class ChatRoom implements Serializable {

    private final int mChatId;

    public ChatRoom(final int chatId) {
        mChatId = chatId;

    }

    public int getChatId() {
        return mChatId;
    }
}
