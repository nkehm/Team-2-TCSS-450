package edu.uw.tcss450team2client.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.Objects;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentAppearanceBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppearanceFragment extends Fragment {
    private FragmentAppearanceBinding binding;
    private UserInfoViewModel mUserViewModel;
    private Boolean mLightMode;

    public AppearanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = (new ViewModelProvider(getActivity())).get(UserInfoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAppearanceBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);

        if (prefs.contains(getString(R.string.keys_prefs_themes))) {
            int theme = prefs.getInt(getString(R.string.keys_prefs_themes), -1);

            switch (theme) {
                case 0:
                default:
                    binding.rbThemeLightblue.setChecked(true);
                    mUserViewModel.setTheme(R.style.Theme_LightBlue);
                    break;
                case 1:
                    binding.rbThemeIndigo.setChecked(true);
                    mUserViewModel.setTheme(R.style.Theme_Indigo);
                    break;
                case 2:
                    binding.rbThemeTeal.setChecked(true);
                    mUserViewModel.setTheme(R.style.Theme_Teal);
                    break;
            }
        } else {
            binding.rbThemeLightblue.setChecked(true);
        }

        if (prefs.contains(getString(R.string.keys_prefs_modes))) {
            int darkMode = prefs.getInt(getString(R.string.keys_prefs_modes), -1);

            if (darkMode == 0) {
                binding.rbDarkmodeOff.setChecked(true);
                mUserViewModel.setDMode(0);
            } else if (darkMode == 1) {
                binding.rbDarkmodeOn.setChecked(true);
                mUserViewModel.setDMode(1);
            } else {
                binding.rbDarkmodeOff.setChecked(true);
            }
        }

    }
}