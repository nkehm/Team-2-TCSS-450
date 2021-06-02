package edu.uw.tcss450team2client.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.Objects;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentAppearanceBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppearanceFragment extends Fragment {
    private FragmentAppearanceBinding binding;
    private Boolean mLightMode;

    public AppearanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        binding.rgThemes.setOnCheckedChangeListener(((group, checkedId) -> {
            if (checkedId == R.id.rb_theme_blue) {
            }
        }));
    }
}