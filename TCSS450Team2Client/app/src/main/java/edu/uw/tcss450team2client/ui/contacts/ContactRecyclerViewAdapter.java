package edu.uw.tcss450team2client.ui.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

public class ContactRecyclerViewAdapter extends
        RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mContacts;
    private final Context mContext;
    private final FragmentManager mFragMan;
    private final UserInfoViewModel mUserModel;
    private final ContactListViewModel mViewModel;
    private final int mChatID;
    private final boolean mThroughChat;


    public ContactRecyclerViewAdapter(List<Contact> contacts, Context context, FragmentManager fm,
                                      UserInfoViewModel userModel,
                                      ContactListViewModel viewModel,
                                      int chatID,
                                      boolean throughChat) {
        this.mContacts = contacts;
        this.mContext = context;
        this.mFragMan = fm;
        this.mUserModel = userModel;
        this.mViewModel = viewModel;
        this.mChatID = chatID;
        this.mThroughChat = throughChat;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.contact_item, parent, false);
        ContactViewHolder viewHolder = new ContactViewHolder(contactView);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder  {
        private final TextView nameTextView;
        private final TextView usernameTextView;
        private final ImageButton moreButtonView;
        private Contact mContact;

        public ContactViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.contact_name);
            usernameTextView = v.findViewById(R.id.contact_username);
            moreButtonView = v.findViewById(R.id.contact_more_button);
        }



        /**
         * Sets the contact name and username
         * Sets the More Button Popup Menu with its behavior
         * @param contact the contact
         */
        @SuppressLint("NonConstantResourceId")
        @RequiresApi(api = Build.VERSION_CODES.Q)
        private void setContact(final Contact contact) {
            mContact = contact;
            final String name = contact.getFirstName() + " " + contact.getLastName();
            nameTextView.setText(name);
            usernameTextView.setText(contact.getUserName());
            moreButtonView.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
                popupMenu.setForceShowIcon(true);
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.delete_pop_menu) {
                        mViewModel.deleteContact(mUserModel.getJwt(), mContact.getMemberID());
                        this.deleteContact();
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            });
        }

        public void deleteContact(){
            mContacts.remove(mContact);
            notifyDataSetChanged();
        }
    }
}
