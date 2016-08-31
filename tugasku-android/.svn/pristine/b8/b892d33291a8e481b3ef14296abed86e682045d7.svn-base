package id.ac.gunadarma.tugasku.helper.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MemoBootReceiver extends BroadcastReceiver {
	MemoAlarmReceiver alarm = new MemoAlarmReceiver();
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			alarm.setAlarm(context);
		}
	}
}