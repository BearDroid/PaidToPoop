package com.beardroid.paid2poop;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
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
    private final int REFRESH_RATE = 100;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    Toolbar mToolbar;
    ImageButton play;
    ImageButton pause;
    DataHandler handler;
    boolean running = false;
    long currentTime;
    long masterBaseTime;
    NotificationManager mNotificationManager;
    private TextView tempTextView;
    private Handler mHandler = new Handler();
    private long startTime;
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

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle(null);

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

    }

    public void startClick(View view) {
        showStopButton();
        if (stopped) {
            startTime = System.currentTimeMillis() - elapsedTime;
        } else {
            startTime = System.currentTimeMillis();
        }
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
    }

    public void stopClick(View view) {
        hideStopButton();
        mHandler.removeCallbacks(startTimer);
        stopped = true;
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
            milleseconds = "00";
        }
        if(milleseconds.length() > 2) {
            milleseconds = milleseconds.substring(milleseconds.length() - 3, milleseconds.length() - 2);
        }
        ((TextView) findViewById(R.id.timer)).setText(hours + " " + minutes + " " + seconds + " ");
        ((TextView) findViewById(R.id.timerms)).setText(milleseconds);
    }

    /**
     * public void stopTimer(View view) {
     * running = false;
     * chrono.stop();
     * long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
     * double seconds = elapsedMillis / 1000;
     * double minutes = seconds / 60;
     * DecimalFormat timeFormat = new DecimalFormat("0.00");
     * String minStr = timeFormat.format(minutes);
     * double minDbl = Double.parseDouble(minStr);
     * final double hrDbl = minDbl / 60;
     * AlertDialog.Builder builder = new AlertDialog.Builder(this);
     * LayoutInflater inflater = this.getLayoutInflater();
     * final View alertView = inflater.inflate(R.layout.time_dialog, (ViewGroup) this.findViewById(R.id.timeDialog));
     * RadioButton good = (RadioButton) alertView.findViewById(R.id.goodbutton);
     * good.setChecked(true);
     * builder.setView(alertView);
     * builder.setPositiveButton("Add!", new DialogInterface.OnClickListener() {
     *
     * @Override public void onClick(DialogInterface dialog, int which) {
     * pref = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
     * String wageStr = pref.getString("hourlyWage", null);
     * RadioGroup ratingGroup = (RadioGroup) alertView.findViewById(R.id.ratingGroup);
     * RadioButton ratingButton;
     * int selectedRating = ratingGroup.getCheckedRadioButtonId();
     * ratingButton = (RadioButton) alertView.findViewById(selectedRating);
     * String ratingString = (String) ratingButton.getText();
     * <p/>
     * Double wageDbl = Double.parseDouble(wageStr); //saved wage to double
     * double moneyMade = hrDbl * wageDbl;
     * DecimalFormat moneyFormat = new DecimalFormat("0.00");
     * String madeStr = moneyFormat.format(moneyMade);
     * moneyMade = Double.parseDouble(madeStr);
     * madeStr = "$" + madeStr;
     * String date = dateGetter();
     * String time = timeGetter();
     * openDB();
     * handler.insertData(moneyMade, date, time, ratingString, madeStr);
     * finish();
     * }
     * })
     * .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
     * @Override public void onClick(DialogInterface dialog, int which) {
     * chrono.setBase(SystemClock.elapsedRealtime());
     * }
     * }).show();
     * TextView minutesNum = (TextView) alertView.findViewById(R.id.minutesNum);
     * minutesNum.setText(minStr);
     * <p/>
     * }
     */
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
        /**
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
         builder.getNotification().flags |= Notification.FLAG_NO_CLEAR;

         mNotificationManager.notify(0, builder.build());
         }**/
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
