package edu.uw.tcss450team2client.ui.weather;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class WeatherMapViewModel extends ViewModel {
    private MutableLiveData<Location> mLocation;

    public WeatherMapViewModel() {
        mLocation = new MediatorLiveData<>();
    }

    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }

}
