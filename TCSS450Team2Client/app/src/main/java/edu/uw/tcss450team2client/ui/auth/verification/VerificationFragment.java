package edu.uw.tcss450team2client.ui.auth.verification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450team2client.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerificationFragment extends Fragment {

    /**
     * Empty constructor.
     */
    public VerificationFragment() {
        // Required empty constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        V args = VerifyFragmentArgs.fromBundle(getArguments());

        FragmentVerifyBinding binding = FragmentVerifyBinding.bind(getView());
        binding.editPersonalGreeting.setText("Hello " + args.getFirstName() + " " +
                args.getLastName() + "! Please confirm your email at " + args.getEmail() +
                " so you can start enjoying appName");

        binding.buttonVerify.setOnClickListener(button -> navigateToLogin(args));
    }

    /**
     * Navigation back to login screen.
     *
     * @param args args used update login upon verification.
     */
    private void navigateToLogin(VerifyFragmentArgs args) {
        VerifyFragmentDirections.ActionVerifyFragmentToSignInFragment directions =
                VerifyFragmentDirections.actionVerifyFragmentToSignInFragment();
        directions.setEmail(args.getEmail());
        directions.setPassword(args.getPassword());
        Navigation.findNavController((getView())).navigate(directions);
    }
}