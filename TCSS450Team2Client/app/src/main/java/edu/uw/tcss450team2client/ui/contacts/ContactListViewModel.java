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
import com.pusher.pushnotifications.BeamsCallback;
import com.pusher.pushnotifications.PushNotifications;
import com.pusher.pushnotifications.PusherCallbackError;
import com.pusher.pushnotifications.auth.AuthData;
import com.pusher.pushnotifications.auth.AuthDataGetter;
import com.pusher.pushnotifications.auth.BeamsTokenProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.io.RequestQueueSingleton;

public class ContactListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mContactList;
    private MutableLiveData<List<Contact>> mContactListFull;
    private MutableLiveData<List<Contact>> mFavoriteList;
    private final MutableLiveData<JSONObject> mResponse;


    /**
     * Constructor for Contact List View Model
     *
     * @param application the application
     */
    public ContactListViewModel(@NonNull Application application) {
        super(application);
        ContactGenerator contactGenerator = new ContactGenerator();
//        mContactList = new MutableLiveData<>(contactGenerator.getContactList());
//        mFavoriteList = new MutableLiveData<>(contactGenerator.getContactList());
//        mContactListFull = new MutableLiveData<>(contactGenerator.getContactList());

        mContactList = new MutableLiveData<>(new ArrayList<>());
        mContactListFull = new MutableLiveData<>(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * contact list view model observer.
     *
     * @param owner    life cycle owner
     * @param observer observer
     */
    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Contact>> observer) {
        mContactList.observe(owner, observer);
    }

    public void addFavoriteListObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super List<Contact>> observer) {
        mFavoriteList.observe(owner, observer);
    }

    public void addContactListAllObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super List<Contact>> observer) {
        mContactListFull.observe(owner, observer);
    }


    /**
     * webservice response observer.
     *
     * @param owner    life cycle owner
     * @param observer observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    public void connectPusher(final String jwt, final String email) {
        BeamsTokenProvider tokenProvider = new BeamsTokenProvider(
                "https://mobileapp-group-backend.herokuapp.com/pusher",
                () -> {
                    /*
                     Headers and URL query params your auth endpoint needs to
                     request a Beams Token for a given user
                    */
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", jwt);

                    HashMap<String, String> queryParams = new HashMap<>();
                    return new AuthData(
                            headers,
                            queryParams
                    );
                }
        );

        PushNotifications.setUserId(email, tokenProvider,
                new BeamsCallback<Void, PusherCallbackError>() {
                    @Override
                    public void onSuccess(Void... values) {
                        Log.i("PusherBeams", "Successfully authenticated with Pusher Beams");
                    }

                    @Override
                    public void onFailure(PusherCallbackError error) {
                        Log.i("PusherBeams", "Pusher Beams authentication failed: "
                                + error.getMessage());
                    }
                });
    }

    /**
     * connect to the webservice and get contact list
     *
     * @param jwt authorization token
     */
    public void connectGet(String jwt) {
        String url = "https://tcss450-team2-server.herokuapp.com/contacts";
//        String url = "http://localhost:5000/contacts";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // no body
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
     * connect to the webservice and get favorite list
     *
     * @param jwt authorization token
     */
    public void connectGetFavorite(String jwt) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/favorite";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // no body
                this::handleSuccessFavorite,
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
     * connect to the webservice and request for a contact deletion
     *
     * @param jwt      JWT authorization token
     * @param memberID to be deleted
     */
    public void deleteContact(String jwt, final int memberID) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/contact/" + memberID;
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                mResponse::setValue,
                this::handleError) {
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * add a contact to favorite tab
     *
     * @param jwt      JWT Authorization Token
     * @param memberID to be favorite
     */
    public void addFavorite(final String jwt, final int memberID) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/favorite/" + memberID;


        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                mResponse::setValue,
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
     * Remove a contact from the favorite tab
     *
     * @param jwt      JWT Authorization Token
     * @param memberID to be un-favorite
     */
    public void unFavorite(final String jwt, final int memberID) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/favorite/delete/"
                + memberID;

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                mResponse::setValue,
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
     * Aceept friend request
     *
     * @param jwt      JWT
     * @param memberID to accept
     */
    public void acceptRequest(final String jwt, final int memberID) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/request/" + memberID;

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                mResponse::setValue,
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

    public void declineRequest(final String jwt, final String username) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/decline";

        JSONObject body = new JSONObject();
        try {
            body.put("userName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
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
     * send a friend request to username if match.
     *
     * @param jwt      JWT Authorization Token.
     * @param username the username.
     */
    public void addFriend(final String jwt, final String username) {

        String url = "https://mobileapp-group-backend.herokuapp.com/contact/add";

        JSONObject body = new JSONObject();
        try {
            body.put("userName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
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
     * connect to the webservice and get contact list
     * @param jwt authorization token
     */
    public void connectGetAll(String jwt) {
//        String url = "https://mobileapp-group-backend.herokuapp.com/contact/all";
        String url = "https//localhost:5000/contact/all";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // no body
                this::handleSuccessAll,
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

    private void handleSuccessAll(JSONObject jsonObject) {
        ArrayList<Contact> temp = new ArrayList<>();
        try {
            JSONArray contacts = jsonObject.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                String email = contact.getString("email");
                String firstName = contact.getString("firstName");
                String lastName = contact.getString("lastName");
                String username = contact.getString("userName");
                int memberID = contact.getInt("memberId");
                Contact entry = new Contact(email, firstName, lastName, username, memberID);
                temp.add(entry);
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mContactListFull.setValue(temp);
    }


    /**
     * handle a success connection to the back-end
     * @param result result
     */
    private void handleSuccess(final JSONObject result) {
        ArrayList<Contact> temp = new ArrayList<>();
        try {
            JSONArray contacts = result.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                int verified = contact.getInt("verified");
                if (verified == 1) {
                    String email = contact.getString("email");
                    String firstName = contact.getString("firstName");
                    String lastName = contact.getString("lastName");
                    String username = contact.getString("userName");
                    int memberID = contact.getInt("memberId");

                    Contact entry = new Contact(email, firstName, lastName, username, memberID);
                    temp.add(entry);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mContactList.setValue(temp);
    }

    /**
     * handle a success connection to the back-end
     *
     * @param result result
     */
    private void handleSuccessFavorite(final JSONObject result) {
        ArrayList<Contact> temp = new ArrayList<>();
        try {
            JSONArray contacts = result.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                int favorite = contact.getInt("favorite");
                if (favorite == 1) {
                    String email = contact.getString("email");
                    String firstName = contact.getString("firstName");
                    String lastName = contact.getString("lastName");
                    String username = contact.getString("userName");
                    int memberID = contact.getInt("memberId");

                    Contact entry = new Contact(email, firstName, lastName, username, memberID);
                    temp.add(entry);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mFavoriteList.setValue(temp);
    }

    public void putContactMembers(final String jwt, int chatID, int memberID) throws JSONException {
        String url = "https://mobileapp-group-backend.herokuapp.com/addcontactmember/" + chatID + "/" + memberID;
        System.out.println("Adding Contact To Chat, Member ID: ");
        JSONObject body = new JSONObject();
        try {
            body.put("memberid", memberID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(body.toString());

        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body, //push token found in the JSONObject body
                mResponse::setValue,
                this::handleChatError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
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

    /**
     * handle a failure connection to the back-end
     *
     * @param error the error.
     */
    private void handleChatError(final VolleyError error) {
        Log.e("CONNECTION ERROR", "No Chat Info");
    }


    /**
     * handle a failure connection to the back-end
     *
     * @param error the error.
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public List<Contact> getList() {
        return this.mContactListFull.getValue();
    }
}




//public class ContactListViewModel extends AndroidViewModel {
//
//    private MutableLiveData<List<Contact>> mFriends;
//    private List<Contact> mFriendHandler = new ArrayList<Contact>();
//    private MutableLiveData<List<Contact>> mPendings;
//    private List<Contact> mPendingHandler = new ArrayList<Contact>();
//    private MutableLiveData<List<Contact>> mInvites;
//    private List<Contact> mInviteHandler = new ArrayList<Contact>();
//    private String mJwt;
//    private String mEmail;
//
//
//    public ContactListViewModel(@NonNull Application application) {
//        super(application);
//
//        ContactGenerator contactGenerator = new ContactGenerator();
//        // Setup MutableLiveData for all the fields that are being used in Contact List
//        mFriends = new MutableLiveData<>();
//        mFriends.setValue(new ArrayList<>());
//
//        // Set local dummy data for testing
////        mFriends.setValue(contactGenerator.getContactList());
//
//        mPendings = new MutableLiveData<>();
//        mPendings.setValue(new ArrayList<>());
//
//        mInvites = new MutableLiveData<>();
//        mInvites.setValue(new ArrayList<>());
//    }
//
//    public void addContactListObserver(@NonNull LifecycleOwner owner,
//                                    @NonNull Observer<? super List<Contact>> observer) {
//        mFriends.observe(owner, observer);
//    }
//
//    public void addPendingObserver(@NonNull LifecycleOwner owner,
//                                       @NonNull Observer<? super List<Contact>> observer) {
//        mPendings.observe(owner, observer);
//    }
//
//    public void adInviteObserver(@NonNull LifecycleOwner owner,
//                                       @NonNull Observer<? super List<Contact>> observer) {
//        mInvites.observe(owner, observer);
//    }
//
//    private void handleError(final VolleyError error) {
//        if (Objects.isNull(error.networkResponse)) {
//            Log.e("NETWORK ERROR", error.getMessage());
//        }
//        else {
//            String data = new String(error.networkResponse.data, Charset.defaultCharset());
//            Log.e("CLIENT ERROR",
//                    error.networkResponse.statusCode +
//                            " " +
//                            data);
//        }
//    }
//
//    private void handleResult(final JSONObject result) {
//        try {
//            JSONArray tempJSONArray = result.getJSONArray("contacts");
//            List<String> temp = new ArrayList<>();
//            for (int i = 0; i < tempJSONArray.length(); i++) {
//                temp.add(tempJSONArray.getJSONObject(i).getString("email"));
//                temp.add(tempJSONArray.getJSONObject(i).getString("firstname"));
//                temp.add(tempJSONArray.getJSONObject(i).getString("lastname"));
//                temp.add(tempJSONArray.getJSONObject(i).getString("username"));
//            }
//            for (int i = 0; i < temp.size(); i += 4) {
//                mFriendHandler.add(new Contact(temp.get(i), temp.get(i + 1), temp.get(i + 2), temp.get(i + 3)));
//            }
//            if (mFriendHandler.equals(mFriends)) { // If equal, then not update the data to prevent scrolling issue - Hung Vu
//                return;
//            }
//            mFriends.setValue(mFriendHandler);
//        } catch (JSONException e){
//            Log.e("JSON PARSE ERROR", "Found in handle Success Connection list VM");
//            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
//        }
//
//        try {
//            JSONArray contactsArray = result.getJSONArray("sentRequests");
//            List<String> temp2 = new ArrayList<>();
//            for (int i = 0; i < contactsArray.length(); i++) {
//                temp2.add(contactsArray.getJSONObject(i).getString("email"));
//                temp2.add(contactsArray.getJSONObject(i).getString("firstname"));
//                temp2.add(contactsArray.getJSONObject(i).getString("lastname"));
//                temp2.add(contactsArray.getJSONObject(i).getString("username"));
//
//            }
//            mPendingHandler = new ArrayList<>();
//            for (int i = 0; i < temp2.size(); i += 4) {
//                mPendingHandler.add(new Contact(temp2.get(i), temp2.get(i + 1), temp2.get(i + 2), temp2.get(i + 3)));
//            }
//            if (mPendingHandler.equals(mPendings)) { // If equal, then not update the data to prevent scrolling issue - Hung Vu
//                return;
//            }
//            mPendings.setValue(mPendingHandler);
//        } catch (JSONException e) {
//            Log.e("JSON PARSE ERROR", "Found in handle Success Connection list VM");
//            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
//        }
//
//        try {
//            JSONArray contactsArray = result.getJSONArray("receivedRequests");
//            List<String> temp3 = new ArrayList<>();
//            for (int i = 0; i < contactsArray.length(); i++) {
//                temp3.add(contactsArray.getJSONObject(i).getString("email"));
//                temp3.add(contactsArray.getJSONObject(i).getString("firstname"));
//                temp3.add(contactsArray.getJSONObject(i).getString("lastname"));
//                temp3.add(contactsArray.getJSONObject(i).getString("username"));
//
//            }
//            mInviteHandler = new ArrayList<>();
//            for (int i = 0; i < temp3.size(); i += 4) {
//                mInviteHandler.add(new Contact(temp3.get(i), temp3.get(i + 1), temp3.get(i + 2), temp3.get(i + 3)));
//            }
//            if (mInviteHandler.equals(mInvites)) { // If equal, then not update the data to prevent scrolling issue - Hung Vu
//                return;
//            }
//            mInvites.setValue(mInviteHandler);
//        } catch (JSONException e) {
//            Log.e("JSON PARSE ERROR", "Found in handle Success Connection list VM");
//            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
//        }
//
//    }
//
//    public void getAllContacts(final String email, final String jwt) {
//        mJwt = jwt;
//        mEmail = email;
//        // Due to timer task in home, these keep getting printed to logcat, so I comment out - Hung Vu
////        Log.i("JWT", mJwt);
////        String url = getApplication().getResources().getString(R.string.base_url) + mEmail;
//        String url = "localhost:5000/" + mEmail;
//        JSONObject j = new JSONObject();
//        Request request = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null, //no body for this get request
//                this::handleResult,
//                this::handleError) {
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                // add headers <key,value>
//                headers.put("Authorization", mJwt);
//                return headers;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Instantiate the RequestQueue and add the request to the queue
//        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
//                .addToRequestQueue(request);
//    }
//
//    public List<Contact> getFriends() { return mFriends.getValue(); }
//
//    public List<Contact> getPendings() { return mPendings.getValue(); }
//
//    public List<Contact> getInvites() { return mInvites.getValue(); }
//}
