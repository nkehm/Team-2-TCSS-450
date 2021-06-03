package edu.uw.tcss450team2client.ui.weather;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.List;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentWeatherWeekCardBinding;


public class WeatherWeekRecyclerViewAdapter extends RecyclerView.Adapter<edu.uw.tcss450team2client.ui.weather.WeatherWeekRecyclerViewAdapter.WeatherViewHolder> {
    /**
     * List of type WeatherData.
     */
    List<edu.uw.tcss450team2client.ui.weather.WeatherData> mData;
    /**
     * Constructor that instantiates fields.
     * @param data
     */
    public WeatherWeekRecyclerViewAdapter(List<edu.uw.tcss450team2client.ui.weather.WeatherData> data) {
        this.mData = data;

    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder((LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_weather_week_card, parent, false)));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.setWeather(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Helper class that creates view holder. 
     */
    class WeatherViewHolder extends RecyclerView.ViewHolder {
        /**
         * View object. 
         */
        private final View mView;
        /**
         * FragmentWeatherCardBinding object. 
         */
        private FragmentWeatherWeekCardBinding binding;
         /**
          * Constructor that instantiantes fields. 
          * @param view 
          */
        public WeatherViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentWeatherWeekCardBinding.bind(view);
        }
         /**
          * Method that sets weather data.
          */
         @RequiresApi(api = Build.VERSION_CODES.O)
         void setWeather(final edu.uw.tcss450team2client.ui.weather.WeatherData data) {
             LocalDate today = LocalDate.now();
            binding.textviewTime.setText(today.plusDays(data.getIncrement()).getDayOfWeek().name());
             String weatherType = data.getWeather();
             if (weatherType.equals("Thunderstorm")){
                 binding.imageiconWeatherIconWeek.setImageResource(R.drawable.weather_thunder_art);
             } else if (weatherType.equals("Drizzle")){
                 binding.imageiconWeatherIconWeek.setImageResource(R.drawable.weather_drizzle_art);
             } else if (weatherType.equals("Rain")){
                 binding.imageiconWeatherIconWeek.setImageResource(R.drawable.weather_rain_art);
             }else if (weatherType.equals("Snow")){
                 binding.imageiconWeatherIconWeek.setImageResource(R.drawable.weather_snow_art);
             } else if (weatherType.equals("Mist")){
                 binding.imageiconWeatherIconWeek.setImageResource(R.drawable.weather_mist_art);
             } else if (weatherType.equals("Clear")){
                 binding.imageiconWeatherIconWeek.setImageResource(R.drawable.weather_clear_art);
             } else if (weatherType.equals("Clouds")){
                 binding.imageiconWeatherIconWeek.setImageResource(R.drawable.weather_clouds_art);
             }


            if (data.getTemp() == -1|| data.getTemp() < -459) {
                binding.textviewLowTemp.setText(String.format("%.2f", data.getTempMin()));
                binding.textviewHighTemp.setText(String.format("%.2f", data.getTempMax()));
            } else {
                binding.textviewHighTemp.setText(String.format("%.2f", data.getTemp()));
            }
        }
    }
}
