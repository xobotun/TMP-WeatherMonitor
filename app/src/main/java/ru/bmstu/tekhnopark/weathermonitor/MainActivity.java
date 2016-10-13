package ru.bmstu.tekhnopark.weathermonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class MainActivity extends AppCompatActivity {

    private TextView textTemperature;
    private MyBroadcastReceiver receiver;
	private IntentFilter filter;
	private boolean isSubscribed = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTemperature = (TextView) findViewById(R.id.textWeather);
        receiver = new MyBroadcastReceiver();
		filter = new IntentFilter();
        filter.addAction(WeatherService.DATA_LOADED);
		
		setSubscribed(true);
		startService(new Intent(WeatherService.LOAD_DATA));
    }

    @Override
    protected void onStart() {
        super.onStart();
		setSubscribed(true);
    }

	@Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(WeatherService.LOAD_DATA));
    }
	
    @Override
    protected void onStop() {
        super.onStop();
        setSubscribed(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
	
	private void setSubscribed(boolean value) {
		if (value && !isSubscribed) {
			isSubscribed = true;
			LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(receiver, filter);
			return;
		}
		if (!value && isSubscribed) {
			isSubscribed = false;
			LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(receiver);
			return;
		}
	}

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            City city = WeatherStorage.getInstance(MainActivity.this).getCurrentCity();
            if (city == null)
                city = City.SPRINGFIELD;
            Weather weather = WeatherStorage.getInstance(MainActivity.this).getLastSavedWeather(city);
            textTemperature.setText("Temperature: " + weather.getTemperature() + "\r\n" + weather.getDescription());
        }
    }
}
