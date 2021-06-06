package edu.uw.tcss450team2client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.android.volley.VolleyError;
import com.auth0.android.jwt.JWT;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.tcss450team2client.databinding.FragmentContactBinding;
import edu.uw.tcss450team2client.databinding.FragmentContactListBinding;
import edu.uw.tcss450team2client.model.LocationViewModel;
import edu.uw.tcss450team2client.databinding.ActivityMainBinding;
import edu.uw.tcss450team2client.model.NewContactRequestCountViewModel;
import edu.uw.tcss450team2client.model.NewMessageCountViewModel;
import edu.uw.tcss450team2client.model.Notification;
import edu.uw.tcss450team2client.model.PushyTokenViewModel;
import edu.uw.tcss450team2client.model.UserInfoViewModel;
import edu.uw.tcss450team2client.services.PushReceiver;
import edu.uw.tcss450team2client.ui.chat.ChatMessage;
import edu.uw.tcss450team2client.ui.chat.ChatViewModel;
import edu.uw.tcss450team2client.ui.contacts.ContactFragment;
import edu.uw.tcss450team2client.ui.contacts.ContactListViewModel;
import edu.uw.tcss450team2client.ui.contacts.ContactRequestViewModel;
import edu.uw.tcss450team2client.ui.contacts.Invitation;
import edu.uw.tcss450team2client.ui.weather.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private MainPushReceiver mPushReceiver;

    private MainPushRequestReceiver mPushRequestReceiver;

    private MainPushRequestAcceptedReceiver mPushRequestAcceptedReceiver;

    private NewMessageCountViewModel mNewMessageModel;

    private UserInfoViewModel mUserViewModel;

    private WeatherViewModel mWeatherModel;
    private static boolean mFirstConnect = true;

    private NewContactRequestCountViewModel mNewRequestModel;

    NavController navController;

    private MainActivityArgs mArgs;

    private MutableLiveData<JSONObject> mResponse;

    private ContactRequestViewModel mContactRequestViewModel;

    private ContactListViewModel mContactListViewModel;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // A constant int for the permissions request code. Must be a 16 bit number
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;
    private LocationRequest mLocationRequest;
    //Use a FusedLocationProviderClient to request the location
    private FusedLocationProviderClient mFusedLocationClient;
    // Will use this call back to decide what to do when a location change is detected
    private LocationCallback mLocationCallback;
    //The ViewModel that will store the current location
    private LocationViewModel mLocationModel;

    private ActivityMainBinding binding;

    private FragmentContactBinding contactBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        SharedPreferences prefs =
                this.getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);

        if (prefs.contains(getString(R.string.keys_prefs_themes))) {
            int theme = prefs.getInt(getString(R.string.keys_prefs_themes), -1);

            switch (theme) {
                case 1:
                    Log.d("main", "set indigo");
                    setTheme(R.style.Theme_Indigo);
                    break;
                case 2:
                    Log.d("main", "set teal");
                    setTheme(R.style.Theme_Teal);
                    break;
                default:
                    Log.d("main", "set light blue");
                    setTheme(R.style.Theme_LightBlue);
                    break;
            }
        }

        int darkMode = prefs.getInt(getString(R.string.keys_prefs_modes), -1);
        if (darkMode == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (darkMode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        mArgs = MainActivityArgs.fromBundle(getIntent().getExtras());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        contactBinding = FragmentContactBinding.inflate(getLayoutInflater());
        setContentView(contactBinding.getRoot());
        setContentView(binding.getRoot());

        JWT jwt = new JWT(mArgs.getJwt());
        String username = jwt.getClaim("username").asString();
        String firstName = jwt.getClaim("firstname").asString();
        String lastName = jwt.getClaim("lastname").asString();
        int memberId = jwt.getClaim("memberid").asInt();

        mUserViewModel = new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(mArgs.getEmail(), mArgs.getJwt(), mArgs.getFirstname(),
                        mArgs.getLastname(), mArgs.getMemberid(), mArgs.getUsername()))
                .get(UserInfoViewModel.class);

        mWeatherModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        mWeatherModel.setUserInfoViewModel(mUserViewModel);

        if (mFirstConnect) {
            mWeatherModel.connectGet();
            mFirstConnect  = false;
        }

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,
                R.id.navigation_contacts, R.id.navigation_chat, R.id.navigation_weather).build();
        navController = Navigation.findNavController(this,
                R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController,
                mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        } else {
            //The user has already allowed the use of Locations. Get the current location.
            requestLocation();
        }


        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);
        mNewRequestModel = new ViewModelProvider(this).get(NewContactRequestCountViewModel.class);
        mContactRequestViewModel = new ViewModelProvider(this).get(ContactRequestViewModel.class);
        mContactListViewModel = new ViewModelProvider(this).get(ContactListViewModel.class);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_chat) {
                //When the user navigates to the chats page, reset the new message count.
                // This will need some extra logic for your project as it should have
                // multiple chat rooms.
                mNewMessageModel.reset();
            }
            if (destination.getId() == R.id.navigation_contacts) {
                //When the user navigates to the chats page, reset the new message count.
                // This will need some extra logic for your project as it should have
                // multiple chat rooms.
//                mNewRequestModel.reset();
            }
        });

        mNewMessageModel.addMessageCountObserver(this, count -> {

            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_chat); //THIS WAS NAV_CHAT BEFORE I CHANGED IT
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
        });

        mNewRequestModel.addContactCountObserver(this, count -> {

            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_contacts); //THIS WAS NAV_CHAT BEFORE I CHANGED IT
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new contact request, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
//            BadgeDrawable badgeDrawable = contactBinding.tabLayout.getTabAt(0).getOrCreateBadge();
//            if (count > 0) {
//                //new messages! update and show the notification badge.
//                badgeDrawable.setNumber(count);
//                badgeDrawable.setVisible(true);
//            } else {
//                //user did some action to clear the new contact request, remove the badge
//                badgeDrawable.clearNumber();
//                badgeDrawable.setVisible(false);
//            }

        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this,
                R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Toolbar menu with defined functions.
     *
     * @param menu the options menu where we place the items.
     * @return boolean to determine if menu is to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.navigate_button_password:
                //navController.navigate(R.id.changePasswordFragment);
                changePasswordDialogue();
                break;

            case R.id.action_appearance:
                navController.navigate(R.id.appearanceFragment);
                break;

            case R.id.action_sign_out:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                    requestLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION DENIED", "Nothing to see or do here.");

                    //Shut down the app. In production release, you would let the user
                    //know why the app is shutting down...maybe ask for permission again?
                    finishAndRemoveTask();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Creates a popup dialog box that prompts users to change their password.
     */
    private void changePasswordDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.textview_changePassword_headMessage);
        //builder.setMessage(R.string.textview_changePassword_description);
        builder.setPositiveButton(R.string.button_changePassword_change, (dialog, which) -> {
            Log.d("ChangeP", "User wants to change password");
            //connectChangePassword();
            navController.navigate(R.id.changePasswordFragment);
        });
        builder.setNegativeButton(R.string.button_changePassword_cancel, (dialog, which) -> {
            Log.d("changeP", "cancel change");
            //do nothing
        });
        builder.create();
        builder.show();
    }


    /**
     * Server credential authentication error handling.
     *
     * @param error message
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
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("LOCATION", location.toString());
                                if (mLocationModel == null) {
                                    mLocationModel = new ViewModelProvider(MainActivity.this)
                                            .get(LocationViewModel.class);
                                }
                                mLocationModel.setLocation(location);
                            }
                        }
                    });
        }
    }

    /**
     * Allows user to sign out in app.
     */
    public void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();

        PushyTokenViewModel model = new ViewModelProvider(this)
                .get(PushyTokenViewModel.class);
        //when we hear back from the web service quit
        model.addResponseObserver(this, result -> finishAndRemoveTask());
        model.deleteTokenFromWebservice(
                new ViewModelProvider(this)
                        .get(UserInfoViewModel.class)
                        .getJwt()
        );

    }

    /**
     * Changes the color theme of the app.
     * @param view the theme to change to
     */
    public void changeColorTheme(View view) {
        SharedPreferences prefs =
                this.getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);

        if (view.getId() == R.id.rb_theme_lightblue
                && mUserViewModel.getTheme() != R.style.Theme_LightBlue) {
            mUserViewModel.setTheme(R.style.Theme_LightBlue);
            prefs.edit().putInt(getString(R.string.keys_prefs_themes), 0).apply();
            recreate();
        } else if (view.getId() == R.id.rb_theme_indigo
                && mUserViewModel.getTheme() != R.style.Theme_Indigo) {
            mUserViewModel.setTheme(R.style.Theme_Indigo);
            prefs.edit().putInt(getString(R.string.keys_prefs_themes), 1).apply();
            recreate();
        } else if (view.getId() == R.id.rb_theme_teal
                && mUserViewModel.getTheme() != R.style.Theme_Teal) {
            mUserViewModel.setTheme(R.style.Theme_Teal);
            prefs.edit().putInt(getString(R.string.keys_prefs_themes), 2).apply();
            recreate();
        }
    }

    /**
     * Changes the dark mode of the app.
     * @param view the view from button clicked
     */
    public void changeDarkMode(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        SharedPreferences prefs =
                this.getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (view.getId() == R.id.rb_darkmode_off && mUserViewModel.getDMode() != 0 && checked) {
            mUserViewModel.setDMode(0);
            prefs.edit().putInt(getString(R.string.keys_prefs_modes), 0).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (view.getId() == R.id.rb_darkmode_on && mUserViewModel.getDMode() != 1 && checked) {
            mUserViewModel.setDMode(1);
            prefs.edit().putInt(getString(R.string.keys_prefs_modes), 1).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushReceiver extends BroadcastReceiver {
        private ChatViewModel mModel = new ViewModelProvider(MainActivity.this).get(ChatViewModel.class);

        @Override
        public void onReceive(Context context, Intent intent) {
            Notification notification = new Notification();
            NavController nc = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            Log.d("PUSHY", "result: " + intent.toString());
            if (intent.hasExtra("chatMessage")) {
                Log.d("PUSHY", "MainActivity has received chat message");
                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.chatFragment) {
                    mNewMessageModel.increment();
                }
                //Inform the view model holding chatroom messages of the new
                // message.
                if (mUserViewModel != null && mUserViewModel.getUsername() != null) {
                    Log.d("PUSHY", "Message from" + cm.getSender());
                    if (!cm.getSender().equals(mUserViewModel.getUsername())) {
                        Log.d("PUSHY", "We didn't send this message!" + cm.getSender());
                        notification.setData(cm);
                        mUserViewModel.addNotifications(notification);
                    }
                }
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
            } else if (intent.hasExtra("invitation")) {
                Log.d("PUSHY", "MainActivity has received contact Invite");
                Invitation contactRequest = (Invitation) intent.getSerializableExtra("invitation");
                if (nd.getId() != R.id.navigation_contacts) {
                    mNewRequestModel.increment();
                }
                notification.setData(contactRequest);
                mUserViewModel.addNotifications(notification);
            }
        }
    }

    private class MainPushRequestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Notification notification = new Notification();
            NavController nc =
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            Log.d("PUSHY", "result: " + intent.toString());
            if (intent.hasExtra("username")) {
                Log.d("PUSHY", "MainActivity has received contact Invite");
                // If the user is not on the chat screen, update the
                // NewRequestCountView Model
                if (nd.getId() != R.id.navigation_contacts) {
                    mNewRequestModel.increment();


                } else {
                    mContactRequestViewModel.connectGet(mUserViewModel.getJwt());
                }
            }
        }
    }

    private class MainPushRequestAcceptedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Notification notification = new Notification();
            NavController nc =
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            Log.d("PUSHY", "result: " + intent.toString());
            if (intent.hasExtra("usernameAccepted")) {
                Log.d("PUSHY", "MainActivity has received contact Invite accepted");
                // If the user is not on the chat screen, update the
                // NewRequestCountView Model
                if (nd.getId() == R.id.navigation_contacts) {
                    mContactListViewModel.connectGet(mUserViewModel.getJwt());
                }
            }

        }
    }


    /**
     * Returns UserInfoViewModel.
     */
    public UserInfoViewModel getUserInfoViewModel() {
        return mUserViewModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPushReceiver == null) {
            mPushReceiver = new MainPushReceiver();
        }
        if (mPushRequestReceiver == null) {
            mPushRequestReceiver = new MainPushRequestReceiver();
        }
        if (mPushRequestAcceptedReceiver == null) {
            mPushRequestAcceptedReceiver = new MainPushRequestAcceptedReceiver();
        }
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(PushReceiver.RECEIVED_NEW_MESSAGE);
        iFilter.addAction(PushReceiver.RECEIVED_NEW_CONTACT_REQUEST);

        registerReceiver(mPushReceiver, iFilter);
        IntentFilter iFilterRequest = new IntentFilter();
        IntentFilter iFilterRequestAccepted = new IntentFilter();
        iFilterRequest.addAction(PushReceiver.RECEIVED_NEW_CONTACT_REQUEST);
        iFilterRequestAccepted.addAction(PushReceiver.RECEIVED_NEW_CONTACT_ACCEPTED);
        registerReceiver(mPushRequestReceiver, iFilterRequest);
        registerReceiver(mPushRequestAcceptedReceiver, iFilterRequestAccepted);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushReceiver != null) {
            unregisterReceiver(mPushReceiver);
        }
        if (mPushRequestReceiver != null) {
            unregisterReceiver(mPushRequestReceiver);
        }
        if (mPushRequestAcceptedReceiver != null) {
            unregisterReceiver(mPushRequestAcceptedReceiver);
        }
    }
}