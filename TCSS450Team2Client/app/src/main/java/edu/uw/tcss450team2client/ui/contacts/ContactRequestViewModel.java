package edu.uw.tcss450team2client.ui.contacts;

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

/**
 * A contact request view model
 *
 * @author Caleb Chang
 * @version 05/2021
 */
public class ContactRequestViewModel extends AndroidViewModel {

    private MutableLiveData<List<FriendRequest>> mRequestList;
    private final MutableLiveData<JSONObject> mResponse;

    /**
     * Constructor for ContactRequestViewModel
     *
     * @param application
     */
    public ContactRequestViewModel(@NonNull Application application) {
        super(application);
        RequestGenerator requestGenerator = new RequestGenerator();

        mRequestList = new MutableLiveData<>(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Observer
     *
     * @param owner    LifecycleOwneer
     * @param observer Observer List FriendRequest
     */
    public void addRequestListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<FriendRequest>> observer) {
        mRequestList.observe(owner, observer);
    }

    /**
     * Connect to webserver by sending HTTP request to get the contact request
     * @param jwt User's jwt
     */
    public void connectGet(final String jwt) {
        String url = "https://tcss450-team2-server.herokuapp.com/contacts/requestlist";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSuccess,
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Handle task when success getting response from server
     * @param result JSON object from server
     */
    private void handleSuccess(final JSONObject result) {
        ArrayList<FriendRequest> temp = new ArrayList<>();
        try {
            JSONArray requests = result.getJSONArray("request");
            for (int i = 0; i < requests.length(); i++) {

                JSONObject request = requests.getJSONObject(i);

                String username = request.getString("username");

                int memberID = request.getInt("memberid");

                FriendRequest entry = new FriendRequest(username, memberID);

                temp.add(entry);
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactRequestListViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mRequestList.setValue(temp);
    }


    /**
     * handle a failure connection to the back-end
     * @param error the error.
     */
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", "No contacts");
    }
}