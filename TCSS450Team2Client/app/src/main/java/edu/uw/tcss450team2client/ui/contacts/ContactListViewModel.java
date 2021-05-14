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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.io.RequestQueueSingleton;

public class ContactListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mFriends;
    private List<Contact> mFriendHandler = new ArrayList<Contact>();
    private MutableLiveData<List<Contact>> mPendings;
    private List<Contact> mPendingHandler = new ArrayList<Contact>();
    private MutableLiveData<List<Contact>> mInvites;
    private List<Contact> mInviteHandler = new ArrayList<Contact>();
    private String mJwt;
    private String mEmail;


    public ContactListViewModel(@NonNull Application application) {
        super(application);

        ContactGenerator contactGenerator = new ContactGenerator();
        // Setup MutableLiveData for all the fields that are being used in Contact List
        mFriends = new MutableLiveData<>();
        mFriends.setValue(new ArrayList<>());

        // Set local dummy data for testing
//        mFriends.setValue(contactGenerator.getContactList());

        mPendings = new MutableLiveData<>();
        mPendings.setValue(new ArrayList<>());

        mInvites = new MutableLiveData<>();
        mInvites.setValue(new ArrayList<>());
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<Contact>> observer) {
        mFriends.observe(owner, observer);
    }

    public void addPendingObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Contact>> observer) {
        mPendings.observe(owner, observer);
    }

    public void adInviteObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Contact>> observer) {
        mInvites.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }

    private void handleResult(final JSONObject result) {
        try {
            JSONArray tempJSONArray = result.getJSONArray("contacts");
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < tempJSONArray.length(); i++) {
                temp.add(tempJSONArray.getJSONObject(i).getString("email"));
                temp.add(tempJSONArray.getJSONObject(i).getString("firstname"));
                temp.add(tempJSONArray.getJSONObject(i).getString("lastname"));
                temp.add(tempJSONArray.getJSONObject(i).getString("username"));
            }
            for (int i = 0; i < temp.size(); i += 4) {
                mFriendHandler.add(new Contact(temp.get(i), temp.get(i + 1), temp.get(i + 2), temp.get(i + 3)));
            }
            if (mFriendHandler.equals(mFriends)) { // If equal, then not update the data to prevent scrolling issue - Hung Vu
                return;
            }
            mFriends.setValue(mFriendHandler);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success Connection list VM");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }

        try {
            JSONArray contactsArray = result.getJSONArray("sentRequests");
            List<String> temp2 = new ArrayList<>();
            for (int i = 0; i < contactsArray.length(); i++) {
                temp2.add(contactsArray.getJSONObject(i).getString("email"));
                temp2.add(contactsArray.getJSONObject(i).getString("firstname"));
                temp2.add(contactsArray.getJSONObject(i).getString("lastname"));
                temp2.add(contactsArray.getJSONObject(i).getString("username"));

            }
            mPendingHandler = new ArrayList<>();
            for (int i = 0; i < temp2.size(); i += 4) {
                mPendingHandler.add(new Contact(temp2.get(i), temp2.get(i + 1), temp2.get(i + 2), temp2.get(i + 3)));
            }
            if (mPendingHandler.equals(mPendings)) { // If equal, then not update the data to prevent scrolling issue - Hung Vu
                return;
            }
            mPendings.setValue(mPendingHandler);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success Connection list VM");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }

        try {
            JSONArray contactsArray = result.getJSONArray("receivedRequests");
            List<String> temp3 = new ArrayList<>();
            for (int i = 0; i < contactsArray.length(); i++) {
                temp3.add(contactsArray.getJSONObject(i).getString("email"));
                temp3.add(contactsArray.getJSONObject(i).getString("firstname"));
                temp3.add(contactsArray.getJSONObject(i).getString("lastname"));
                temp3.add(contactsArray.getJSONObject(i).getString("username"));

            }
            mInviteHandler = new ArrayList<>();
            for (int i = 0; i < temp3.size(); i += 4) {
                mInviteHandler.add(new Contact(temp3.get(i), temp3.get(i + 1), temp3.get(i + 2), temp3.get(i + 3)));
            }
            if (mInviteHandler.equals(mInvites)) { // If equal, then not update the data to prevent scrolling issue - Hung Vu
                return;
            }
            mInvites.setValue(mInviteHandler);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success Connection list VM");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }

    }

    public void getAllContacts(final String email, final String jwt) {
        mJwt = jwt;
        mEmail = email;
        // Due to timer task in home, these keep getting printed to logcat, so I comment out - Hung Vu
//        Log.i("JWT", mJwt);
//        String url = getApplication().getResources().getString(R.string.base_url) + mEmail;
        String url = "localhost:5000/" + mEmail;
        JSONObject j = new JSONObject();
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", mJwt);
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

    public List<Contact> getFriends() { return mFriends.getValue(); }

    public List<Contact> getPendings() { return mPendings.getValue(); }

    public List<Contact> getInvites() { return mInvites.getValue(); }
}
