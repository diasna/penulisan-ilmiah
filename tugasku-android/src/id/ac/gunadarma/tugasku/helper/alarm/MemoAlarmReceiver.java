package id.ac.gunadarma.tugasku.helper.alarm;

import id.ac.gunadarma.tugasku.MainActivity;
import id.ac.gunadarma.tugasku.R;
import id.ac.gunadarma.tugasku.db.TaskSQLiteHelper;
import id.ac.gunadarma.tugasku.ui.TimePreference;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

public class MemoAlarmReceiver extends WakefulBroadcastReceiver {

	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	public static final int NOTIFICATION_ID = 1;

	private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    
	@Override
	public void onReceive(Context ctx, Intent intent) {
		mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, MainActivity.class), 0);
		TaskSQLiteHelper sqLiteHelper = new TaskSQLiteHelper(ctx);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
			.setAutoCancel(true)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("Pengingat Tugas")
			.setContentText("Kamu mempunyai tugas penting dalam "+sqLiteHelper.getNearDeadline()+" hari");

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	public void setAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MemoAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String time = prefs.getString("notif_time", "08:00");
        System.out.println("Set Next Alarm "+time);
        int hour = TimePreference.getHour(time);
        int minute = TimePreference.getMinute(time);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        
        alarmMgr.cancel(alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, alarmIntent);
        
        ComponentName receiver = new ComponentName(context, MemoBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);           
    }
}
