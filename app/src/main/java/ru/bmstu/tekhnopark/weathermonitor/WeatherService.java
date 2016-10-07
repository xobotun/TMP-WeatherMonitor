package ru.bmstu.tekhnopark.weathermonitor;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class WeatherService extends IntentService {
    public static final String LOAD_DATA = "ru.bmstu.tekhnopark.weathermonitor.LOAD_DATA";
    public static final String DATA_LOADED = "ru.bmstu.tekhnopark.weathermonitor.DATA_LOADED";

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(LOAD_DATA)) {
                Log.d("WeatherService", "Caught intent!");
                City city = WeatherStorage.getInstance(WeatherService.this).getCurrentCity();
                if (city == null)
                    city = City.SPRINGFIELD;

                Weather weather;
                try {
                    weather = WeatherUtils.getInstance().loadWeather(city);
                } catch (IOException e) {
                    weather = WeatherStorage.getInstance(WeatherService.this).getLastSavedWeather(city);
                    if (weather == null)
                        weather = new Weather(374, "No Internet Connection");
                }
                WeatherStorage.getInstance(WeatherService.this).saveWeather(city, weather);

                Log.d("WeatherService", "Weather is \"" + "Temperature: " + weather.getTemperature() + " " + weather.getDescription() + '"');

                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(DATA_LOADED));
            }
        }
    }
}
