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
    private RadioGroup mRadioGroup;
    private FragmentAppearanceBinding binding;
    private Boolean dark_boolean;

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
        SharedPreferences mPrefs = this.requireActivity().getSharedPreferences("DARKMODE", 0);
        dark_boolean = mPrefs.getBoolean("dark_boolean", false);
        if (dark_boolean) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // on below line we are setting on check change method for our radio group.
        binding.rgDarkmode.setOnCheckedChangeListener((group, checkedId) -> {
            // on radio button check change
            if (checkedId == R.id.rb_darkmode_off) {
                // on below line we are checking the radio button with id.
                // on below line we are setting the text to text view as light mode.
                // on below line we are changing the theme to light mode.
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                dark_boolean = false;
//                        setTheme(R.style.Theme_ThemeSwitch);
            } else  if (checkedId == R.id.rb_darkmode_on) {
                    // this method is called when dark radio button is selected
                    // on below line we are setting dark theme text to our text view.
                    // on below line we are changing the theme to dark mode.
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    dark_boolean = true;
//                        setTheme(R.style.Theme_custom);
            }
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putBoolean("dark_boolean", dark_boolean).apply();
        });
    }
}