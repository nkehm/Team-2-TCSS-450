package edu.uw.tcss450team2client.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450team2client.R;

public class UserInfoViewModel extends ViewModel {

    private final String mEmail;
    private final String mJwt;
    private final String mFName;
    private final String mLName;
    private final String mUsername;
    private final int mMemberId;
    private final MutableLiveData<List<Notification>> mNotificationList;

    // theme
    private Integer mTheme;

    // dark mode
    private Integer mDMode;


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
        mTheme = R.style.Theme_LightBlue;
        mDMode = 0;
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

    /**
     * Get app theme.
     * @return current theme of app
     */
    public int getTheme() {
        return mTheme;
    }

    /**
     * Set app theme.
     * @param theme theme to change to
     */
    public void setTheme(final int theme) {
        mTheme = theme;
    }

    /**
     * Get app dark mode.
     * @return current mode of app
     */
    public int getDMode() {
        return mDMode;
    }

    /**
     * Set app dark mode.
     * @param dMode mode to change to
     */
    public void setDMode(final int dMode) {
        mDMode = dMode;
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
