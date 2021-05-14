package edu.uw.tcss450team2client.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentContactFavoriteListBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFavoriteListFragment extends Fragment {

    private ContactListViewModel mModel;
    private UserInfoViewModel mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_favorite_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);

        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mModel.connectGetFavorite(mUser.getJwt());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactFavoriteListBinding binding = FragmentContactFavoriteListBinding
                .bind(getView());

        mModel.addFavoriteListObserver(getViewLifecycleOwner(), contactList ->
                binding.listRoot.setAdapter(
                        new ContactFavoriteRecyclerViewAdapter(contactList, this.getContext(),
                                mUser, mModel)
                ));
    }
}