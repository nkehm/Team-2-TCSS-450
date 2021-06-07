package edu.uw.tcss450team2client.ui.weather;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import edu.uw.tcss450team2client.MainActivity;
import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentWeatherListBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherListFragment extends Fragment {
    /**
     * WeatherViewModel object.
     */
    private WeatherViewModel mModel;
    /**
     * FragmentWeatherListBinding object.
     */
    private FragmentWeatherListBinding binding;
    /**
     * List of Weather data for hourly weather.
     */
    private List<WeatherData> hourList;
    /**
     * List of Weather data for daily weather.
     */
    private List<WeatherData> dayList;

    private FusedLocationProviderClient fusedLocationClient;

    private double lat = -404;
    private double lng = -404;
    private boolean nope = false;
    private boolean check = false;

    /**
     * Empty public constructor.
     */
    public WeatherListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null&& nope == false && check == false) {
                                lat = location.getLatitude();
                                lng = location.getLongitude();

                                if(lat != 0 && lng != 0 && nope == false && WeatherMapFragment.check() == false){
                                    mModel.connectGet(String.valueOf(lat),String.valueOf(lng));
                                }

                            }
                        }
                    });
        }else{
                Boolean nope = true;
        }


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherListBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentWeatherListBinding binding = FragmentWeatherListBinding.bind(getView());
        binding.buttonSearch.setOnClickListener(this::searchZip);
        binding.buttonMap.setOnClickListener(this::searchMap);
        if(WeatherMapFragment.check() == true){
            check = true;
            mModel.connectGet(WeatherMapFragment.getLat(),WeatherMapFragment.getLng());
        }

        mModel.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (!location.isEmpty()) {
                binding.textviewZipData.setText(location.get("zip"));
                binding.textviewLocationData.setText(location.get("city"));
                // TODO: Structure supports displaying longitude/latitude as well.
                //  It is in locationData
            }
        });

        mModel.addWeatherObserver(getViewLifecycleOwner(), weatherList -> {
            if (!weatherList.isEmpty()) {

                hourList = weatherList.subList(1, 25);
                dayList = weatherList.subList(25, 30);

                binding.recyclerViewToday.setAdapter(new WeatherRecyclerViewAdapter(hourList));
                binding.recyclerViewWeekly.setAdapter(new WeatherWeekRecyclerViewAdapter(dayList));
                binding.textviewCurrentData.setText(weatherList.get(0).getWeather() +
                        ", " + String.format("%.2f", weatherList.get(0).getTemp()) + " F");
                if (weatherList.get(0).getWeather().equals("Thunderstorm")){
                    binding.imageiconWeatherIcon.setImageResource(R.drawable.weather_thunder_art);
                } else if (weatherList.get(0).getWeather().equals("Drizzle")){
                    binding.imageiconWeatherIcon.setImageResource(R.drawable.weather_drizzle_art);
                } else if (weatherList.get(0).getWeather().equals("Rain")){
                    binding.imageiconWeatherIcon.setImageResource(R.drawable.weather_rain_art);
                }else if (weatherList.get(0).getWeather().equals("Snow")){
                    binding.imageiconWeatherIcon.setImageResource(R.drawable.weather_snow_art);
                } else if (weatherList.get(0).getWeather().equals("Mist")){
                    binding.imageiconWeatherIcon.setImageResource(R.drawable.weather_mist_art);
                } else if (weatherList.get(0).getWeather().equals("Clear")){
                    binding.imageiconWeatherIcon.setImageResource(R.drawable.weather_clear_art);
                } else if (weatherList.get(0).getWeather().equals("Clouds")){
                    binding.imageiconWeatherIcon.setImageResource(R.drawable.weather_clouds_art);
                }
            }
        });
    }

    private void searchZip(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        nope = true;
        String s = binding.textviewZipData.getText().toString();
        mModel.connectGet(s);
    }

    private void searchMap(View view) {
        Navigation.findNavController(getView()).navigate(WeatherListFragmentDirections.actionWeatherListFragmentToWeatherMapFragment());
    }
}
