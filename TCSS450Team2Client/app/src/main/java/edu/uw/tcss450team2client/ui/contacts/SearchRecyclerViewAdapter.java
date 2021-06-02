package edu.uw.tcss450team2client.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.model.UserInfoViewModel;
import edu.uw.tcss450team2client.ui.contacts.Contact;
import edu.uw.tcss450team2client.ui.contacts.ContactListViewModel;

/**
 * A recycler view for the list in search tab.
 *
 * @author Caleb Chang
 * @version 05/2021
 */
public class SearchRecyclerViewAdapter  extends
        RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder> implements Filterable {

    private List<Contact> mContacts;
    private List<Contact> mContactsFull;
    private UserInfoViewModel mUserModel;
    private ContactListViewModel mViewModel;
    private View searchView;


    public SearchRecyclerViewAdapter(List<Contact> contacts, UserInfoViewModel userModel,
                                     ContactListViewModel viewModel) {
        this.mContacts = contacts;
        this.mContactsFull = new ArrayList<>(contacts);
        searchFilter.filter(null);
        this.mUserModel = userModel;
        this.mViewModel = viewModel;
    }


    /**
     * Constructor that builds the recycler view adapter use all the contact items.
     * @param parent
     * @param viewType
     * @return
     */
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        searchView = inflater.inflate(R.layout.contact_search_item, parent, false);
        return new SearchViewHolder(searchView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Contact currentItem = mContacts.get(position);
        holder.usernameTextView.setText(currentItem.getEmail());
        holder.nameTextView.setText(currentItem.getFirstName() + " " + currentItem.getLastName());
        holder.addButton.setOnClickListener(v -> {
            holder.addButton.setVisibility(View.GONE);
            mViewModel.addFriend(mUserModel.getJwt(), mContacts.get(position).getUserName());
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    /**
     * Create a search list holder
     */
    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView usernameTextView;
        private final ImageButton addButton;
        private Contact mContact;

        public SearchViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.contact_name);
            usernameTextView = view.findViewById(R.id.contact_username);
            addButton = view.findViewById(R.id.contact_search_button);
        }
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                filteredList.clear();
                return results;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Contact contact : mContactsFull) {
                    String wholeName = contact.getFirstName().toLowerCase() + " " +
                            contact.getLastName().toLowerCase();
                    if (wholeName.contains(filterPattern)) {
                        filteredList.add(contact);
                    }
                }
            }
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mContacts.clear();
            if (results.values != null) {
                mContacts.addAll((List<Contact>) results.values);
            }
            notifyDataSetChanged();
        }
    };
}

