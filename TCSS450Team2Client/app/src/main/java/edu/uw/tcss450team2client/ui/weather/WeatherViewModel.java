package edu.uw.tcss450team2client.ui.weather;

import android.app.Application;
import android.util.Base64;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450team2client.io.RequestQueueSingleton;

public class WeatherViewModel extends AndroidViewModel {

        private MutableLiveData<JSONObject> mResponse;

        public WeatherViewModel(@NonNull Application application) {
            super(application);
            mResponse = new MutableLiveData<>();
            mResponse.setValue(new JSONObject());
        }

        public void addResponseObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super JSONObject> observer) {
            mResponse.observe(owner, observer);
        }

        private void handleError(final VolleyError error) {
            if (Objects.isNull(error.networkResponse)) {
                try {
                    mResponse.setValue(new JSONObject("{" +
                            "error:\"" + error.getMessage() +
                            "\"}"));
                } catch (JSONException e) {
                    Log.e("JSON PARSE", "JSON Parse Error in handleError");
                }
            }
            else {
                String data = new String(error.networkResponse.data, Charset.defaultCharset())
                        .replace('\"', '\'');
                try {
                    JSONObject response = new JSONObject();
                    response.put("code", error.networkResponse.statusCode);
                    response.put("data", new JSONObject(data));
                    mResponse.setValue(response);
                } catch (JSONException e) {
                    Log.e("JSON PARSE", "JSON Parse Error in handleError");
                }
            }
        }

    public void connect(final String zip, final String authToken) {
        String url = "https://tcss450-team2-server.herokuapp.com/weather?zip=" + zip;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                String auth = "Bearer Token "
                        + Base64.encodeToString(authToken.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }



}