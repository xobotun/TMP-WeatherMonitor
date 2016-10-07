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

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.WeatherStorage;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        createButtons();

        Switch backgroundLoading = (Switch) findViewById(R.id.switch1);
        backgroundLoading.setChecked(SettingStorage.shouldLoadInBackground());
        backgroundLoading.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingStorage.setLoadInBackground(SettingsActivity.this, isChecked);
                //buttonView.setChecked(SettingStorage.shouldLoadInBackground());
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
