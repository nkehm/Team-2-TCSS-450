package edu.uw.tcss450team2client.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentRegisterBinding;
import edu.uw.tcss450team2client.databinding.FragmentWeatherBinding;
import edu.uw.tcss450team2client.ui.auth.register.RegisterViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;
    private WeatherViewModel mWeatherViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherViewModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textViewSummary = binding.textViewSummary;
        textViewSummary.setText("Current Weather: 75°f");

    }
    /*private void verifyAuthWithServer() {
        mWeatherViewModel.connect("98444",
                );
    }*/

}