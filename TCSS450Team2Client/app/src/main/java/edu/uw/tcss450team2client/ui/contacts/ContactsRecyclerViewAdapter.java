package edu.uw.tcss450team2client.ui.contacts;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentContactCardBinding;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactsViewHolder>{

    private final List<Contact> mContacts;

    public ContactsRecyclerViewAdapter(List<Contact> items) {
        this.mContacts = items;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private Contact mContact;
        public FragmentContactCardBinding binding;
        //Store all of the blogs to present

        public ContactsViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactCardBinding.bind(view);
//            binding.buittonMore.setOnClickListener(this::handleMoreOrLess);
//            mView.setOnClickListener(this::handleMoreOrLess);
        }


        void setContact(final Contact contact) {
            mContact = contact;
            // For contact profile
//            binding.textContactUsername.setOnClickListener(view -> {
//                Navigation.findNavController(mView).navigate(
//                        ContactsFragmentDirections
//                                .actionNavigationBlogsToBlogPostFragment(blog));
//            });
            binding.textContactName.setText(contact.getFirstName());
            binding.textContactUsername.setText(contact.getUserName());
        }
    }

}
