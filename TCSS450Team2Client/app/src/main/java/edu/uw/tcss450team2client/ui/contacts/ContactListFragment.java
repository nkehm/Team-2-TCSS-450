package edu.uw.tcss450team2client.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.uw.tcss450team2client.MainActivity;
import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentContactListBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

public class ContactListFragment extends Fragment  {

    private ContactListViewModel mModel;
    private UserInfoViewModel mInfoModel;
    private int mChatID;
    private boolean mThroughChat;



    public ContactListFragment() {
        // empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);

        mInfoModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        /*Chat id*/
//        if(getArguments() != null) {
//            ContactListFragmentArgs args = ContactListFragmentArgs.fromBundle(getArguments());
//            mChatID = args.getChatid();
//            mThroughChat = args.getThroughChat();
//        }

        mModel.connectGet(mInfoModel.getJwt());

//        mModel.connectPusher(mInfoModel.getJwt(), mInfoModel.getEmail());
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

//        FloatingActionButton fab = view.findViewById(R.id.contact_add_float_button);
//
        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(contactList, this.getContext(),
                            getChildFragmentManager(), mInfoModel, mModel, mChatID, mThroughChat));
//            fab.setOnClickListener(v -> {
//                ContactAddDialog dialog = new ContactAddDialog(mInfoModel, mModel);
//                dialog.show(getChildFragmentManager(), "add");
//            });

        });
    }
}