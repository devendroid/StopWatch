package com.devs.stopwatch;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private Timer timer;
    private int timeCountUp = 0;
    private int hour, minute, second, millis, reminder1, reminder2;
    private TextView tvTimer, tvHr, tvMn, tvMillis;
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
        tvMillis = (TextView) findViewById(R.id.tv_millis);
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
//                        hour = ++timeCountUp / 3600;
//                        reminder = timeCountUp % 3600;
//                        minute = reminder / 60;
//                        second = reminder % 60;
//                        hrStr = (hour < 10 ? "0" : "") + hour;
//                        mnStr = (minute < 10 ? "0" : "") + minute;
//                        secStr = (second < 10 ? "0" : "") + second;
//                        tvHr.setText(hrStr + ":");
//                        tvMn.setText(mnStr + ":");
//                        tvTimer.setText(secStr);



                        hour = ++timeCountUp / 3600000;
                        reminder1 = timeCountUp % 3600000;
                        minute = reminder1 / 60000;
                        reminder2 = reminder1 % 60000;
                        second = reminder2 /1000;
                        millis = reminder2 % 1000;
                        tvHr.setText(String.format("%02d:", hour));
                        tvMn.setText(String.format("%02d:", minute));
                        tvTimer.setText(String.format("%02d", second));
                        tvMillis.setText(String.format("%03d", millis));

                    }
                });
            }
        }, 1, 1);
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
        tvMillis.setText("0000");
    }

    private void addInLapsList() {
        Collections.reverse(lapsList);
        lapsList.add(hour + " : " + minute + " : " + second +" : "+millis);
        Collections.reverse(lapsList);
        adapter.notifyDataSetChanged();
    }
}
