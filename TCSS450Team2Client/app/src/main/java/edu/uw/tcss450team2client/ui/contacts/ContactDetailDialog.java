package edu.uw.tcss450team2client.ui.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

/**
 * Contact Detail Dialog to show user more detail of a contact when they click on a contact card
 * @author Ford Nguyen
 * @version 1.0
 */

public class ContactDetailDialog extends DialogFragment {

    private final Contact mContact;

    private ContactListViewModel mContactModel;

    private final UserInfoViewModel mUserModel;

    private final ContactRecyclerViewAdapter.ContactViewHolder mUpdater;

    private int mChatID;

    private boolean throughChat;

    private Activity activity;

    public ContactDetailDialog(Contact contact, ContactListViewModel contactModel,
                               UserInfoViewModel infoModel,
                               int chatId,
                               boolean throughChat,
                               ContactRecyclerViewAdapter.ContactViewHolder updater) {
        this.mContact = contact;
        this.mContactModel = contactModel;
        this.mUserModel = infoModel;
        this.mChatID = chatId;
        this.throughChat = throughChat;
        mUpdater = updater;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactModel =  new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.contact_detail, null);

        TextView nameText = view.findViewById(R.id.contact_detail_name);
        nameText.setText(mContact.getFirstName() + " " + mContact.getLastName());

        TextView usernameText = view.findViewById(R.id.contact_detail_username);
        usernameText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_contacts_all_24,
                0, 0, 0);
        usernameText.setText(mContact.getUserName());

        TextView emailText = view.findViewById(R.id.contact_detail_email);
        emailText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_email_24,
                0, 0, 0);
        emailText.setText(mContact.getEmail());

        Button deleteButton = view.findViewById(R.id.contact_detail_delete_button);
        deleteButton.setOnClickListener(v -> {
            mContactModel.deleteContact(mUserModel.getJwt(), mContact.getMemberID());
            mUpdater.deleteContact();
            dismiss();
        });

        Button messageButton = view.findViewById(R.id.contact_detail_message_button);
        messageButton.setEnabled(throughChat);
        messageButton.setOnClickListener(v -> {
            putMemberIntoTheRoom();
        });

        mContactModel.addResponseObserver(
                this.getActivity(),
                this::observeAddingContactToChat
        );

        builder.setView(view);
        return builder.create();
    }

    private void observeAddingContactToChat(final JSONObject response) {
        if (response.length() > 0) {
            try {
                openDialog(throughChat, response.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
    }

    private void openDialog(boolean isSuccess, String theMessage) {
        if(isSuccess) {
            Toast.makeText(activity, theMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void putMemberIntoTheRoom(){
        System.out.println(mContact.getMemberID() + " " + mChatID);
        try {
            mContactModel.putContactMembers(mUserModel.getJwt(), mChatID, mContact.getMemberID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
