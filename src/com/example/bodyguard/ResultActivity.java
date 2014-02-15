package com.example.bodyguard;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ResultActivity extends Activity {

    public static String phNumber;
    public static String latitude;
    public static String longitude;

    // Google Map
    private GoogleMap    googleMap;

    // GPSTracker class
    GPSTracker	   gps;

    @Override
    public boolean onTouchEvent(MotionEvent event)
	{
	    // TODO Auto-generated method stub
	    setContentView(R.layout.result);
	    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + "\nLong: ", Toast.LENGTH_LONG).show();
	    return super.onTouchEvent(event);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.result);
	    initilizeMap();
	    gps = new GPSTracker(this);
	    if (gps.canGetLocation()) {
		Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
		MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).title(
"Victim's Location, Mobile#" + phNumber).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).zoom(14).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googleMap.getUiSettings().setZoomGesturesEnabled(true);
		googleMap.setMyLocationEnabled(true);
		googleMap.addMarker(marker);
		TextView setnumber = (TextView) findViewById(R.id.number);
		setnumber.setText("Victim's Mobile: " + phNumber);
		// TextView setname = (TextView) findViewById(R.id.name);
		// Toast.makeText(getApplicationContext(),
		// "Your Location is - \nLat: " + "\nLong: ",
		// Toast.LENGTH_LONG).show();

	    }
	}

    protected void onClick(View view)
	{
	    // TODO Auto-generated method stub
	    // super.onCreate(savedInstanceState);
	    setContentView(R.layout.result);
	    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + "\nLong: ", Toast.LENGTH_LONG).show();

	}

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
}
