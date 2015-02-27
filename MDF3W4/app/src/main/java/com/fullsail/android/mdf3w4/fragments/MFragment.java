package com.fullsail.android.mdf3w4.fragments;

/**
 * Created by shaunthompson on 2/25/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.android.mdf3w4.AddActivity;
import com.fullsail.android.mdf3w4.DetailsActivity;
import com.fullsail.android.mdf3w4.MainActivity;
import com.fullsail.android.mdf3w4.R;
import com.fullsail.android.mdf3w4.dataclass.LocationClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.HashMap;


public class MFragment extends MapFragment implements OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener, LocationListener {

    final String TAG = "MFragment.java";

    public static final int ADDREQUEST = 2;
    private static final int REQUEST_ENABLE_GPS = 0x02001;

    private final String saveFile = "MDF3W4.txt";


    private ArrayList<LocationClass> mLocationList = new ArrayList<LocationClass>();
    private HashMap<String, LocationClass> mHashMap;



    GoogleMap mMap;
    LocationManager locMgr;
    LatLng mPosition;
    LatLng currentPosition;
    Double currentLat;
    Double currentLng;

    Context context;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mHashMap = new HashMap<String, LocationClass>();
        mLocationList = new ArrayList<LocationClass>();




        // TODO- GRAB CURRENT LOCATION
        locMgr = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        enableGps();

        // create location object
        //mLocationList = new ArrayList<LocationClass>();

        // create map object
        mMap = getMap();

        // verify whether or not there is a saved instance of coordinates
        readFile();

        // verify object exists
        if (mMap != null){


            // zoom into the map
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLng), 17));

            createMarkers(mLocationList);

            mMap.setInfoWindowAdapter(new MarkerAdapter());
            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnMapLongClickListener(this);



        }
        else
        {
            Log.e(TAG, "Map = Null!");
        }

        Button add = (Button) getActivity().findViewById(R.id.button);

        add.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        // TODO GRAB CURRENT LOCATION AND PUT INTO INTENT
                        Intent buttonIntent = new Intent(getActivity().getApplicationContext(), AddActivity.class);
                        buttonIntent.putExtra("Add", "From_Button");
                        buttonIntent.putExtra("Latitude", currentLat);
                        buttonIntent.putExtra("Longitude", currentLng);
                        startActivityForResult(buttonIntent, ADDREQUEST);

                        Log.i(TAG, "To AddActivity from MFragment Button: ");

                    }
                }
        );

    }

    private void createMarkers(ArrayList<LocationClass> locations)
    {
        mMap = getMap();
        //mHashMap = new HashMap<String, LocationClass>();


        for(int i=0;i<locations.size();i++)
        {
            for (LocationClass location : locations)
            {

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLat(), location.getLong()))
                        .title(location.getTitle()));
                mHashMap.put(location.getTitle(), location);

                Log.d(TAG, "Title: " + location.getTitle() + "n/Lat: " + location.getLat() + "n/Long: " + location.getLong());
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADDREQUEST && resultCode == getActivity().RESULT_OK) {
            String rTitle = data.getStringExtra("markerTitle");
            String rDetails = data.getStringExtra("markerDetails");
            String rLong = data.getStringExtra("markerLong");
            String rLat = data.getStringExtra("markerLat");
            String rImage = data.getStringExtra("markerImage");
            String action = data.getStringExtra("action");

            Double longitude = Double.parseDouble(rLong);
            Double latitude = Double.parseDouble(rLat);

            mLocationList.add(new LocationClass(rTitle, rDetails, latitude, longitude, rImage));



            //MainListFragment nf = (MainListFragment) getFragmentManager().findFragmentById(R.id.container);
            //nf.updateListData();



            if (action.equals("add")) {
                Toast.makeText(getActivity().getApplicationContext(), "Marker added for: " + rTitle, Toast.LENGTH_LONG).show();

                // todo- set coordinates to be dynamic
                //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(rTitle));

                writeFile();
                createMarkers(mLocationList);

            }
        }
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {
        String key = marker.getTitle();
        Log.e(TAG, "KEY: " + key);

        new AlertDialog.Builder(getActivity())
                .setTitle(mHashMap.get(key).getTitle())
                //todo - set message to be dynamic
                .setMessage(mHashMap.get(key).getDetail())
                        .setPositiveButton("Details", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // TODO - CALL OBJECT LOCATION BASED ON TITLE STRING

                                String key = marker.getTitle();

                                Intent detailIntent = new Intent(getActivity().getApplicationContext(), DetailsActivity.class);
                                //detailIntent.putExtra(DetailsActivity.EXTRA_ITEM, mLocationList.get(0));
                                detailIntent.putExtra("Title", mHashMap.get(key).getTitle());
                                detailIntent.putExtra("Details", mHashMap.get(key).getDetail());
                                detailIntent.putExtra("Image", mHashMap.get(key).getImage());
                                startActivity(detailIntent);


                                Log.i(TAG, "To DetailActivity from MFragment: " + mHashMap.get(key).getImage());
                            }
                        })
                        .setNeutralButton("Remove", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                marker.remove();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
    }

    @Override
    public void onMapLongClick(final LatLng location) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Map Clicked")
                .setMessage("Add new marker here?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //mMap.addMarker(new MarkerOptions().position(location).title("New Marker"));

                        //mPosition = location;
                        Intent addIntent = new Intent(getActivity().getApplicationContext(), AddActivity.class);
                        addIntent.putExtra("Add", "From_LongPress");
                        addIntent.putExtra("Latitude", location.latitude);
                        addIntent.putExtra("Longitude", location.longitude);
                        startActivityForResult(addIntent, ADDREQUEST);

                        Log.i(TAG, "To AddActivity from MFragment: " + location.longitude + " // " + location.latitude);
                    }
                })
                .show();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

        TextView mText;

        public MarkerAdapter() {
            mText = new TextView(getActivity());
        }

        @Override
        public View getInfoContents(Marker marker) {
            // defines what shows up inside the info window
            mText.setText(marker.getTitle());
            return mText;
        }

        @Override
        public View getInfoWindow(Marker marker) {

            // defines what the window background looks like

            return null;
        }
    }


    public ArrayList<LocationClass> getArticles() {
        return mLocationList;
    }

    private void enableGps() {
        if(locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

            Location loc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null) {
                currentLng = loc.getLongitude();
                currentLat = loc.getLatitude();
            }

        } else {
            new AlertDialog.Builder(getActivity().getApplicationContext())
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

    private void readFile() {


        try {
            FileInputStream fin = getActivity().openFileInput(saveFile);
            ObjectInputStream oin = new ObjectInputStream(fin);
            mLocationList = (ArrayList<LocationClass>) oin.readObject();
            Log.e(TAG, "Files loaded successfully: " + mHashMap);
            oin.close();

        } catch(Exception e) {
            Log.e(TAG, "There are no files to pull");

            Toast.makeText(getActivity().getApplicationContext(), "No Locations Saved - Please add new location", Toast.LENGTH_LONG).show();

            // static population of data
            // Initialize the HashMap for Markers and MyMarker object


            //mLocationList.add(new LocationClass("United States", "This is the USA", Double.parseDouble("33.7266622"), Double.parseDouble("-87.1469829"), "No Image"));

            //mLocationList.add(new LocationClass("England", "The Redcoats are coming!", Double.parseDouble("52.4435047"), Double.parseDouble("-3.4199249"), "No Image"));


            createMarkers(mLocationList);

            writeFile();
        }
    }


    // Creates local storage file
    private void writeFile() {

        try {
            FileOutputStream fos = getActivity().openFileOutput(saveFile, getActivity().MODE_PRIVATE);


            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mLocationList);
            Log.i(TAG, "Object Saved Successfully");
            oos.close();

        } catch (Exception e) {
            Log.e(TAG, "Save Unsuccessful");
        }
    }

}
