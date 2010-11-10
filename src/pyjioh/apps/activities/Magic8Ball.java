package pyjioh.apps.activities;

import java.util.Random;

import pyjioh.apps.R;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class Magic8Ball extends Activity implements SensorEventListener {

	private Random randomizer = new Random();
	
	private String getAnswer() {
		int randomInt = randomizer.nextInt(20);
		return getResources().getStringArray(R.array.responses)[randomInt];
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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

		AlphaAnimation fade = new AlphaAnimation(0, 1);
		fade.setStartOffset(1000);
		triangle.setVisibility(TextView.VISIBLE);
		fade.setDuration(1000);
		triangle.startAnimation(fade);
		
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// do nothing
	}

	public void onSensorChanged(SensorEvent event) {
		
	}

}