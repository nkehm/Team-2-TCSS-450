package edu.uw.tcss450team2client.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450team2client.MainActivity;
import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentContactListBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment {

    public static final String TAB_POSITION = "position";

    private ContactListViewModel mContactListViewModel;
    private UserInfoViewModel mUserInfoViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactListViewModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mUserInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());
        Bundle mArgs = getArguments();
        mContactListViewModel.getAllContacts(mUserInfoViewModel.getEmail(),mUserInfoViewModel.getJwt());

        if (mArgs.getInt(TAB_POSITION) == 1) {
            mContactListViewModel.addContactListObserver(getViewLifecycleOwner(), connectionList -> binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(connectionList, 1, (MainActivity) ContactListFragment.this.getActivity())));
        }

        if (mArgs.getInt(TAB_POSITION) == 2) {
            mContactListViewModel.addContactListObserver(getViewLifecycleOwner(), connectionList -> binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(connectionList, 2, (MainActivity) ContactListFragment.this.getActivity())));
        }

        if (mArgs.getInt(TAB_POSITION) == 3) {
            mContactListViewModel.addContactListObserver(getViewLifecycleOwner(), connectionList -> binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(connectionList, 3, (MainActivity) ContactListFragment.this.getActivity())));
        }

    }
}