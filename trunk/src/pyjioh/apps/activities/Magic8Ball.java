package pyjioh.apps.activities;

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

	private SensorManager sensorManager;
	private Sensor sensor;
	private float lastX;
	private float lastY;
	private float lastZ;
	private int shakeCount = 0;

	private boolean isSensorRegistered() {
		return sensor != null;
	}

	/**
	 * the magic code here
	 */
	private String getAnswer() {
		int randomInt = randomizer.nextInt(20);
		return getResources().getStringArray(R.array.responses)[randomInt];
	}

	private void registerSensorListener() {
		sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	private void unregisterSensorListener() {
		sensorManager.unregisterListener(this);
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
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

	@Override
	public void onResume() {
		super.onResume();
		registerSensorListener();
		if (isSensorRegistered())
			showMessage(R.string.shake_me_title, null);
		else
			showMessage(R.string.menu_shake_title, null);
	}

	@Override
	public void onPause() {
		unregisterSensorListener();
		super.onPause();
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
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// do nothing
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			if (isShakeEnough(event.values[0], event.values[1], event.values[2]))
				showMessage(null, getAnswer());
	}

	private boolean isShakeEnough(float x, float y, float z) {
		double force = 0.0d;
		force += Math.pow((x - lastX) / SensorManager.GRAVITY_EARTH, 2.0);
		force += Math.pow((y - lastY) / SensorManager.GRAVITY_EARTH, 2.0);
		force += Math.pow((z - lastZ) / SensorManager.GRAVITY_EARTH, 2.0);
		force = Math.sqrt(force);

		lastX = x;
		lastY = y;
		lastZ = z;

		if (force > Settings.THRESHOLD) {
			shakeCount++;
			if (shakeCount > Settings.SHAKE_COUNT) {
				shakeCount = 0;
				lastX = 0;
				lastY = 0;
				lastZ = 0;
				return true;
			}
		}
		return false;
	}

}
