package com.example.mcresswell.project01.userProfile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mcresswell.project01.util.WeatherUtils;
import com.example.mcresswell.project01.weather.WeatherForecast;

import java.io.IOException;
import java.net.URL;

public class UserProfileViewModel extends AndroidViewModel {

    //need to update this, but left the weatherViewModel code in here as a reference - kayla
    private static final String LOG = UserProfileViewModel.class.getSimpleName();

    private final MutableLiveData<WeatherForecast> forecastData =
            new MutableLiveData<>();
    private String mLocationCity;
    private String mLocationCountry;


    public UserProfileViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadData(){
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
                URL weatherDataURL = null;
                String retrievedJsonData = null;
                if (strings != null) {
                    String city = strings[0];
                    String country = strings[1];
                    Log.d(LOG, city + ", " + country);
                    weatherDataURL = WeatherUtils.buildWeatherApiUrl(city, country);
                }
                try {
                    retrievedJsonData = WeatherUtils.getJsonDataFromUrl(weatherDataURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return retrievedJsonData;

            }

            @Override
            protected void onPostExecute(String s) {
                    forecastData.setValue(WeatherUtils.getWeatherForecast(s));

            }
        }.execute(mLocationCity, mLocationCountry);
    }

    public void setLocation(String city, String country) {
        mLocationCity = city;
        mLocationCountry = country;
        loadData();
    }


    public MutableLiveData<WeatherForecast> getForecastData() {
        return forecastData;
    }
}
