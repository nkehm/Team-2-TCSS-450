package edu.uw.tcss450team2client.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Calendar;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.databinding.FragmentWeatherCardBinding;


public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<edu.uw.tcss450team2client.ui.weather.WeatherRecyclerViewAdapter.WeatherViewHolder> {
    /**
     * List of type WeatherData.
     */
    List<edu.uw.tcss450team2client.ui.weather.WeatherData> mData;
    /**
     * Constructor that instantiates fields. 
     * @param data
     */
    public WeatherRecyclerViewAdapter(List<edu.uw.tcss450team2client.ui.weather.WeatherData> data) {
        this.mData = data;

    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder((LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_weather_card, parent, false)));
    }

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
        private FragmentWeatherCardBinding binding;
         /**
          * Constructor that instantiantes fields. 
          * @param view 
          */
        public WeatherViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentWeatherCardBinding.bind(view);
        }
         /**
          * Method that sets weather data.
          */
        void setWeather(final edu.uw.tcss450team2client.ui.weather.WeatherData data) {
            int hour = (Calendar.getInstance().getTime().getHours() + data.getIncrement()) % 24;


            binding.textviewTime.setText(hour + ":00");
            //binding.textviewType.setText(data.getWeather());

            String weatherType = data.getWeather();
            if (weatherType.equals("Thunderstorm")){
                binding.imageiconWeatherIconList.setImageResource(R.drawable.weather_thunder_art);
            } else if (weatherType.equals("Drizzle")){
                binding.imageiconWeatherIconList.setImageResource(R.drawable.weather_drizzle_art);
            } else if (weatherType.equals("Rain")){
                binding.imageiconWeatherIconList.setImageResource(R.drawable.weather_rain_art);
            }else if (weatherType.equals("Snow")){
                binding.imageiconWeatherIconList.setImageResource(R.drawable.weather_snow_art);
            } else if (weatherType.equals("Mist")){
                binding.imageiconWeatherIconList.setImageResource(R.drawable.weather_mist_art);
            } else if (weatherType.equals("Clear")){
                binding.imageiconWeatherIconList.setImageResource(R.drawable.weather_clear_art);
            } else if (weatherType.equals("Clouds")){
                binding.imageiconWeatherIconList.setImageResource(R.drawable.weather_clouds_art);
            }

            if (data.getTemp() == -1|| data.getTemp() < -459) {
                binding.textviewHighTemp.setText(String.format("%.2f", data.getTempMax()));
            } else {
                binding.textviewHighTemp.setText(String.format("%.2f", data.getTemp()));
            }
        }
    }
}
