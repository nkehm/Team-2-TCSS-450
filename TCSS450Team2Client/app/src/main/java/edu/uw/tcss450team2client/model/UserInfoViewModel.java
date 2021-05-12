package edu.uw.tcss450team2client.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserInfoViewModel extends ViewModel {

    private final String mEmail;
    private final String mJwt;
    private final String mFName;
    private final String mLName;
    private final String mUsername;
    //private final int mMemberID;

    private UserInfoViewModel(String email, String jwt, String fName,
                              String LName, String Username) {
        mEmail = email;
        mJwt = jwt;
        mFName = fName;
        mLName = LName;
        mUsername = Username;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getJwt() {
        return mJwt;
    }

    public String getFName() { return mFName; }

    public String getLName() { return mLName; }

    public String getUsername() { return mUsername; }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final String jwt;
        private final String fName;
        private final String lName;
        private final String username;

        public UserInfoViewModelFactory(String email, String jwt, String fName,
                                        String lName, String username) {
            this.email = email;
            this.jwt = jwt;
            this.fName = fName;
            this.lName = lName;
            this.username = username;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt, fName, lName, username);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }
}
