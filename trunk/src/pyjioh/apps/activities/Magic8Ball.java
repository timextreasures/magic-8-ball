package pyjioh.apps.activities;

import java.util.List;
import java.util.Random;

import pyjioh.apps.R;
import pyjioh.apps.consts.Settings;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class Magic8Ball extends Activity implements SensorEventListener {

	private Random randomizer = new Random();
	private int countSensorChange = 0;
	private SensorManager sensorManager;
	private Sensor sensor;

	private String getAnswer() {
		int randomInt = randomizer.nextInt(20);
		return getResources().getStringArray(R.array.responses)[randomInt];
	}

	private void resetSensorReadings() {
		countSensorChange = 0;
	}

	private void registerSensorManager() {
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			sensor = sensors.get(0);
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		registerSensorManager();
		showMessage(R.string.shake_me_title, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.shake:
			showMessage(null, getAnswer());
			return true;
		}
		return false;
	}

	private void showMessage(Integer resid, String message) {
		TextView triangle = (TextView) findViewById(R.id.MessageTextView);
		triangle.setVisibility(TextView.INVISIBLE);
		if (resid != null)
			triangle.setText(resid);
		else if (message != null)
			triangle.setText(message);

		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setStartOffset(Settings.START_OFFSET);
		triangle.setVisibility(TextView.VISIBLE);
		animation.setDuration(Settings.FADE_DURATION);
		triangle.startAnimation(animation);

		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(Settings.VIBRATE_TIME);

		resetSensorReadings();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// do nothing
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.values[2] > 6)
			countSensorChange++;
		if ((countSensorChange >= 4) && event.values[2] > 6)
			showMessage(null, getAnswer());
	}

}