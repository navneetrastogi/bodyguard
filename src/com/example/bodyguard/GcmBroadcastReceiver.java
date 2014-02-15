package com.example.bodyguard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    String serverUri = "http://192.168.103.124:9000/userinformation/";

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void onReceive(Context context, Intent intent)
	{
	    // Explicitly specify that GcmIntentService will handle the intent.
	    ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
	    // Start the service, keeping the device awake while it is
	    // launching.
	    startWakefulService(context, (intent.setComponent(comp)));
	    ResultActivity.phNumber = intent.getStringExtra("phoneNumber");
	    ResultActivity.latitude = intent.getStringExtra("latitude");
	    ResultActivity.longitude = intent.getStringExtra("longitude");

	    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

	    Intent intent1 = new Intent(context, ResultActivity.class);
	    PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1, 0);

	    Notification n = new Notification.Builder(context).setContentTitle("Somebody is in risk..Please help!!!")
		    .setDefaults(Notification.DEFAULT_ALL)
		    .setSmallIcon(R.drawable.logo)
		    .setContentIntent(pIntent).setAutoCancel(true).getNotification();
	    notificationManager.notify(0, n);

	}

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message)
	{
	    int icon = R.drawable.ic_launcher;
	    long when = System.currentTimeMillis();
	    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    Notification notification = new Notification(icon, message, when);

	    String title = context.getString(R.string.app_name);

	    Intent notificationIntent = new Intent(context, MainActivity.class);
	    // set intent so it does not start a new activity
	    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	    notification.setLatestEventInfo(context, title, message, intent);
	    notification.flags |= Notification.FLAG_AUTO_CANCEL;

	    // Play default notification sound
	    notification.defaults |= Notification.DEFAULT_SOUND;

	    // Vibrate if vibrate is enabled
	    notification.defaults |= Notification.DEFAULT_VIBRATE;
	    notificationManager.notify(0, notification);

	}
}
