package edu.uw.tcss450team2client.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450team2client.MainActivity;
import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentHomeBinding;
import edu.uw.tcss450team2client.model.UserInfoViewModel;
import edu.uw.tcss450team2client.ui.weather.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private WeatherViewModel mWeatherModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mWeatherModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding =  FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentHomeBinding.bind(requireView());

        UserInfoViewModel model = new ViewModelProvider(requireActivity())
                .get(UserInfoViewModel.class);
        binding.textHomeWelcome.setText("Welcome Home " + model.getEmail() + "!");

        mWeatherModel.addWeatherObserver(getViewLifecycleOwner(), weatherList -> {
            if (!weatherList.isEmpty()) {
                binding.textHomeWeatherdata.setText(weatherList.get(0).getWeather()
                        +  ", " + String.format("%.2f", weatherList.get(0).getTemp()) + " F");
                switch (weatherList.get(0).getWeather()) {
                    case "Thunderstorm":
                        binding.imageiconHomeWeather.setImageResource(R.drawable.weather_thunder_art);
                        break;
                    case "Drizzle":
                        binding.imageiconHomeWeather.setImageResource(R.drawable.weather_drizzle_art);
                        break;
                    case "Rain":
                        binding.imageiconHomeWeather.setImageResource(R.drawable.weather_rain_art);
                        break;
                    case "Snow":
                        binding.imageiconHomeWeather.setImageResource(R.drawable.weather_snow_art);
                        break;
                    case "Mist":
                        binding.imageiconHomeWeather.setImageResource(R.drawable.weather_mist_art);
                        break;
                    case "Clear":
                        binding.imageiconHomeWeather.setImageResource(R.drawable.weather_clear_art);
                        break;
                    case "Clouds":
                        binding.imageiconHomeWeather.setImageResource(R.drawable.weather_clouds_art);
                        break;
                }
            }
        });
    }
}