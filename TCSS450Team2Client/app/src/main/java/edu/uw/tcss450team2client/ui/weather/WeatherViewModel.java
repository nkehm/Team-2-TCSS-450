package edu.uw.tcss450team2client.ui.weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.model.UserInfoViewModel;


public class WeatherViewModel extends AndroidViewModel {

    private MutableLiveData<List<WeatherData>> mWeatherData;

    private MutableLiveData<Map<String, String>> mLocationData;

    private UserInfoViewModel userInfoViewModel;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeatherData = new MutableLiveData<>();
        mLocationData = new MutableLiveData<>();
    }

    public void addWeatherObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<WeatherData>> observer) {
        mWeatherData.observe(owner, observer);
    }

    public void addLocationObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super Map<String, String>> observer) {
        mLocationData.observe(owner, observer);
    }


    private void handleError(final VolleyError error) {
        if (error != null && error.getMessage() != null) {
            Log.e("CONNECTION ERROR", error.getMessage());
            throw new IllegalStateException(error.getMessage());
        }
    }

    private void handleResult(final JSONObject result) {

        if (!result.has("current")) {
            throw new IllegalStateException("Unexpected response in WeatherViewModel: " + result);
        }
        try {
            Map<String, String> location = new HashMap<String, String>();
            JSONObject locationData = result.getJSONObject("location");
            location.put("zip", locationData.getString("zip"));
            location.put("city", locationData.getString("city"));
            location.put("Lat", locationData.getString("latitude"));
            location.put("long", locationData.getString("longitude"));

            mLocationData.setValue(location);
            JSONObject currentData = result.getJSONObject("current");

            JSONObject hour = result.getJSONObject("hourly");
            JSONArray hourArray = hour.getJSONArray("data");

            JSONObject daily = result.getJSONObject("daily");
            JSONArray dailyArray = daily.getJSONArray("data");

            ArrayList<WeatherData> weatherDataList = new ArrayList<WeatherData>();

            weatherDataList.add(new WeatherData("current", 0,
                    currentData.getDouble("temp"), currentData.getString("weather")));

            for (int i = 0; i < hourArray.length(); i++) {
                WeatherData someHour = new WeatherData(
                        "hourly", i, hourArray.getJSONObject(i).getDouble("temp"),
                        hourArray.getJSONObject(i).getString("weather"));
                weatherDataList.add(someHour);
            }

            for (int i = 0; i < dailyArray.length(); i++) {
                WeatherData someDay = new WeatherData(
                        i, dailyArray.getJSONObject(i).getDouble("tempMin"),
                        dailyArray.getJSONObject(i).getDouble("tempMax"),
                        dailyArray.getJSONObject(i).getString("weather"));
                weatherDataList.add(someDay);
            }

            mWeatherData.setValue(weatherDataList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to webservice to get default weather data.
     */
    public void connectGet() {
        if (userInfoViewModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        boolean entered = false;
        String url= getApplication().getResources().getString(R.string.base_url) +
                "weather?zip=98402";

        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                //no body for this get request
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + userInfoViewModel.getJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Connect to web service with zip as parameter
     * @param zip Zip code for place specified.
     */
    public void connectGet(String zip) {
        if (userInfoViewModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://tcss450-team2-server.herokuapp.com/weather?zip=" + zip;

        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                //no body for this get request
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + userInfoViewModel.getJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Connect to web service with latitude and longitude as parameters
     * @param lat latitute string
     * @param lng longitude string
     */
    public void connectGet(String lat, String lng) {
        if (userInfoViewModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://tcss450-team2-server.herokuapp.com/weather?latitude=" + lat +"&longitude=" + lng;

        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                //no body for this get request
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + userInfoViewModel.getJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }


    public void setUserInfoViewModel(UserInfoViewModel vm) {
        userInfoViewModel = vm;
    }
}
