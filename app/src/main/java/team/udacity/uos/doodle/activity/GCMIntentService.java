package team.udacity.uos.doodle.activity;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import team.udacity.uos.doodle.R;

import static team.udacity.uos.doodle.activity.BasicInfo.PROJECT_ID;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	public DevicePolicyManager devicePolicyManager;
	public ComponentName adminComponent;

	NotificationManager notiManager;
	Vibrator vibrator;
	final static int MyNoti = 0;
	/**
	 * Constructor
	 */
    public GCMIntentService() {
        super(PROJECT_ID);

        Log.d("gcm", "GCMIntentService() called.");
    }



    @Override
    public void onRegistered(Context context, String registrationId) {
    	Log.i("gcm", "onRegistered called : " + registrationId);

    	BasicInfo.RegistrationId = registrationId;



    }



    @Override
    public void onUnregistered(Context context, String registrationId) {


    }

    @Override
    public void onError(Context context, String errorId) {
    	Log.d(TAG, "onError called.");

    	sendToastMessage(context, "에러입니다. : " + errorId);
    }

    @Override
	protected void onDeletedMessages(Context context, int total) {
    	Log.d(TAG, "onDeletedMessages called.");

    	super.onDeletedMessages(context, total);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.d(TAG, "onRecoverableError called.");

		return super.onRecoverableError(context, errorId);
	}

	@Override
    public void onMessage(Context context, Intent intent) {

		String msg = "";
		String from = "";
		String action = "";
        Bundle extras = intent.getExtras();
        if (extras != null) {
        	msg= (String) extras.get("msg");
            from = (String) extras.get("who");
            action = (String) extras.get("action");
            Log.i("teacher",from + "으로부터 메세지 : "+msg);

        }

        notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// ----------알림설정----------//
		Notification noti;
		noti = new Notification(R.drawable.ic_launcher, from + " : " + msg, System.currentTimeMillis());
		noti.defaults = Notification.DEFAULT_SOUND;
		noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		Intent newIntent = new Intent(getBaseContext(), DetailViewActivity.class);

		//PendingIntent pendingI = PendingIntent.getActivity(GCMIntentService.this, 0, newIntent, newIntent.FLAG_ACTIVITY_NEW_TASK);
		//noti.setLatestEventInfo(GCMIntentService.this, "Smart Class",from + " : " + msg, pendingI);
		notiManager.notify(MyNoti, noti);
		vibrator.vibrate(1000);



    }

	/**
	 * Send status messages for toast display
	 *
	 * @param context
	 * @param message
	 */
	static void sendToastMessage(Context context, String message) {

    }

}