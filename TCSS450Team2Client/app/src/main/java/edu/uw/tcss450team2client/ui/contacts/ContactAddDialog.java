package edu.uw.tcss450team2client.ui.contacts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

public class ContactAddDialog extends DialogFragment {

    private UserInfoViewModel mUserInfoModel;
    private ContactListViewModel mContactViewModel;
    private TextView userInput;

    public ContactAddDialog(UserInfoViewModel userInfoViewModel,
                            ContactListViewModel contactListViewModel) {
        this.mContactViewModel = contactListViewModel;
        this.mUserInfoModel = userInfoViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.contact_add_dialog, null);

        userInput = view.findViewById(R.id.contact_add_username);
        Button addOkButton = view.findViewById(R.id.contact_add_ok);


        addOkButton.setOnClickListener(v -> {
            mContactViewModel.addFriend(mUserInfoModel.getJwt(), userInput.getText().toString());
            mContactViewModel.addResponseObserver(this.getActivity(),
                    this::observeAddUserResponse);
        });



        builder.setView(view);
        return builder.create();
    }

    /**
     * An observer on the HTTP Response from the web server.
     *
     * @param response the Response from the server
     */
    private void observeAddUserResponse(JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    userInput.setError("Error Adding: " +
                            response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                dismiss();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
