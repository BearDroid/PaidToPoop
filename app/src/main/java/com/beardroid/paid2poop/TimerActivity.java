package com.beardroid.paid2poop;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Max on 12/3/2014.
 */
public class TimerActivity extends ActionBarActivity {
    public static final String MY_PREFS = "myPrefs";
    private static final String TIME_START = "timerStart";
    private final int REFRESH_RATE = 100;
    public SharedPreferences pref;
    public long startTime;
    SharedPreferences.Editor editor;
    ImageButton play;
    ImageButton pause;
    DataHandler handler;
    boolean running = false;
    long currentTime;
    long masterBaseTime;
    NotificationManager mNotificationManager;
    private TextView tempTextView;
    private Handler mHandler = new Handler();
    private long elapsedTime;
    private Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            mHandler.postDelayed(this, REFRESH_RATE);
        }
    };
    private String hours, minutes, seconds, milleseconds;
    private long secs, mins, hrs, msecs;
    private boolean stopped = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setContentView(R.layout.timer_view);

        pref = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = pref.edit();

        play = (ImageButton) findViewById(R.id.playBtn);
        pause = (ImageButton) findViewById(R.id.pauseBtn);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startClick(v);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopClick(v);
            }
        });
        startTime = pref.getLong(TIME_START, 0);
        if (startTime > 0) {
            mHandler.removeCallbacks(startTimer);
            mHandler.postDelayed(startTimer, 0);
            showStopButton();
            running = true;
        }

    }

    public void startClick(View view) {
        showStopButton();
        if (stopped) {
            startTime = System.currentTimeMillis() - elapsedTime;

        } else {
            startTime = System.currentTimeMillis();
        }
        editor.putLong(TIME_START, startTime);
        editor.apply();
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
        running = true;
    }

    public void stopClick(View view) {
        hideStopButton();
        mHandler.removeCallbacks(startTimer);
        stopped = true;
        long elapsed = System.currentTimeMillis() - startTime;
        Toast.makeText(this, "" + elapsed, Toast.LENGTH_SHORT).show();
        complete(view, elapsed);
        running = false;
    }

    public void showStopButton() {
        pause.setVisibility(View.VISIBLE);
        play.setVisibility(View.GONE);
    }

    public void hideStopButton() {
        pause.setVisibility(View.GONE);
        play.setVisibility(View.VISIBLE);
    }

    public void updateTimer(float time) {
        secs = (long) (time / 1000);
        mins = (long) ((time / 1000) / 60);
        hrs = (long) (((time / 1000) / 60) / 60);

        secs = secs % 60;
        seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }

        mins = mins % 60;
        minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }

        hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "0";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }
        milleseconds = String.valueOf((long) time);
        if (milleseconds.length() == 2) {
            milleseconds = "0" + milleseconds;
        }
        if (milleseconds.length() <= 1) {
            milleseconds = "0";
        }
        if (milleseconds.length() > 2) {
            milleseconds = milleseconds.substring(milleseconds.length() - 3, milleseconds.length() - 2);
        }

        ((TextView) findViewById(R.id.timer)).setText(hours + " " + minutes + " " + seconds + " ");
        ((TextView) findViewById(R.id.timerms)).setText(milleseconds);
    }

    public String timeParse(long time) {
        secs = (long) (time / 1000);
        mins = (long) ((time / 1000) / 60);
        hrs = (long) (((time / 1000) / 60) / 60);

        secs = secs % 60;
        seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }

        mins = mins % 60;
        minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }

        hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "0";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }
        String timeView = hours + ":" + minutes + ":" + seconds;
        return timeView;
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
        if (running) {
            showNotification();
        }
        super.onPause();
    }

    public void showNotification() {
        long time = pref.getLong(TIME_START, 0);
        long elapsed = System.currentTimeMillis() - time;
        Intent intent = new Intent(this, TimerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews notif = new RemoteViews(getPackageName(),
                R.layout.timer_notif);
        long notificationTime = SystemClock.elapsedRealtime() - elapsed;
        editor.putLong("savedTime", notificationTime);
        notif.setChronometer(R.id.chronometer, notificationTime, null, !stopped);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Poop in progress!")
                .setAutoCancel(stopped)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(!stopped)
                .setContent(notif);
        mNotificationManager.notify(0, builder.build());
    }

    public void complete(View view, long elapsed) {
        running = false;
        String timeView = timeParse(elapsed);
        double seconds = elapsed / 1000;
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
                editor.remove(TIME_START).apply();
                finish();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
        TextView minutesNum = (TextView) alertView.findViewById(R.id.minutesNum);
        minutesNum.setText(timeView);
    }

    @Override
    protected void onResume() {
        mNotificationManager.cancel(0);
        super.onResume();
    }

    private void openDB() {
        handler = new DataHandler(this);
        handler.open();
    }

    @Override
    public void onBackPressed() {
        editor.remove("startTime").apply();
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
