package edu.uw.tcss450team2client.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentChatCardBinding;

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    private List<Message> mMessages;
    private final FragmentManager mFrag;

    public ChatListRecyclerViewAdapter(List<Message> items, FragmentManager fm) {
        mMessages = items;
        mFrag = fm;
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
        holder.setMessage(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentChatCardBinding binding;
        public Message mMessage;

        public ChatListViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);

            view.setOnClickListener(v -> {
                navigateToChat();
            });
        }

        private void navigateToChat() {
            ChatListFragmentDirections.ActionNavigationChatToChatFragment directions =
                    ChatListFragmentDirections.actionNavigationChatToChatFragment(mMessage.getChatID(), mMessage.getMessageName());
        }

        void setMessage(final Message message) {
            binding.textMessageName.setText(message.getMessageName());
            mMessage = message;
            binding.buttonDelete.setText("Delete");
            binding.buttonDelete.setOnClickListener(button -> handleDelete());
        }

        public void handleDelete() {
            //DeleteChatDialog dialog = new DeleteChatDialog(mMessage.getChatID(), mFrag, this);
            //dialog.show(mFrag, "...");
        }
    }
}
