package edu.uw.tcss450team2client.ui.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentSettingsChangepasswordBinding;
import edu.uw.tcss450team2client.utils.PasswordValidator;

import static android.widget.Toast.LENGTH_LONG;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdUpperCase;

/**
 * A fragment for changing password in app.
 *
 * @author Nathan Stickler
 * @version 5/2021
 */
public class ChangePasswordFragment extends Fragment {

    /**
     * Stores FragmentSettingsChangepasswordBinding variable.
     */
    private FragmentSettingsChangepasswordBinding binding;

    /**
     * Stores mChangePasswordModel variable.
     */
    private ChangePasswordViewModel mChangePasswordModel;

    /**
     * Method to validate the old password.
     */
    private PasswordValidator mOldPasswordValidator = checkPwdLength(6)
            .and(checkPwdSpecialChar())
            .and(checkExcludeWhiteSpace())
            .and(checkPwdDigit())
            .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * Method to validate the new password.
     */
    private PasswordValidator mNewPasswordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editNewPass.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    private String mEmail;

    /**
     * Empty public constructor.
     */
    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiates a ChangePasswordFragmentViewModel when register fragment is created.
     *
     * @param savedInstanceState reconstructed fragment from previous saved state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangePasswordModel = new ViewModelProvider(getActivity())
                .get(ChangePasswordViewModel.class);
    }

    /**
     * Inflates RegisterFragment layout and instantiates ChangePasswordFragment binding when created.
     *
     * @param inflater           object to inflate any view in layout.
     * @param container          parent view fragment UI is attached to.
     * @param savedInstanceState reconstructed fragment from previous saved state.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsChangepasswordBinding.inflate(inflater, container, false);

        ChangePasswordFragmentArgs args = ChangePasswordFragmentArgs.fromBundle(getArguments());
        mEmail = args.getEmail().equals("default") ? "" : args.getEmail();

        return binding.getRoot();
    }

    /**
     * When view is created the register fragment allows user to add input into text fields.
     * An observer checks for proper credentials before allowing user to register account.
     *
     * @param view               returned by onCreateView.
     * @param savedInstanceState reconstructed fragment from previous saved state.
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        mChangePasswordModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

    }

    /**
     * Helper method to  validate OldPassword.  Sets error message if
     * incorrect input is given.  Calls method to validate new password
     */
    private void validateOldPassword() {
        mOldPasswordValidator.processResult(
                mOldPasswordValidator.apply(binding.editCurrentPass.getText().toString()),
                this::validateNewPassword,
                result -> binding.editCurrentPass.setError("Please enter a valid Password."));
    }

    /**
     * Helper method to  validate password.  Sets error message if
     * incorrect input is given.  Calls method to verify and authenticate credentials.
     */
    private void validateNewPassword() {
        mNewPasswordValidator.processResult(
                mNewPasswordValidator.apply(binding.editNewPass.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editNewPass.setError("Please enter a valid Password."));
    }


    /**
     * Method passes credentials to server for authentication.
     */
    private void verifyAuthWithServer() {
        mChangePasswordModel.connect(

                mEmail,
                binding.editCurrentPass.getText().toString(),
                binding.editNewPass.getText().toString());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to ChangePasswordFragmentVViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editNewPass.setError("Error Authenticating: " + response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                //success scenario
                //navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

//    private EditText mEmail;
//    private EditText mCurrentPassword;
//    private EditText mNewPassword;
//    private EditText mRetypeNewPassword;
//
//    ConstraintLayout ChangePassword;
//
//    private ChangePasswordViewModel mChangePasswordViewModel;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mChangePasswordViewModel = new ViewModelProvider(getActivity())
//                .get(ChangePasswordViewModel.class);
//
////        ChangePassDialog = binding.settingsLayout;
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mChangePasswordViewModel.addResponseObserver(getViewLifecycleOwner(),
//                this::observeResponse);
//
//
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_settings_changepassword, null);
//        builder.setView(view)
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                    }
//                })
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        String email = mEmail.getText().toString();
//                        String currentPass = mCurrentPassword.getText().toString();
//                        String newPass = mNewPassword.getText().toString();
//                        String retypeNewPass = mRetypeNewPassword.getText().toString();
//                        System.out.println(email);
//                        System.out.println(currentPass);
//                        System.out.println(newPass);
//                        System.out.println((retypeNewPass));
//                        mChangePasswordViewModel.connect(mEmail.getText().toString(),
//                                mCurrentPassword.getText().toString(),
//                                mNewPassword.getText().toString());
//
//
//                        Toast.makeText(getActivity(), "Processing change process request" , LENGTH_LONG).show();
//                        System.out.println("IN HERE");
////                        listener.applyTexts(email);
//                    }
//                });
//        mEmail = view.findViewById(R.id.edit_email);
//        mCurrentPassword = view.findViewById(R.id.edit_current_pass);
//        mNewPassword = view.findViewById(R.id.edit_new_pass);
//        return builder.create();
//    }
//
//    /**
//     * An observer on the HTTP Response from the web server. This observer should be
//     * attached to SignInViewModel.
//     *
//     * @param response the Response from the server
//     */
//    private void observeResponse(final JSONObject response) {
//        if (response.length() > 0) {
//            if (response.has("code")) {
//                System.out.println("Failed to send email");
//            }
//        } else {
//            Log.d("JSON Response", "No Response");
//        }
//    }
}
