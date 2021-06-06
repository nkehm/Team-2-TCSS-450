package edu.uw.tcss450team2client.ui.chat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uw.tcss450team2client.MainActivity;
import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentAddChatBinding;
import edu.uw.tcss450team2client.databinding.FragmentContactBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;
import edu.uw.tcss450team2client.ui.contacts.Contact;
import edu.uw.tcss450team2client.ui.contacts.ContactFragment;
import edu.uw.tcss450team2client.ui.contacts.ContactListFragment;
import edu.uw.tcss450team2client.ui.contacts.ContactListViewModel;
import edu.uw.tcss450team2client.ui.contacts.ContactRecyclerViewAdapter;
import edu.uw.tcss450team2client.ui.contacts.ContactRequestRecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddChatFragment extends Fragment implements View.OnClickListener {

    private ChatListViewModel mModel;
    private FragmentAddChatBinding binding;
    private ArrayList<String> userNames;
    private ContactListViewModel mContactListModel;
    private AddChatViewModel mAddChatViewModel;
    private UserInfoViewModel mInfoModel;

    public AddChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }

        mContactListModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mInfoModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mContactListModel.connectGet(mInfoModel.getJwt());
        mAddChatViewModel = new ViewModelProvider(getActivity()).get(AddChatViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddChatBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        mContactListModel.addContactListObserver(getViewLifecycleOwner(), contacts -> {
            binding.listRoot.setLayoutManager(manager);
            binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(contacts, this.getContext(),
                            getChildFragmentManager(), mInfoModel, mContactListModel, mAddChatViewModel));
            Log.d("Contact list in chat", "Setting adapter");
        });

        mAddChatViewModel.addChatUserListObserver(getViewLifecycleOwner(), chatUser -> {
            binding.editTextEnterUser.setText(chatUser);
        });

        binding.editTextEnterChatNameAddchatfragment.setOnClickListener(this);
        binding.imageButtonAddChatAddchatfragment.setOnClickListener(this);
        binding.imageButtonClearChatAddchatfragment.setOnClickListener(this);
    }

    public Boolean chatNameValidation(String str) {


        if (str.equals("")) {
            binding.editTextEnterChatNameAddchatfragment.setError("Not a valid chat name.  No input was provided");
            return false;
        }
        return true;
    }

    public Boolean usernameValidation(List<String> username) {
        for (int i = 0; i < username.size(); i++) {

            if (username.get(i).equals("")) {
                binding.editTextEnterUser.setError("One or more blank username(s)");
                binding.editTextEnterUser.requestFocus();
                return false;
            } else if (username.get(i).length() > 32 || username.get(i).length() < 4) {
                binding.editTextEnterUser.setError("Valid usernames are 4-32 characters long");
                binding.editTextEnterUser.requestFocus();
                return false;
            } else if (!username.get(i).matches("^\\w+$")) {
                binding.editTextEnterUser.setError("Usernames must be alphanumeric");
                binding.editTextEnterUser.requestFocus();
                return false;
            }
        }
        binding.editTextEnterUser.setError(null);
        return true;
    }

    public List<String> parseUsername(String str) {
        userNames = new ArrayList<String>(Arrays.asList(str.trim().split("\\s*,\\s*")));
        return userNames;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.imageButtonClearChatAddchatfragment) {
            mAddChatViewModel.clearText();
        } else if (chatNameValidation(binding.editTextEnterChatNameAddchatfragment.getText().toString()) &&
                usernameValidation(parseUsername(binding.editTextEnterUser.getText().toString()))) {

            if (v == binding.imageButtonAddChatAddchatfragment) {
                mModel.connectAddChat(
                        binding.editTextEnterChatNameAddchatfragment.getText().toString(), userNames);

                Context context = getContext();
                CharSequence text = "You've Added A Chat!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Navigation.findNavController(getView()).navigate(AddChatFragmentDirections.actionAddChatFragmentToNavigationChat());
            }
        }
    }
}

