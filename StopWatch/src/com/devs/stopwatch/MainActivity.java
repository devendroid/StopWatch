package com.devs.stopwatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private Timer timer;
	private int timeCountUp = 0;
	private int hour, minute, second, reminder;
	private String hrStr, mnStr, secStr;
	private TextView tvTimer, tvHr, tvMn;
	private boolean isRunning = false;
	private ListView lapsListView;
	private ArrayList<String> lapsList;
	private ArrayAdapter<String> adapter;
	private ImageView ivStop, ivPlayPause;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().hide();

		initLayout();
		startCountUpTimer();
	}

	private void initLayout() {

		lapsListView = (ListView) findViewById(R.id.laps_listview);
		lapsList = new ArrayList<>();
		adapter = new ArrayAdapter<String>(this, R.layout.row_laps,
				android.R.id.text1, lapsList);
		lapsListView.setAdapter(adapter);
		
		ivStop = (ImageView) findViewById(R.id.iv_stop);
		ivStop.setOnClickListener(this);
		ivPlayPause = (ImageView) findViewById(R.id.iv_play_pause);
		ivPlayPause.setOnClickListener(this);
		tvTimer = (TextView) findViewById(R.id.tv_timer);
		tvHr = (TextView) findViewById(R.id.tv_hr);
		tvMn = (TextView) findViewById(R.id.tv_mn);
		tvTimer.setOnClickListener(this);
		Typeface tf = Typeface.createFromAsset(this.getAssets(), "ds_dig.TTF");
		tvTimer.setTypeface(tf);
		tvHr.setTypeface(tf);
		tvMn.setTypeface(tf);
	}

	private void pauseCountUpTimer() {
		if (timer != null) {
			isRunning = false;
			ivPlayPause.setImageResource(R.drawable.ic_action_play);
			timer.cancel();
		}
	}

	private void startCountUpTimer() {
		isRunning = true;
		ivPlayPause.setImageResource(R.drawable.ic_action_pause);
		timer = new Timer();
		final Date date = new Date();
		// date.setHours(0);
		// date.setMinutes(0);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						hour = ++timeCountUp / 3600;
						reminder = timeCountUp % 3600;
						minute = reminder / 60;
						second = reminder % 60;
						hrStr = (hour < 10 ? "0" : "") + hour;
						mnStr = (minute < 10 ? "0" : "") + minute;
						secStr = (second < 10 ? "0" : "") + second;
						tvHr.setText(hrStr + ":");
						tvMn.setText(mnStr + ":");
						tvTimer.setText(secStr);
					}
				});
			}
		}, 1000, 1000);
	}

	@Override
	protected void onStop() {
		super.onStop();
		//timer.cancel();
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_timer:
			addInLapsList();
			break;
		case R.id.iv_stop:
			stopCountUpTimer();
			break;

		case R.id.iv_play_pause:
			if (isRunning) {
				pauseCountUpTimer();
			} else {
				startCountUpTimer();
			}
			break;
		}
	}
	
	private void stopCountUpTimer() {
		pauseCountUpTimer();
		// reset all values
		timeCountUp = 0;
		lapsList.clear();
		adapter.notifyDataSetChanged();
		tvHr.setText("00:");
		tvMn.setText("00:");
		tvTimer.setText("00");
	}

	private void addInLapsList() {
		Collections.reverse(lapsList);
		lapsList.add(hrStr + ":" + mnStr + ":" + secStr);
		Collections.reverse(lapsList);
		adapter.notifyDataSetChanged();
	}
}
