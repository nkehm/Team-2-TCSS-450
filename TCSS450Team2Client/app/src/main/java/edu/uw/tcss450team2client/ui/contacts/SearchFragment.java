package edu.uw.tcss450team2client.ui.contacts;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentSearchBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;
import edu.uw.tcss450team2client.ui.contacts.Contact;
import edu.uw.tcss450team2client.ui.contacts.ContactListViewModel;

/**
 * A simple {@link Fragment} subclass for search tab.
 *
 * @author Caleb Chang
 * @version 05/2021
 */
public class SearchFragment extends Fragment {
    private ContactListViewModel mModel;
    private UserInfoViewModel mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mModel.connectGetAllContacts(mUser.getJwt());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentSearchBinding binding = FragmentSearchBinding.bind(getView());
        mModel.addContactListAllObserver(getViewLifecycleOwner(), contactList -> {
                    SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(contactList, mUser, mModel);
                    binding.listRoot.setAdapter(adapter);
                    binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
        );
    }

}