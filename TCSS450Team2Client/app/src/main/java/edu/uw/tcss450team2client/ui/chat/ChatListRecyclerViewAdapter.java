package edu.uw.tcss450team2client.ui.chat;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.List;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentChatCardBinding;

/**
 * A recycler view for the chat list.
 *
 * @author Nathan Stickler
 * @author Nam Hoang
 * @version 5/2021
 */
public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    /**
     * A list of chat rooms.
     */
    List<ChatRoom> mChatRooms;

    /**
     * A fragment that builds the recycler view.
     */
    private final ChatListFragment mParent;

    /**
     * Constructor that builds the recycler view adapter from the list of chat rooms.
     *
     * @param chats the chat rooms
     * @param parent the recycler view
     */
    public ChatListRecyclerViewAdapter(List<ChatRoom> chats, ChatListFragment parent) {
        this.mChatRooms = chats;
        this.mParent = parent;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {

        try {
            holder.setChatRoom(mChatRooms.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mChatRooms.size();
    }

    /**
     * Creates a chat list holder.
     */
    public class ChatListViewHolder extends RecyclerView.ViewHolder {


        public final View mView;
        public FragmentChatCardBinding binding;
        Observer<List<String>> mObserver;

        /**
         * Constructs a chat room view holder
         * @param view view
         */
        public ChatListViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);

//            view.setOnClickListener(v -> {
//                navigateToChat();
//            });
        }

        void setChatRoom(final ChatRoom chatRoom) throws JSONException {
            binding.deleteChatRoom.setOnClickListener(view -> deleteChat(this, chatRoom));
            binding.layoutInner.setOnClickListener(view -> Navigation.findNavController(mView).navigate(ChatListFragmentDirections.actionNavigationChatToChatFragment(chatRoom)));

            LiveData<List<String>> liveData = chatRoom.getLiveEmailList();
            Observer<List<String>> observer = strings -> {
                if (strings.isEmpty()) {
                    return;
                }
                String emails = String.join(", ", strings);
                Log.d("test log", strings.toString());
                binding.textMessageName.setText(emails);
            };

            if (mObserver != null){
                liveData.removeObserver(mObserver);
            }
            liveData.observe(mParent, observer);
            mObserver = observer;
//            String emails = String.join(", ", liveData.getValue());
//            binding.textMessageName.setText("Chat Room: #: " + emails);
        }

//        private void navigateToChat() {
//            ChatListFragmentDirections.ActionNavigationChatToChatFragment directions =
//                    ChatListFragmentDirections.actionNavigationChatToChatFragment(mMessage.getChatID(), mMessage.getMessageName());
//        }

//        void setMessage(final Message message) {
//            binding.textMessageName.setText(message.getMessageName());
//            mMessage = message;
//            binding.buttonDelete.setText("Delete");
//            binding.buttonDelete.setOnClickListener(button -> handleDelete());
//        }

        public void handleDelete() {
            //DeleteChatDialog dialog = new DeleteChatDialog(mMessage.getChatID(), mFrag, this);
            //dialog.show(mFrag, "...");
        }
    }

    private void deleteChat(final ChatListViewHolder view, final ChatRoom chatRoom) {
        Log.d("ChatListRecycle", "Pop up dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(mParent.getActivity());
        builder.setTitle(R.string.dialog_chatListRecycler_title);
        builder.setMessage(R.string.dialog_chatListRecycler_message);
        builder.setPositiveButton(R.string.dialog_chatListRecycler_positive, (dialog, which) -> {
            mChatRooms.remove(chatRoom);
            notifyItemRemoved(view.getLayoutPosition());
            final int chatId = chatRoom.getChatId();
            mParent.deleteChat(chatId);
            Log.d("ChatListRecycle", "Removed chatroom with ID: " + chatId);
        });
        builder.setNegativeButton(R.string.dialog_chatListRecycler_negative, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setChatRooms(List<ChatRoom> rooms){
        mChatRooms = rooms;
        notifyDataSetChanged();
    }
}
