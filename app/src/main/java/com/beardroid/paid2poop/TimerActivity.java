package com.beardroid.paid2poop;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Max on 12/3/2014.
 */
public class TimerActivity extends ActionBarActivity {
    public static final String MY_PREFS = "myPrefs";

    public SharedPreferences pref;
    public Chronometer chrono;
    Toolbar mToolbar;
    ImageButton play;
    ImageButton pause;
    DataHandler handler;
    boolean running = false;
    long currentTime;
    long baseTime;
    NotificationManager mNotificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setContentView(R.layout.timer_view);
        chrono = new Chronometer(this);
        chrono = (Chronometer) findViewById(R.id.chrono);

        currentTime = 0;
        pref = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle(null);

        play = (ImageButton) findViewById(R.id.playBtn);
        pause = (ImageButton) findViewById(R.id.pauseBtn);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer();

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChronometer(v);
            }
        });

    }

    public void startChronometer() {
        play.setVisibility(View.GONE);
        pause.setVisibility(View.VISIBLE);
        baseTime = SystemClock.elapsedRealtime() - currentTime;
        chrono.setBase(baseTime);
        chrono.start();
        running = true;
    }

    public void stopChronometer(View view) {
        pause.setVisibility(View.GONE);
        play.setVisibility(View.VISIBLE);
        running = false;
        chrono.stop();
        long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
        double seconds = elapsedMillis / 1000;
        double minutes = seconds / 60;
        DecimalFormat timeFormat = new DecimalFormat("0.00");
        String minStr = timeFormat.format(minutes);
        double minDbl = Double.parseDouble(minStr);
        final double hrDbl = minDbl / 60;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View alertView = inflater.inflate(R.layout.time_dialog, (ViewGroup) this.findViewById(R.id.timeDialog));
        RadioButton good = (RadioButton) alertView.findViewById(R.id.goodbutton);
        good.setChecked(true);
        builder.setView(alertView);
        builder.setPositiveButton("Add!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pref = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                String wageStr = pref.getString("hourlyWage", null);
                RadioGroup ratingGroup = (RadioGroup) alertView.findViewById(R.id.ratingGroup);
                RadioButton ratingButton;
                int selectedRating = ratingGroup.getCheckedRadioButtonId();
                ratingButton = (RadioButton) alertView.findViewById(selectedRating);
                String ratingString = (String) ratingButton.getText();

                Double wageDbl = Double.parseDouble(wageStr); //saved wage to double
                double moneyMade = hrDbl * wageDbl;
                DecimalFormat moneyFormat = new DecimalFormat("0.00");
                String madeStr = moneyFormat.format(moneyMade);
                moneyMade = Double.parseDouble(madeStr);
                madeStr = "$" + madeStr;
                String date = dateGetter();
                String time = timeGetter();
                openDB();
                handler.insertData(moneyMade, date, time, ratingString, madeStr);
                finish();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chrono.setBase(SystemClock.elapsedRealtime());
                    }
                }).show();
        TextView minutesNum = (TextView) alertView.findViewById(R.id.minutesNum);
        minutesNum.setText(minStr);

    }

    public String timeGetter() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        if (hour == 0) {
            hour = 12;
        }
        Format timeFormat = new DecimalFormat("00");
        int min = cal.get(Calendar.MINUTE);
        String minute = timeFormat.format(min);
        int ampm = cal.get(Calendar.AM_PM);
        String ampmString;
        if (ampm == 0) { //translates ampm to a readable string because 0 or 1 doesn't help
            ampmString = "AM";
        } else {
            ampmString = "PM";
        }
        String time = hour + ":" + minute + " " + ampmString;
        return time;
    }

    public String dateGetter() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        int year = cal.get(Calendar.YEAR);
        String date = month + " " + day + ", " + year;
        return date;
    }

    @Override
    protected void onPause() {
        currentTime = SystemClock.elapsedRealtime() - chrono.getBase();
        currentTime = currentTime / 1000;
        if (running) {
            Intent intent = this.getIntent();
            long startTime = SystemClock.elapsedRealtime() - chrono.getBase();
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Poop in progress!")
                    .setAutoCancel(true)
                    .setUsesChronometer(true)
                    .setWhen(System.currentTimeMillis() - startTime)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_launcher);
            mNotificationManager.notify(0, builder.build());
        }
        super.onPause();
    }

    private void openDB() {
        handler = new DataHandler(this);
        handler.open();
    }

    @Override
    public void onBackPressed() {
        running = false;
        mNotificationManager.cancel(0);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void closeDB() {
        handler.close();
    }
}
