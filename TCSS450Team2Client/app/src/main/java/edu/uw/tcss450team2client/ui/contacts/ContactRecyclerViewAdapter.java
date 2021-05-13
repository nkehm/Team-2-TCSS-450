package edu.uw.tcss450team2client.ui.contacts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450team2client.MainActivity;
import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentContactCardBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder>{

    private final List<Contact> mContacts;
    private RecyclerView mRecyclerView;
    private UserInfoViewModel mUserInfoViewModel;
    private final int mPosition;
    private ContactListViewModel mContactListViewModel;

    public ContactRecyclerViewAdapter(List<Contact> items, int postition, MainActivity mainActivity) {
        this.mContacts = items;
        mPosition = postition;

        // TODO check if not passing Activity will work
        mContactListViewModel = new ViewModelProvider(mainActivity).get(ContactListViewModel.class);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentContactCardBinding binding;
        //Store all of the blogs to present

        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactCardBinding.bind(view);
            binding.cardRoot.setOnClickListener(this::onClick);
        }

        private void onClick(View view) {
            Log.d("Card onclick", "Card has been clicked");
        }

        void setContact(final Contact contact) {
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
