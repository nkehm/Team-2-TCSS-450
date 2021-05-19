package edu.uw.tcss450team2client.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450team2client.R;
import edu.uw.tcss450team2client.model.UserInfoViewModel;

public class ContactRequestRecyclerViewAdapter extends
        RecyclerView.Adapter<ContactRequestRecyclerViewAdapter.RequestViewHolder> {

    private List<FriendRequest> mFriendRequest;

    private ContactListViewModel mViewModel;

    private UserInfoViewModel mInfoModel;

    private Context mContext;

    public ContactRequestRecyclerViewAdapter(List<FriendRequest> requests, Context context) {
        this.mFriendRequest = requests;
        mContext = context;

        mInfoModel = new ViewModelProvider((FragmentActivity) mContext)
                .get(UserInfoViewModel.class);

        mViewModel = new ViewModelProvider((FragmentActivity) mContext)
                .get(ContactListViewModel.class);
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View contactView = inflater.inflate(R.layout.fragment_contact_request_card,
                parent, false);
        ContactRequestRecyclerViewAdapter.RequestViewHolder viewHolder = new
                ContactRequestRecyclerViewAdapter.RequestViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.setRequest(mFriendRequest.get(position));
    }

    @Override
    public int getItemCount() {
        return mFriendRequest.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        private TextView usernameTextView;
        private ImageButton acceptButton;
        private ImageButton declineButton;
        private final View mView;
        private FriendRequest mRequest;

        public RequestViewHolder(View v) {
            super(v);
            mView = v;
            usernameTextView = v.findViewById(R.id.contact_username_request);
            acceptButton = v.findViewById(R.id.contact_request_accept_button);
            declineButton = v.findViewById(R.id.contact_request_decline_button);
        }

        private void setRequest(final FriendRequest request) {
            mRequest = request;
            usernameTextView.setText(request.getUsername());

            //Accept button on click listener
            acceptButton.setOnClickListener(v -> {

            });
            //Decline button on click listener
            declineButton.setOnClickListener(v -> {

            });
        }
    }
}
