package com.fullsail.android.locationexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener {
	
	private static final int REQUEST_ENABLE_GPS = 0x02001;

	TextView mLatitude;
	TextView mLongitude;
	
	LocationManager mManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mLatitude = (TextView)findViewById(R.id.latitude);
		mLongitude = (TextView)findViewById(R.id.longitude);
		
		mManager = (LocationManager)getSystemService(LOCATION_SERVICE);
	}
	
	private void enableGps() {
		if(mManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
			
			Location loc = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        if(loc != null) {
	        	mLatitude.setText("" + loc.getLatitude());
	        	mLongitude.setText("" + loc.getLongitude());
	        }
	        
		} else {
			new AlertDialog.Builder(this)
			.setTitle("GPS Unavailable")
			.setMessage("Please enable GPS in the system settings.")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivityForResult(settingsIntent, REQUEST_ENABLE_GPS);
				}
			
			})
			.show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enableGps();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		enableGps();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		mLatitude.setText("" + location.getLatitude());
		mLongitude.setText("" + location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
}
