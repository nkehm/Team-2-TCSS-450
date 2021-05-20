package edu.uw.tcss450team2client.ui.auth.signin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentRecoverPasswordBinding;
import edu.uw.tcss450team2client.utils.PasswordValidator;

import static edu.uw.tcss450team2client.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450team2client.utils.PasswordValidator.checkPwdSpecialChar;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecoverPasswordFragment extends Fragment {

    private FragmentRecoverPasswordBinding binding;

    private RecoverPasswordViewModel mRecoverPasswordModel;

    /**
     * Method to validate email.
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Empty public constructor.
     */
    public RecoverPasswordFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecoverPasswordModel = new ViewModelProvider(getActivity())
                .get(RecoverPasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recover_password, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.buttonSendRecover.setOnClickListener(this::attemptRecovery);

        mRecoverPasswordModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

        RecoverPasswordFragmentArgs args = RecoverPasswordFragmentArgs.fromBundle(getArguments());

//        binding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
    }

    /**
     * Creates a dialog box that acknowledges the users input and sends an email request.
     */
    private void forgotPasswordDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.textview_changePassword_headMessage);
        builder.setMessage(R.string.textview_changePassword_description);
        builder.setTitle(R.string.text_forgot_password);
        builder.setPositiveButton(R.string.button_recoverPassword_send, (dialog, which) -> {
            Log.d("Recovery", "Acknowledge");
            binding.buttonSendRecover.setOnClickListener(this::attemptRecovery);
        });
        builder.setNegativeButton(R.string.button_recoverPassword_cancel, (dialog, which) -> {
            Log.d("Recovery", "Cancel");
        });
        builder.create();
        builder.show();
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to  PasswordRecoveryViewModel
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            //don't want the client "user" to know if the email was correct or not?
            //email was or wasn't send, we want to create a dialog possible success dialog
            forgotPasswordDialogue();

        } else {
            Log.d("JSON Response", "No Response");

        }
    }

    /**
     * Navigates the user to login fragment.
     */
    private void navigateToLogin() {
        RecoverPasswordFragmentDirections.ActionFragmentRecoverPasswordToSignInFragment directions =
                RecoverPasswordFragmentDirections.actionFragmentRecoverPasswordToSignInFragment();

        directions.setEmail(binding.editEmail.getText().toString());
        Navigation.findNavController(getView()).navigate(directions);
    }

    /**
     * Helper method to being sequential validation process.
     *
     * @param button login button.
     */
    private void attemptRecovery(final View button) {
        Log.d("Recovery", "User selected to recover password");
        validateEmail();
    }

    /**
     * Helper method to validate email.  Sets error message
     * if incorrect input is given.  Calls validate password method.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::sendRecoveryRequest,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }
    /**
     * Method to send recovery request.
     */
    private void sendRecoveryRequest() {
        //recover the users password
        mRecoverPasswordModel.connect(binding.editEmail.getText().toString());
    }
}