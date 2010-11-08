package pyjioh.apps.activities;

import java.util.Random;

import pyjioh.apps.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class Magic8Ball extends Activity {
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
		ImageView triangle = (ImageView) findViewById(R.id.MessageImage);
		triangle.setVisibility(ImageView.INVISIBLE);
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
			showAnswer(0);
			return true;
		}
		return false;
	}

	private void showAnswer(int i) {
		/*
		 * TODO create message use getAnswer()
		 */
		ImageView triangle = (ImageView) findViewById(R.id.MessageImage);
		
		AlphaAnimation fade = new AlphaAnimation(0, 1);
		fade.setStartOffset(500);
		triangle.setVisibility(ImageView.VISIBLE);
		fade.setDuration(1000);
		triangle.startAnimation(fade);

		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		try {
			vibrator.vibrate(100);
		} catch (Exception e) {

		}

	}

}