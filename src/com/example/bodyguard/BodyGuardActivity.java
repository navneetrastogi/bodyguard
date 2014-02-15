//package com.example.bodyguard;
//
//import java.io.IOException;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.widget.TextView;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//
//public class BodyGuardActivity extends Activity {
//	public static final String EXTRA_MESSAGE = "message";
//	public static final String PROPERTY_REG_ID = "registration_id";
//	private static final String PROPERTY_APP_VERSION = "appVersion";
//	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//
//	/**
//	 * Substitute you own sender ID here. This is the project number you got
//	 * from the API Console, as described in "Getting Started."
//	 */
//    String		      SENDER_ID			= "1049791365253";
//
//	/**
//	 * Tag used on log messages.
//	 */
//	static final String TAG = "GCMDemo";
//
//	TextView mDisplay;
//	GoogleCloudMessaging gcm;
//	AtomicInteger msgId = new AtomicInteger();
//	SharedPreferences prefs;
//	Context context;
//
//	String regid;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//
//		// setContentView(R.layout.main);
//		// mDisplay = (TextView) findViewById(R.id.display);
//
//		context = getApplicationContext();
//
//		if (checkPlayServices()) {
//			gcm = GoogleCloudMessaging.getInstance(this);
//			regid = getRegistrationId(context);
//
//			// storeRegistrationId(context, regid);
//			if (regid.isEmpty()) {
//				registerInBackground();
//			}
//		} else {
//			Log.i(TAG, "No valid Google Play Services APK found.");
//		}
//
//	}
//
//	private String getRegistrationId(Context context) {
//		final SharedPreferences prefs = getGCMPreferences(context);
//	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
//	    ;
//		if (registrationId.isEmpty()) {
//			Log.i(TAG, "Registration not found.");
//			return "";
//		}
//		// Check if app was updated; if so, it must clear the registration ID
//		// since the existing regID is not guaranteed to work with the new
//		// app version.
//		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//		int currentVersion = getAppVersion(context);
//		if (registeredVersion != currentVersion) {
//			Log.i(TAG, "App version changed.");
//			return "";
//		}
//		return registrationId;
//	}
//
//	private SharedPreferences getGCMPreferences(Context context) {
//		// This sample app persists the registration ID in shared preferences,
//		// but
//		// how you store the regID in your app is up to you.
//		return getSharedPreferences(BodyGuardActivity.class.getSimpleName(), Context.MODE_PRIVATE);
//	}
//
//	private static int getAppVersion(Context context) {
//		try {
//			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//			return packageInfo.versionCode;
//		} catch (NameNotFoundException e) {
//			// should never happen
//			throw new RuntimeException("Could not get package name: " + e);
//		}
//	}
//
//	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
//   
//
//	private void sendRegistrationIdToBackend() {
//		// Your implementation here.
//	}
//
//	private void storeRegistrationId(Context context, String regId) {
//		final SharedPreferences prefs = getGCMPreferences(context);
//		int appVersion = getAppVersion(context);
//		Log.i(TAG, "Saving regId on app version " + appVersion);
//		SharedPreferences.Editor editor = prefs.edit();
//		editor.putString(PROPERTY_REG_ID, regId);
//		editor.putInt(PROPERTY_APP_VERSION, appVersion);
//		editor.commit();
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//	    // checkPlayServices();
//	}
//
//	private boolean checkPlayServices() {
//		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//		if (resultCode != ConnectionResult.SUCCESS) {
//			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//			} else {
//				Log.i(TAG, "This device is not supported.");
//				finish();
//			}
//			return false;
//		}
//		return true;
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public void onClick(final View view) {
//		if (view == findViewById(R.id.action_settings)) {
//			new AsyncTask() {
//				@SuppressWarnings("unused")
//				protected String doInBackground(Void... params) {
//					String msg = "";
//					try {
//						Bundle data = new Bundle();
//						data.putString("my_message", "Hello World");
//						data.putString("my_action", "com.google.android.gcm.demo.app.ECHO_NOW");
//						String id = Integer.toString(msgId.incrementAndGet());
//						gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
//						msg = "Sent message";
//					} catch (IOException ex) {
//						msg = "Error :" + ex.getMessage();
//					}
//					return msg;
//				}
//
//				protected void onPostExecute(String msg) {
//					mDisplay.append(msg + "\n");
//				}
//
//				@Override
//				protected Object doInBackground(Object... params) {
//					// TODO Auto-generated method stub
//					return null;
//				}
//			}.execute(null, null, null);
//		} else if (view == findViewById(R.id.action_settings)) {
//			mDisplay.setText("");
//		}
//	}
// }
