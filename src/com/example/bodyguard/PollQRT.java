package com.example.bodyguard;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class PollQRT {

    private final String serverUri = "http://192.168.103.124:9000/userinformation";

    @SuppressWarnings("unchecked")
    public void sendFirstCurrentLocation(Context context, Intent intent, final UserInformation userInformation, final GPSTracker gpsTracker)
	{
	    // Explicitly specify that GcmIntentService will handle the intent
	    // Start the service, keeping the device awake while it is
	    // launching.

	    new AsyncTask() {
		@Override
		protected Object doInBackground(Object... params)
		    {
			HttpResponse response = null;
			JSONObject jo = null;
			try {
			    jo = new JSONObject();
			    jo.put("deviceId", userInformation.getDeviceId());
			    JSONObject cord = new JSONObject();
			    cord.put("latitude", gpsTracker.getLatitude());
			    cord.put("longitude", gpsTracker.getLongitude());
			    jo.put("name", userInformation.getName());
			    jo.put("phoneNumber", userInformation.getPhonenumber());
			    jo.put("cord", cord);
			    jo.put("keepWatch", "false");

			}
			catch (Exception e) {
			    Log.i(GcmIntentService.TAG, e.getMessage());
			}

			HttpPost post = new HttpPost(serverUri);

			try {
			    post.setEntity(new StringEntity(jo.toString(), "UTF8"));
			    post.setHeader("Content-type", "application/json");
			    HttpClient client = new DefaultHttpClient();

			    response = client.execute(post);
			    if (response.getStatusLine().getStatusCode() == 200) {
				Thread.currentThread().sleep(10000);
			    }
			    System.out.print(response.getStatusLine().getStatusCode());
			}
			catch (ClientProtocolException e) {
			    Log.i(GcmIntentService.TAG, e.getMessage());

			}
			catch (IOException e) {
			    // TODO Auto-generated catch block
			    Log.i(GcmIntentService.TAG, e.getMessage());

			}
			catch (InterruptedException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();

			}

			return response;
		    }

	    }.execute();

	}

    @SuppressWarnings("unchecked")
    public void sendCurrentLocation(Context context, Intent intent, final UserInformation userInformation, final GPSTracker gpsTracker)
	{
	    // Explicitly specify that GcmIntentService will handle the intent
	    // Start the service, keeping the device awake while it is
	    // launching.

	    new AsyncTask() {
		@Override
		protected Object doInBackground(Object... params)
		    {
			HttpResponse response = null;
			while (true) {
			JSONObject jo = null;
			try {
				jo = new JSONObject();
				jo.put("deviceId", userInformation.getDeviceId());
				JSONObject cord = new JSONObject();
				cord.put("latitude", gpsTracker.getLatitude());
				cord.put("longitude", gpsTracker.getLongitude());
				jo.put("name", userInformation.getName());
				jo.put("phoneNumber", userInformation.getPhonenumber());
				jo.put("cord", cord);
				jo.put("keepWatch", "true");

			}
			catch (Exception e) {
			    Log.i(GcmIntentService.TAG, e.getMessage());
			}

			HttpPost post = new HttpPost(serverUri);

			try {
			    post.setEntity(new StringEntity(jo.toString(), "UTF8"));
			    post.setHeader("Content-type", "application/json");
			    HttpClient client = new DefaultHttpClient();

			    response = client.execute(post);
				if (response.getStatusLine().getStatusCode() == 200) {
				    Thread.currentThread().sleep(10000);
				}
			    System.out.print(response.getStatusLine().getStatusCode());
			}
			catch (ClientProtocolException e) {
			    Log.i(GcmIntentService.TAG, e.getMessage());
				break;
			}
			catch (IOException e) {
			    // TODO Auto-generated catch block
			    Log.i(GcmIntentService.TAG, e.getMessage());
				break;
			}
			    catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			    }
			}
			return response;
		    }

	    }.execute();

	}

    @SuppressWarnings("rawtypes")
    public void sendSOS(Context context, Intent intent, final UserInformation userInformation)
	{
	    // Explicitly specify that GcmIntentService will handle the intent.

	    // Start the service, keeping the device awake while it is
	    // launching.

	    new AsyncTask() {
		@Override
		protected Object doInBackground(Object... params)
		    {
			JSONObject jo = null;
			try {
			    jo = new JSONObject();
			    jo.put("deviceId", userInformation.getDeviceId());
			    JSONObject cord = new JSONObject();
			    cord.put("latitude", userInformation.getCord().getLatitude());
			    cord.put("longitude", userInformation.getCord().getLongitude());
			    jo.put("name", userInformation.getName());
			    jo.put("phoneNumber", userInformation.getPhonenumber());
			    jo.put("cord", cord);
			}
			catch (Exception e) {
			    Log.i(GcmIntentService.TAG, e.getMessage());
			}

			HttpGet post = new HttpGet(serverUri + "?deviceid=" + userInformation.getDeviceId());
			HttpResponse response = null;

			try {
			    // post.setEntity(new StringEntity(jo.toString(),
			    // "UTF8"));
			    post.setHeader("Content-type", "application/json");
			    HttpClient client = new DefaultHttpClient();

			    response = client.execute(post);

			    System.out.print(response.getStatusLine().getStatusCode());
			}
			catch (ClientProtocolException e) {
			    Log.i(GcmIntentService.TAG, e.getMessage());
			}
			catch (IOException e) {
			    // TODO Auto-generated catch block
			    Log.i(GcmIntentService.TAG, e.getMessage());
			}
			return response;
		    }

	    }.execute();

	}
}
