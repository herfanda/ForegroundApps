package cartenz.yunus.foregroundapps.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.List;


public class RestarterBroadCastReceiver extends BroadcastReceiver {
    private Context context;

    private String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (isMyServiceIsRunning(this.context,ForegroundService.class)){
            Log.i("FOREGROUND SERVICE", "Foreground Service Still Running !!!");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(new Intent(context, ForegroundService.class));
            } else {
                context.startService(new Intent(context,ForegroundService.class));
            }

            Log.i("FOREGROUND SERVICE", "Foreground Service Stops! Lets Restart !!!");
        }
        
    }

    private void startServiceByAlarm(Context context) {
        // Get alarm manager.
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // Create intent to invoke the background service.
        Intent intent = new Intent(context, ForegroundService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long startTime = System.currentTimeMillis();
        long intervalTime = 60*1000;

        String message = "Start service use repeat alarm. ";

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        Log.d(TAG_BOOT_BROADCAST_RECEIVER, message);

        // Create repeat alarm.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);
    }


    public static boolean isMyServiceIsRunning(Context context, Class<?> serviceClass){

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (services != null) {
            for (int i = 0; i < services.size(); i++) {
                if ((serviceClass.getName()).equals(services.get(i).service.getClassName()) && services.get(i).pid != 0) {
                    return true;
                }
            }
        }


        return false;
    }
}
