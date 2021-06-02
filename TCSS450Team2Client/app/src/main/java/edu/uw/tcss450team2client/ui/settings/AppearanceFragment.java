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
                case 1:
                    binding.rbThemePurple.setChecked(true);
                    mUserViewModel.setTheme(R.style.Theme_Purple);
                    break;
                case 0:
                default:
                    binding.rbThemeBlue.setChecked(true);
                    mUserViewModel.setTheme(R.style.Theme_Blue);
                    break;
            }
        } else {
            binding.rbThemeBlue.setChecked(true);
        }

        // save theme state
        SharedPreferences mPrefs = this.requireActivity().getSharedPreferences("LIGHTMODE", 0);
        mLightMode = mPrefs.getBoolean("lightMode", true);
        if (mLightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }

        binding.rgDarkmode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_darkmode_off) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                mLightMode = true;
            } else  if (checkedId == R.id.rb_darkmode_on) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                mLightMode = false;
            }
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putBoolean("lightMode", mLightMode).apply();
        });

//        binding.rgThemes.setOnCheckedChangeListener(((group, checkedId) -> {
//            if (checkedId == R.id.rb_theme_blue) {
//            }
//        }));
    }
}