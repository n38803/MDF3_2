package com.fullsail.android.mdf3w4.fragments;

/**
 * Created by shaunthompson on 2/25/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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


public class MFragment extends MapFragment implements OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener, LocationListener {

    final String TAG = "MFragment.java";

    public static final int ADDREQUEST = 2;
    private static final int REQUEST_ENABLE_GPS = 0x02001;

    private final String saveFile = "MDF3W4.txt";

    private ArrayList<LocationClass> mLocationList;

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

        // verify whether or not there is a saved instance of coordinates
        readFile();

        // TODO- GRAB CURRENT LOCATION
        locMgr = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        // create location object
        mLocationList = new ArrayList<LocationClass>();

        // create map object
        mMap = getMap();

        // verify object exists
        if (mMap != null){

            //currentPosition = (new LatLng(currentLat, currentLng));

            // zoom into the map
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.590647,-81.304510), 17));

            // statically add markers to map
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.590647,-81.304510)).title("MDVBS Faculty Offices"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.591748,-81.305910)).title("Crispers"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.595842,-81.304188)).title("Full Sail Live"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.596591,-81.301302)).title("Advising"));

            mMap.setInfoWindowAdapter(new MarkerAdapter());
            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnMapLongClickListener(this);



        }
        else
        {
            Log.e(TAG, "Map = Null!");
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADDREQUEST && resultCode == getActivity().RESULT_OK) {
            String rTitle = data.getStringExtra("markerTitle");
            String rDetails = data.getStringExtra("markerDetails");
            String rImage = data.getStringExtra("markerImage");
            String action = data.getStringExtra("action");

            mLocationList.add(new LocationClass(rTitle, rDetails, mPosition, rImage));


            //MainListFragment nf = (MainListFragment) getFragmentManager().findFragmentById(R.id.container);
            //nf.updateListData();

            writeFile();

            if (action.equals("add")) {
                Toast.makeText(getActivity().getApplicationContext(), "Marker added for: " + rTitle, Toast.LENGTH_LONG).show();

                // todo- set coordinates to be dynamic
                mMap.addMarker(new MarkerOptions().position(mPosition).title(rTitle));



            }
        }
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {
        new AlertDialog.Builder(getActivity())
                .setTitle(marker.getTitle())

                //todo - set message to be dynamic
                .setMessage("MAKE THIS DYNAMIC")
                .setPositiveButton("Details", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent detailIntent = new Intent(getActivity().getApplicationContext(), DetailsActivity.class);
                        detailIntent.putExtra(DetailsActivity.EXTRA_ITEM, "TESTTTT");
                        startActivity(detailIntent);


                        Log.i(TAG, "To DetailActivity from MFragment: " + mPosition);
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

                        mPosition = location;
                        Intent addIntent = new Intent(getActivity().getApplicationContext(), AddActivity.class);
                        addIntent.putExtra("Add", "From_LongPress");
                        startActivityForResult(addIntent, ADDREQUEST);

                        Log.i(TAG, "To AddActivity from MFragment: " + mPosition);
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
            oin.close();

        } catch(Exception e) {
            Log.e(TAG, "There are no files to pull");

            Toast.makeText(getActivity().getApplicationContext(), "No Locations Saved - Please add new location", Toast.LENGTH_LONG).show();

            // static population of data
            mLocationList = new ArrayList<LocationClass>();
            mLocationList.add(new LocationClass("Test: Equator", "Test Details", (new LatLng(0,0)), "Test Image"));

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
