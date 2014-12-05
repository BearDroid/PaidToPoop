package com.beardroid.paid2poop;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Max on 12/4/2014.
 */
public class ChronometerService extends Service {

    private ThreadGroup myThreads = new ThreadGroup("ServiceWorker");
    private NotificationManager notificationMgr;
    private int task_id;
    private long ellapsedTime;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String task_name = intent.getExtras().getString("task_name");
        task_id = intent.getExtras().getInt("task_id");
        ellapsedTime = intent.getExtras().getLong("ellapsedTime");
        Log.d("servicebase", "" + ellapsedTime);
        displayNotificationMessage(task_name);
        new Thread(myThreads, new ServiceWorker(), "ChronometerService").start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        myThreads.interrupt();
        notificationMgr.cancelAll();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void displayNotificationMessage(String message) {
        Notification notification = new Notification(R.drawable.ic_timer, message, System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR;
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("task_id", task_id);
        intent.putExtra("ellapsedTime", ellapsedTime);
        Log.d("servicebase1", "" + Long.toString(ellapsedTime));
        PendingIntent contentintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, "ChronometerService", message, contentintent);
        //notificationMgr.notify(0, notification);
    }

    private class ServiceWorker implements Runnable {

        public void run() {

        }

    }

}
