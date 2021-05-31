package edu.uw.tcss450team2client.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450team2client.MainActivity;
import edu.uw.tcss450team2client.MainActivityArgs;
import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentSettingsChangepasswordBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;
import edu.uw.tcss450team2client.utils.PasswordValidator;

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

    private UserInfoViewModel mUserModel;

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
        mUserModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

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
        binding.buttonChange.setOnClickListener(this::attemptChangePassword);
        mChangePasswordModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

    }

    private void attemptChangePassword(final View button) {
        validateOldPassword();
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

                binding.editNewPass.getText().toString(),
                binding.editCurrentPass.getText().toString(),
                mUserModel.getEmail(), mUserModel.getJwt());
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
                    Log.e("Test Log", response.getString("data"));
                    binding.editNewPass.setError("Error Authenticating: " + response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                Log.e("Test Log", "Success");
                //success scenario
                //navigateToLogin();
                ((MainActivity) getActivity()).signOut();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}
