package cartenz.yunus.foregroundapps.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

import cartenz.yunus.foregroundapps.R;
import cartenz.yunus.foregroundapps.activity.MainActivity;

public class ForegroundService extends Service {

    public static final String CHANNEL_ID = "FOREGROUND_SERVICE_CHANNEL";

    private static final String TAG_BOOT_EXECUTE_SERVICE = "BOOT_BROADCAST_SERVICE";

    private Timer timer;

    private TimerTask timerTask;

    private int counter = 0;

    private MainActivity activity;

    public ForegroundService(MainActivity activity){
        super();
        this.activity = activity;

        Log.i("ZEEPOS", "here I am! FOREGROUND SERVICE");
    }

    public ForegroundService(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input;

        if (intent.getAction() == null){
            input = "Foreground Service Android Has been Actived again";
            openMainActivity();
        } else if (intent.getAction().equals("AKTIF")){
            input = "Foreground Service Android is Active";
        } else {
            input = "";
        }


        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Zeepos Foreground")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        //autoStartService();
        startForeground(1, notification);
        startTimer();

        return START_STICKY;
    }

    /**
     * this method used to be autostart service after reboot
     */

    private void autoStartService(){

        String message = "RunAfterBootService onStartCommand() method.";

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        Log.d(TAG_BOOT_EXECUTE_SERVICE, "RunAfterBootService onStartCommand() method.");


    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public void startTimer(){
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }


    /**
     * it sets the timer to print the counter every n seconds
     */
    private void initTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("FOREGROUND", "TIMER CHECKING  "+ (counter++));
            }
        };
    }

    /**
     * optional
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("FOREGROUND", "SERVICE ON DESTROY!");
        Intent broadcastIntent = new Intent(this, RestarterBroadCastReceiver.class);
        sendBroadcast(broadcastIntent);
        //getProcessID();
        stoptimertask();
    }

    private void getProcessID(){
        int pid = android.os.Process.myPid();

        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo processInfo:manager.getRunningAppProcesses()){
            if (processInfo.pid != pid ){
                openMainActivity();
                Log.i("FOREGROUND", "FORCE KILL PROCESS, LET'S RESTART APPS !");
                return;
            }
        }
    }


    public void openMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.putExtra("ACTIVE_AGAIN",true);
        startActivity(mainIntent);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(getApplicationContext(), "onTaskRemoved called", Toast.LENGTH_LONG).show();
        Log.i("FOREGROUND", "ON TASK REMOVED");
        //openMainActivity();
        restartService();

        super.onTaskRemoved(rootIntent);

    }



    public boolean isMyServiceRunning(Class<?> serviceClass, Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    /**
     * this method used to be re-starting a service if its process is killed on closing the application
     * not work properly in android O and above
     * */
    private void restartService(){
        Intent intent = new Intent(getApplicationContext(), ForegroundService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 1000, pendingIntent);
    }



}
