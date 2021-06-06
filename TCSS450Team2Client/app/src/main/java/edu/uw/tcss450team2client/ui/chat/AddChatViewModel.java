package edu.uw.tcss450team2client.ui.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450team2client.ui.contacts.Contact;

public class AddChatViewModel extends AndroidViewModel {

    private MutableLiveData<String> mContactList;

    public AddChatViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mContactList.setValue("");
    }

    public void addChatUserListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<String> observer) {
        mContactList.observe(owner, observer);
    }

    public void updateContactListText(String username) {
        if (mContactList.getValue() == "") {
            mContactList.setValue(username);
        } else {
            if(!mContactList.getValue().contains(username)) {
                mContactList.setValue(mContactList.getValue() + "," + username);
            }
        }
    }

    public void clearText() {
        mContactList.setValue("");
    }
}
