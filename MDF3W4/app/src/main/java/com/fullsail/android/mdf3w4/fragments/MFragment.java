package com.fullsail.android.mdf3w4.fragments;

/**
 * Created by shaunthompson on 2/25/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullsail.android.mdf3w4.AddActivity;
import com.fullsail.android.mdf3w4.MainActivity;
import com.fullsail.android.mdf3w4.R;
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


public class MFragment extends MapFragment implements OnInfoWindowClickListener, OnMapClickListener {

    final String TAG = "MFragment.java";

    public static final int ADDREQUEST = 2;

    GoogleMap mMap;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // create map object
        mMap = getMap();

        // verify object exists
        if (mMap != null){

            // zoom into the map
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.593770, -81.303797), 17));

            // statically add markers to map
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.590647,-81.304510)).title("MDVBS Faculty Offices"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.591748,-81.305910)).title("Crispers"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.595842,-81.304188)).title("Full Sail Live"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.596591,-81.301302)).title("Advising"));

            mMap.setInfoWindowAdapter(new MarkerAdapter());
            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnMapClickListener(this);

        }
        else
        {
            Log.e(TAG, "Map = Null!");
        }

    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Marker Clicked")
                .setMessage("You clicked on: " + marker.getTitle())
                .setPositiveButton("Close", null)
                .setNegativeButton("Remove", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marker.remove();
                    }
                })
                .show();
    }

    @Override
    public void onMapClick(final LatLng location) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Map Clicked")
                .setMessage("Add new marker here?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //mMap.addMarker(new MarkerOptions().position(location).title("New Marker"));

                        Intent addIntent = new Intent(getActivity(), AddActivity.class);
                        addIntent.putExtra("Add", "From_MainActivity");
                        startActivityForResult(addIntent, ADDREQUEST);

                        Log.i(TAG, "Button Clicked");
                    }
                })
                .show();
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

}
