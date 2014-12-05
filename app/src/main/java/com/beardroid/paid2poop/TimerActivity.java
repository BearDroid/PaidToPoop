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
    public final String task_name = "Timing new poop";
    public final int task_id = 1; //no idea what this is for
    public static SharedPreferences mPrefs;
    public boolean isCounting;
    Toolbar mToolbar;
    ImageButton play;
    ImageButton pause;
    DataHandler handler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_view);
        //final String hrlyWage = getIntent().getExtras().getString("wage");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle(null);

        play = (ImageButton) findViewById(R.id.playBtn);
        pause = (ImageButton) findViewById(R.id.pauseBtn);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer(v);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChronometer(v);
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        });

    }

    public void startChronometer(View view) {
        ((com.beardroid.paid2poop.Chronometer) findViewById(R.id.chrono)).start();
        isCounting = true;
        /**
        Intent intent = new Intent(this, ChronometerService.class);
        intent.putExtra("task_name", task_name);
        intent.putExtra("task_id", task_id);
        intent.putExtra("ellapsedTime", ((com.beardroid.paid2poop.Chronometer) findViewById(R.id.chrono)).getBase());
        Log.d("base", "" + ((com.beardroid.paid2poop.Chronometer) findViewById(R.id.chrono)).getBase());
        startService(intent);
         **/
    }

    public void stopChronometer(View view) {
        isCounting = false;
        mPrefs = getPreferences(Context.MODE_PRIVATE);
        final String wageStr = mPrefs.getString("hourlyWage", null);
        ((com.beardroid.paid2poop.Chronometer) findViewById(R.id.chrono)).stop();
        long elapsedMillis = SystemClock.elapsedRealtime() - ((com.beardroid.paid2poop.Chronometer) findViewById(R.id.chrono)).getBase();
        double seconds = elapsedMillis / 1000;
        double minutes = seconds / 60;
        DecimalFormat timeFormat = new DecimalFormat("0.00");
        String minStr = timeFormat.format(minutes);
        double minDbl = Double.parseDouble(minStr);
        final double hrDbl = minDbl / 60;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View alertView = inflater.inflate(R.layout.time_dialog, (ViewGroup) this.findViewById(R.id.timeDialog));
        builder.setView(alertView);
        builder.setPositiveButton("Add!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSharedPreferences("hourlyWage", Context.MODE_PRIVATE);
                final String wageStr = mPrefs.getString("hourlyWage", null);
                RadioGroup ratingGroup = (RadioGroup) alertView.findViewById(R.id.ratingGroup);
                RadioButton ratingButton;
                int selectedRating = ratingGroup.getCheckedRadioButtonId();
                ratingButton = (RadioButton) alertView.findViewById(selectedRating);
                String ratingString = (String) ratingButton.getText();

                Double wageDbl = Double.parseDouble(wageStr); //saved wage to double
                //minDbl = minDbl / 60;
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
                        //do nothing
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
        long startTime = SystemClock.elapsedRealtime() - ((com.beardroid.paid2poop.Chronometer) findViewById(R.id.chrono)).getBase();
        Intent intent = new Intent(this, TimerActivity.class);
        PendingIntent contentintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Current poop:")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis() - startTime)
                .setUsesChronometer(true)
                .setContentIntent(contentintent)
                .setSmallIcon(R.drawable.ic_launcher);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
        super.onPause();
    }

    /**
     * @Override protected void onPause() {
     * Intent intent = new Intent(this, TimerActivity.class);
     * PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
     * // build notification
     * // the addAction re-use the same intent to keep the example short
     * NotificationCompat.Builder n = new NotificationCompat.Builder(this)
     * .setSmallIcon(R.drawable.ic_launcher)
     * .setContentTitle("Current poop time: ")
     * .setContentIntent(pIntent);
     * NotificationManager notificationManager =
     * (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
     * notificationManager.notify(0, n.build());
     * super.onPause();
     * }
     */


    private void openDB() {
        handler = new DataHandler(this);
        handler.open();
    }

    private void closeDB() {
        handler.close();
    }
}
