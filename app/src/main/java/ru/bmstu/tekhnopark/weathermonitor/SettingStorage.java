package ru.bmstu.tekhnopark.weathermonitor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ru.mail.weather.lib.WeatherUtils;

public class SettingStorage {
    static boolean shouldLoadInBackground = false;

    public static boolean shouldLoadInBackground() {
        return shouldLoadInBackground;
    }

    public static void setLoadInBackground(Context context, boolean shouldLoadInBackground1) {
        Log.d("SettingStorage", "shouldLoadInBackground = " + shouldLoadInBackground1);
        shouldLoadInBackground = shouldLoadInBackground1;
        if (shouldLoadInBackground1)
            WeatherUtils.getInstance().schedule(context, new Intent(WeatherService.LOAD_DATA));
        else
            WeatherUtils.getInstance().unschedule(context, new Intent(WeatherService.LOAD_DATA));
    }
}
