package com.example.bodyguard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity {

    // Google Map
    private GoogleMap googleMap;

    // GPSTracker class
    GPSTracker	gps;

    private LatLng    source;
    private LatLng    destination;

    public static final String  EXTRA_MESSAGE		    = "message";
    public static final String  PROPERTY_REG_ID		  = "registration_id";
    private static final String PROPERTY_APP_VERSION	     = "appVersion1";
    private final static int    PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String		      SENDER_ID			= "1049791365253";

    /**
     * Tag used on log messages.
     */
    static final String	 TAG			      = "GCMDemo";

    TextView		    mDisplay;
    GoogleCloudMessaging	gcm;
    AtomicInteger	       msgId			    = new AtomicInteger();
    SharedPreferences	   prefs;
    Context		     context;

    String		      regid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    context = getApplicationContext();

	    try {

		if (checkPlayServices()) {
		    gcm = GoogleCloudMessaging.getInstance(this);
		    regid = getRegistrationId(context);

		    // storeRegistrationId(context, regid);
		    if (regid.isEmpty()) {
			registerInBackground();
		    }
 else {
			sendRegistrationIdToBackend();
		    }
		} else {
		    Log.i(TAG, "No valid Google Play Services APK found.");
		}

		// Loading map
		initilizeMap();
		// latitude and longitude
		double latitude = 0;// = 12.966712;
		double longitude = 0;// = 77.5667;

		gps = new GPSTracker(this);
		if (gps.canGetLocation()) {
		    latitude = gps.getLatitude(); // returns latitude

		    longitude = gps.getLongitude(); // returns longitude
		    // \n is for new line
		    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG)
			    .show();
		    source = new LatLng(latitude, longitude);
		    PollQRT pollQRT = new PollQRT();
		    UserInformation userInformation = new UserInformation();
		    userInformation.setName("Sumit");
		    userInformation.setPhonenumber(8861421989L);
		    // userInformation.setDeviceId(regid);
		    userInformation.setCord(userInformation.new Coordinates(gps.getLatitude(), gps.getLongitude()));
		    pollQRT.sendFirstCurrentLocation(getApplicationContext(), getIntent(), userInformation, gps);

		} else {
		    gps.showSettingsAlert();
		    if (gps.canGetLocation()) {
			latitude = gps.getLatitude(); // returns latitude

			longitude = gps.getLongitude(); // returns longitude
			// \n is for new line)
			source = new LatLng(latitude, longitude);
		    }

		}

		// create marker
		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Location");
		// ROSE color icon
		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		// adding marker
		googleMap.addMarker(marker);
		CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(14).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googleMap.getUiSettings().setZoomGesturesEnabled(true);
		googleMap.setMyLocationEnabled(true);


	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	    // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	    // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	    // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
	    // AutoCompleteTextView autoCompView1 = (AutoCompleteTextView)
	    // findViewById(R.id.autoCompleteTextView1);

	    AutoCompleteTextView autoCompView2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
	    // autoCompView1.setAdapter(new PlacesAutoCompleteAdapter(this,
	    // R.layout.list_item));
	    autoCompView2.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
	    /*
	     * autoCompView1.setOnItemClickListener(new OnItemClickListener() {
	     * 
	     * @Override public void onItemClick(AdapterView<?> parent, View
	     * view, int position, long id) { // TODO Auto-generated method stub
	     * Button setRoute = (Button) findViewById(R.id.button1);
	     * setRoute.setEnabled(false); Toast.makeText(MainActivity.this,
	     * PlacesAutoCompleteAdapter.resultList.get(position),
	     * Toast.LENGTH_SHORT).show(); CustomGeoCoding cgc = new
	     * CustomGeoCoding(); try { source =
	     * cgc.execute(PlacesAutoCompleteAdapter
	     * .resultList.get(position)).get(); } catch (InterruptedException
	     * e) { Toast.makeText(MainActivity.this, e.getMessage(),
	     * Toast.LENGTH_SHORT).show(); e.printStackTrace(); } catch
	     * (ExecutionException e) { Toast.makeText(MainActivity.this,
	     * e.getMessage(), Toast.LENGTH_SHORT).show(); e.printStackTrace();
	     * }
	     * 
	     * }
	     * 
	     * });
	     */

	    autoCompView2.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		    {
			Button setRoute = (Button) findViewById(R.id.button1);
			setRoute.setEnabled(true);
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, PlacesAutoCompleteAdapter.resultList.get(position), Toast.LENGTH_SHORT).show();
			CustomGeoCoding cgc = new CustomGeoCoding();
			try {
			    destination = cgc.execute(PlacesAutoCompleteAdapter.resultList.get(position)).get();
			}
			catch (InterruptedException e) {
			    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			    e.printStackTrace();
			}
			catch (ExecutionException e) {
			    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			    e.printStackTrace();
			}
		    }

	    });

	    Button setRoute = (Button) findViewById(R.id.button1);
	    setRoute.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v)
		    {
			try {
			String url = getDirectionsUrl(source, destination);
			DownloadTask downloadTask = new DownloadTask();
			downloadTask.execute(url);

			googleMap.clear();
			googleMap.addMarker(new MarkerOptions().position(source).icon(
				BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			googleMap.addMarker(new MarkerOptions().position(destination).icon(
				BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(destination.latitude, destination.longitude))
				.zoom(12).build();
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			    Toast.makeText(MainActivity.this, "Route is send to Server and has been set", Toast.LENGTH_SHORT).show();
			    Button setRoute = (Button) findViewById(R.id.button1);
			    setRoute.setEnabled(false);
			    PollQRT pollQRT = new PollQRT();
			    UserInformation userInformation = new UserInformation();
			    userInformation.setName("Sumit");
			    userInformation.setPhonenumber(8861421989L);
			    // userInformation.setDeviceId(regid);
			    userInformation.setCord(userInformation.new Coordinates(gps.getLatitude(), gps.getLongitude()));
			    pollQRT.sendCurrentLocation(getApplicationContext(), getIntent(), userInformation, gps);
			}
			catch (Exception e) {
			    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

		    }
	    });
	    
	    Button sos = (Button) findViewById(R.id.button2);
	    sos.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v)
		    {
			UserInformation userInformation = new UserInformation();
			userInformation.setName("Sumit");
			// userInformation.setDeviceId(regid);
			userInformation.setPhonenumber(8861421989L);
			userInformation.setCord(userInformation.new Coordinates(gps.getLatitude(), gps.getLongitude()));
			PollQRT pollQRT = new PollQRT();
			pollQRT.sendSOS(getApplicationContext(), getIntent(), userInformation);
			Toast.makeText(getApplicationContext(), "SOS sent to server. We are sending help",
				Toast.LENGTH_LONG).show();

		    }
	    });
	}

    public String getDirectionsUrl(LatLng source, LatLng destination)
	{

	    // Origin of route
	    String str_origin = "origin=" + source.latitude + "," + source.longitude;

	    // Destination of route
	    String str_dest = "destination=" + destination.latitude + "," + destination.longitude;

	    // Sensor enabled
	    String sensor = "sensor=false";

	    // Building the parameters to the web service
	    String parameters = str_origin + "&" + str_dest + "&" + sensor;

	    // Output format
	    String output = "json";

	    // Building the url to the web service
	    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

	    return url;
	}

    /** A method to download json data from url */
    public String downloadUrl(String strUrl)
	    throws IOException
	{
	    String data = "";
	    InputStream iStream = null;
	    HttpURLConnection urlConnection = null;
	    try {
		URL url = new URL(strUrl);

		// Creating an http connection to communicate with url
		urlConnection = (HttpURLConnection) url.openConnection();

		// Connecting to url
		urlConnection.connect();

		// Reading data from url
		iStream = urlConnection.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

		StringBuffer sb = new StringBuffer();

		String line = "";
		while ((line = br.readLine()) != null) {
		    sb.append(line);
		}

		data = sb.toString();

		br.close();

	    }
	    catch (Exception e) {
		Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		Log.d("Exception while downloading url", e.toString());
	    }
	    finally {
		iStream.close();
		urlConnection.disconnect();
	    }
	    return data;
	}

    /** A class to download data from Google Directions URL */
    public class DownloadTask extends AsyncTask<String, Void, String> {

	// Downloading data in non-ui thread
	@Override
	protected String doInBackground(String... url)
	    {

		// For storing data from web service
		String data = "";

		try {
		    // Fetching the data from web service
		    data = downloadUrl(url[0]);
		}
		catch (Exception e) {
		    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		    Log.d("Background Task", e.toString());
		}
		return data;
	    }

	// Executes in UI thread, after the execution of
	// doInBackground()
	@Override
	protected void onPostExecute(String result)
	    {
		super.onPostExecute(result);

		ParserTask parserTask = new ParserTask();

		// Invokes the thread for parsing the JSON data
		parserTask.execute(result);

	    }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

	// Parsing the data in non-ui thread
	@Override
	protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
	    {

		JSONObject jObject;
		List<List<HashMap<String, String>>> routes = null;

		try {
		    jObject = new JSONObject(jsonData[0]);
		    DirectionsJSONParser parser = new DirectionsJSONParser();

		    // Starts parsing data
		    routes = parser.parse(jObject);
		}
		catch (Exception e) {
		    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		    e.printStackTrace();
		}
		return routes;
	    }

	// Executes in UI thread, after the parsing process
	@Override
	protected void onPostExecute(List<List<HashMap<String, String>>> result)
	    {
		ArrayList<LatLng> points = null;
		PolylineOptions lineOptions = null;

		// Traversing through all the routes
		for (int i = 0; i < result.size(); i++) {
		    points = new ArrayList<LatLng>();
		    lineOptions = new PolylineOptions();

		    // Fetching i-th route
		    List<HashMap<String, String>> path = result.get(i);

		    // Fetching all the points in i-th route
		    for (int j = 0; j < path.size(); j++) {
			HashMap<String, String> point = path.get(j);

			double lat = Double.parseDouble(point.get("lat"));
			double lng = Double.parseDouble(point.get("lng"));
			LatLng position = new LatLng(lat, lng);

			points.add(position);
		    }

		    // Adding all the points in the route to LineOptions
		    lineOptions.addAll(points);
		    lineOptions.width(5);
		    lineOptions.color(Color.RED);

		}

		// Drawing polyline in the Google Map for the i-th route
		googleMap.addPolyline(lineOptions);
	    }
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap()
	{
	    if (googleMap == null) {
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		// check if map is created successfully or not
		if (googleMap == null) {
		    Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		}
	    }
	}

    private boolean checkPlayServices()
	{
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
		if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
		    GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
		} else {
		    Log.i(TAG, "This device is not supported.");
		    finish();
		}
		return false;
	    }
	    return true;
	}

    private void storeRegistrationId(Context context, String regId)
	{
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}

    private SharedPreferences getGCMPreferences(Context context)
	{
	    return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}

    private static int getAppVersion(Context context)
	{
	    try {
		PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		return packageInfo.versionCode;
	    }
	    catch (NameNotFoundException e) {
		// should never happen
		throw new RuntimeException("Could not get package name: " + e);
	    }
	}

    private String getRegistrationId(Context context)
	{
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");

	    if (registrationId.isEmpty()) {
		Log.i(TAG, "Registration not found.");
		return "";
	    }

	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
		Log.i(TAG, "App version changed.");
		return "";
	    }
	    return registrationId;
	}

    @SuppressWarnings("unchecked")
    private void registerInBackground()
	{
	    new AsyncTask() {
		@Override
		protected Object doInBackground(Object... params)
		    {
			String msg = "";
			try {
			    if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			    }
			    // gcm.unregister();
			    regid = gcm.register(SENDER_ID);
			    msg = "Device registered, registration ID=" + regid;


			    storeRegistrationId(context, regid);
			    sendRegistrationIdToBackend();
			}
			catch (IOException ex) {
			    msg = "Error :" + ex.getMessage();

			}
			return msg;
		    }

		@SuppressWarnings("unused")
		protected void onPostExecute(String msg)
		    {
			mDisplay.append(msg + "\n");
		    }

	    }.execute(null, null, null);
	}

    protected void sendRegistrationIdToBackend()
	{
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    UserInformation.setDeviceId(registrationId);

	}

    @Override
    protected void onResume()
	{
	    super.onResume();
	    checkPlayServices();
	    initilizeMap();
	}

}