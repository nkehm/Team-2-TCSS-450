package edu.uw.tcss450team2client.ui.chat;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

public class ChatRoom implements Serializable {

    /**
     * Mutable Live Data of List<String> that holds the emails for this chatroom.
     */
    private MutableLiveData<List<String>> mEmailList;

    private final int mChatId;

    private String mChatRoomName;

    private final Context mContext;

    private UserInfoViewModel userInfoViewModel;


    /**
     * @param context
     * @param model
     * @param chatId
     */
    public ChatRoom(final Context context, UserInfoViewModel model, final int chatId) {
        mContext = context;
        mChatRoomName = "Dummy Room Name";
        userInfoViewModel = model;
        mChatId = chatId;
        mEmailList = new MutableLiveData<>();

        connectGetEmails();

        mEmailList.setValue(new ArrayList<>());

    }

    private void connectGetEmails() {

        String url = mContext.getResources().getString(R.string.base_url)
                + "chats?chatId=" + mChatId;

        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
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
        Volley.newRequestQueue(mContext.getApplicationContext()).add(request);

        Log.d("test log", "anything");
    }

    /**
     * Method to handle volley errors.
     *
     * @param error VolleyError object.
     */
    private void handleError(final VolleyError error) {
        if (error != null && error.getMessage() != null) {
            Log.e("Connect Error in ChatRoom", error.getMessage());
            throw new IllegalStateException(error.getMessage());
        }
    }

    private void handleResult(final JSONObject result) {
        Log.d("test log", result.toString());
        if (!result.has("rows")) {
            throw new IllegalStateException("Unexpected response in ChatRoom: " + result);
        }
        try {
            ArrayList<String> listOfEmails = new ArrayList<>();
            JSONArray rows = result.getJSONArray("rows");

            for (int counter = 0; counter < rows.length(); counter++) {
                JSONObject row = rows.getJSONObject(counter);
                String email = row.getString("email");
                listOfEmails.add(email);
            }
            mEmailList.setValue(listOfEmails);
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public int getChatId() {
        return mChatId;
    }

    public String getChatRoomName() {
        return mChatRoomName;
    }

    public LiveData<List<String>> getLiveEmailList() {
        return mEmailList;
    }


}


