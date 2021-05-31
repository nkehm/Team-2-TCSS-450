package edu.uw.tcss450team2client.ui.auth.register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450team2client.databinding.FragmentRegisterBinding;
import edu.uw.tcss450team2client.utils.PasswordValidator;

import static edu.uw.tcss450team2client.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdMaxLength;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdUpperCase;

/**
 * A fragment that represents the app registration.
 *
 *  * @author Charles Bryan
 *  * @author Nathan Stickler
 *  * @version 5/2021
 */

public class RegisterFragment extends Fragment {

    /**
     * A binding for this fragment.
     */
    private FragmentRegisterBinding binding;

    /**
     * A register view model.
     */
    private RegisterViewModel mRegisterModel;

    /**
     * Name validator.
     */
    private PasswordValidator mNameValidator = checkPwdLength(1);

    /**
     * User name validator.
     */
    private PasswordValidator mUsernameValidator = checkPwdLength(3);

    /**
     * Email validator.
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Password validator.
     */
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * An empty constructor.
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * A helper method to register on button click.
     *
     * @param button register button
     */
    private void attemptRegister(final View button) {
        validateFirst();
    }

    /**
     * Validate first name.
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    /**
     * Validate late name.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateUsername,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    /**
     * Validate user name.
     */
    private void validateUsername() {
        mUsernameValidator.processResult(
                mUsernameValidator.apply(binding.editUsername.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editUsername.setError("Please enter a username."));
    }

    /**
     * Validate email.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * Validate retyped password.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    /**
     * Validate password.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a valid Password."));
    }

    /**
     * Method passes credientials to the server for authentication.
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editUsername.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * Navigate to verification fragment on successful registration.
     */
    private void navigateToVerification() {
        Navigation.findNavController(getView())
                .navigate(RegisterFragmentDirections
                .actionRegisterFragmentToVerificationFragment(binding.editFirst.getText().toString(),
                        binding.editLast.getText().toString(),
                        binding.editEmail.getText().toString(),
                        binding.editPassword1.getText().toString()));
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToVerification();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}
