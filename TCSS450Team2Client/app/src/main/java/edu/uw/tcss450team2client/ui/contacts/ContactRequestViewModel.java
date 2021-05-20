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

public class ContactRequestViewModel extends AndroidViewModel {

    private MutableLiveData<List<FriendRequest>> mRequestList;


    /**
     * Constructor for ContactRequestViewModel
     *
     * @param application
     */
    public ContactRequestViewModel(@NonNull Application application) {
        super(application);
        RequestGenerator requestGenerator = new RequestGenerator();

        mRequestList = new MutableLiveData<>(requestGenerator.getRequestList());
//        mRequestList = new MutableLiveData<>(new ArrayList<>());
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
}