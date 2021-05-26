package edu.uw.tcss450team2client.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentAppearanceBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppearanceFragment extends Fragment {
    private RadioGroup mRadioGroup;
    private FragmentAppearanceBinding binding;

    public AppearanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAppearanceBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // on below line we are setting on check change method for our radio group.
        binding.rgDarkmode.setOnCheckedChangeListener((group, checkedId) -> {
            // on radio button check change
            switch (checkedId) {
                case R.id.rb_darkmode_off:
                    // on below line we are checking the radio button with id.
                    // on below line we are setting the text to text view as light mode.
                    // on below line we are changing the theme to light mode.
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                        setTheme(R.style.Theme_ThemeSwitch);
                    break;
                case R.id.rb_darkmode_on:
                    // this method is called when dark radio button is selected
                    // on below line we are setting dark theme text to our text view.
                    // on below line we are changing the theme to dark mode.
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//                        setTheme(R.style.Theme_custom);
                    break;
            }
        });
    }
}