package com.example.bodyguard;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class CustomGeoCoding extends AsyncTask<String, Void, LatLng> {

	private double lat;
	private double lng;
	private LatLng latLng;
    private final String API_KEY = "AIzaSyAI6xxyy2Nanjr8uO7XDUaaKM-grEZWhys";

	@Override
	protected LatLng doInBackground(String... youraddress) {
		// output =

		String encAddress = "";
		try {
			encAddress = java.net.URLEncoder.encode(youraddress[0], "utf8");
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}

	    String uriString = "https://maps.google.com/maps/api/geocode/json?address="
 + encAddress + "&sensor=false&key=" + API_KEY;

		StringBuilder stringBuilder = null;
		try {
			HttpGet httpGet = new HttpGet(uriString);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			stringBuilder = new StringBuilder();

			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());

			lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			Log.e("longitude", Double.toString(lng));

			lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");
			Log.e("latitude", Double.toString(lat));
			latLng = new LatLng(lat, lng);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return latLng;
	}

	@Override
	protected void onPostExecute(LatLng result) {
		super.onPostExecute(result);

	}
}
