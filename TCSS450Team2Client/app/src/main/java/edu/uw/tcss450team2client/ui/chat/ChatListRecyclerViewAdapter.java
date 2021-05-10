package edu.uw.tcss450team2client.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentChatListBinding;

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    private List<ChatRoom> mChatRooms;

    public ChatListRecyclerViewAdapter(List<ChatRoom> chatRooms) {
        mChatRooms = chatRooms;
    }

    @NonNull
    @Override
    public ChatListRecyclerViewAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListRecyclerViewAdapter.ChatListViewHolder holder, int position) {
        //holder.setChatRoom(mChatRooms.get(position));
    }

    @Override
    public int getItemCount() {
        return mChatRooms.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentChatListBinding binding;
        Observer<List<String>> mObserver;

        public ChatListViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatListBinding.bind(view);
        }

        public void setChatRoom(List<ChatRoom> chatRooms) {
            mChatRooms = chatRooms;
        }
    }
}
