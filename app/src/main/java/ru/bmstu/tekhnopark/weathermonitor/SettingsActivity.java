package ru.bmstu.tekhnopark.weathermonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class SettingsActivity extends Activity {

    private final ArrayList<Button> buttonsWithListeners = new ArrayList<>(7);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        createButtons();

        Switch backgroundLoadingOn = (Button) findViewById(R.id.buttonLoadBackgroundOn);
        backgroundLoadingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherUtils.getInstance().schedule(SettingsActivity.this, new Intent(WeatherService.LOAD_DATA));
                Log.d("SettingsActivity", "Scheduled loading data in background.");
            }
        });

        Switch backgroundLoadingOff = (Button) findViewById(R.id.buttonLoadBackgroundOff);
        backgroundLoadingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherUtils.getInstance().unschedule(SettingsActivity.this, new Intent(WeatherService.LOAD_DATA));
                Log.d("SettingsActivity", "Unscheduled loading data in background.");
            }
        });
    }

    private void createButtons() {
        LinearLayout root = (LinearLayout) findViewById(R.id.settings_root);

        for (final City city: City.values()) {
            Button button = new Button(this);
            button.setText(city.name());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WeatherStorage.getInstance(SettingsActivity.this).setCurrentCity(city);
                    startService(new Intent(WeatherService.LOAD_DATA));
                    Log.d("SettingsActivity", "Town is now " + city.name());
                }
            });

            root.addView(button);
        }
    }
}
