package edu.uw.tcss450team2client.model;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class UserInfoViewModel extends ViewModel {

    private final String mEmail;
    private final String mJwt;
    private final String mFName;
    private final String mLName;
    private final String mUsername;
    private final int mMemberId;
    private final MutableLiveData<List<Notification>> mNotificationList;


    private UserInfoViewModel(String email, String jwt, String fName,
                              String LName, int memberId, String Username) {
        mEmail = email;
        mJwt = jwt;
        mFName = fName;
        mLName = LName;
        mUsername = Username;
        mMemberId = memberId;
        mNotificationList = new MutableLiveData<>();
        mNotificationList.setValue(new ArrayList<>());
    }

    public void addNotifications(Notification notification) {
        List<Notification> newList = this.mNotificationList.getValue();
        newList.add(notification);
        this.mNotificationList.setValue(newList);
    }

    public void clearNotifications() {
        List<Notification> newList = this.mNotificationList.getValue();
        newList.clear();
        this.mNotificationList.setValue(newList);
    }

    public void deleteNotification(final Notification toDelete) {
        List<Notification> newList = this.mNotificationList.getValue();
        newList.remove(toDelete);
        this.mNotificationList.setValue(newList);
    }

    public void addNotificationsObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Notification>> observer) {
        mNotificationList.observe(owner, observer);
    }

    public String getEmail() {
        return mEmail;
    }

    public String getJwt() {
        return mJwt;
    }

    public String getFName() {
        return mFName;
    }

    public String getLName() {
        return mLName;
    }

    public int getMemberId(){
        return mMemberId;
    }

    public String getUsername() {
        return mUsername;
    }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final String jwt;
        private final String fName;
        private final String lName;
        private final String username;
        private final int memberId;


        public UserInfoViewModelFactory(String email, String jwt, String fName,
                                        String lName, int memberId, String username) {
            this.email = email;
            this.jwt = jwt;
            this.fName = fName;
            this.lName = lName;
            this.username = username;
            this.memberId = memberId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt, fName, lName, memberId, username);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }
}
