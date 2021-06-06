package edu.uw.tcss450team2client.ui.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.ContactItemBinding;
import edu.uw.tcss450team2client.databinding.FragmentAddChatBinding;
import edu.uw.tcss450team2client.databinding.FragmentContactRequestCardBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;
import edu.uw.tcss450team2client.ui.chat.AddChatFragment;
import edu.uw.tcss450team2client.ui.chat.AddChatViewModel;
import edu.uw.tcss450team2client.ui.chat.ChatListViewModel;

/**
 * A recycler view for the contact list.
 *
 * @version 05/2021
 */
public class ContactRecyclerViewAdapter extends
        RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mContacts;
    private final Context mContext;
    private final FragmentManager mFragMan;
    private final UserInfoViewModel mUserModel;
    private final ContactListViewModel mViewModel;
    private AddChatViewModel mAddChatViewModel;


    public ContactRecyclerViewAdapter(List<Contact> contacts, Context context, FragmentManager fm,
                                      UserInfoViewModel userModel,
                                      ContactListViewModel viewModel, AddChatViewModel mAddChatViewModel) {
        this.mContacts = contacts;
        this.mContext = context;
        this.mFragMan = fm;
        this.mUserModel = userModel;
        this.mViewModel = viewModel;
        this.mAddChatViewModel = mAddChatViewModel;
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
        holder.setClickPassUsername(mContacts.get(position));

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * Create a contact list holder
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder  {
        private final TextView nameTextView;
        private final TextView usernameTextView;
        private final ImageButton moreButtonView;
        private Contact mContact;
        private ContactItemBinding binding;

        public ContactViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.contact_name);
            usernameTextView = v.findViewById(R.id.contact_username);
            moreButtonView = v.findViewById(R.id.contact_more_button);
            binding = ContactItemBinding.bind(v);
        }

        /**
         * Pass contact to Add chat view model to update the add user text edit
         * @param mContacts Contact to pass
         */
        private void setClickPassUsername(Contact mContacts) {
            binding.layoutInner.setOnClickListener(v -> {
                Log.d("Contact list", mContacts.getUserName() + " Contact card clicked");
                mAddChatViewModel.updateContactListText(mContacts.getUserName());
            });
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
                        // delete from database
                        mViewModel.deleteContact(mUserModel.getJwt(), mContact.getMemberID());
                        this.deleteContact();
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            });
        }

        /**
         * Delete contact from recycler view
         */
        public void deleteContact(){
            mContacts.remove(mContact);
            notifyDataSetChanged();
        }
    }
}
