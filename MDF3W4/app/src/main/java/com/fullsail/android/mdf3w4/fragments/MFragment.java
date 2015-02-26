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
import android.widget.Toast;

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
    LatLng mPosition;

    Context context;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADDREQUEST && resultCode == getActivity().RESULT_OK) {
            String rTitle = data.getStringExtra("markerTitle");
            String rDetails = data.getStringExtra("markerDetails");
            String rImage = data.getStringExtra("markerImage");
            String action = data.getStringExtra("action");

            //mArticleList.add(new NewsArticle(rTitle, rAuthor, rDate));


            //MainListFragment nf = (MainListFragment) getFragmentManager().findFragmentById(R.id.container);
            //nf.updateListData();

            //writeFile();

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

                        mPosition = location;
                        Intent addIntent = new Intent(getActivity().getApplicationContext(), AddActivity.class);
                        addIntent.putExtra("Add", "From_MFragment");
                        startActivityForResult(addIntent, ADDREQUEST);

                        Log.i(TAG, "To AddActivity from MFragment: " + mPosition);
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

    private void readFile() {

        /*
        try {
            FileInputStream fin = openFileInput(saveFile);
            ObjectInputStream oin = new ObjectInputStream(fin);
            mArticleList = (ArrayList<NewsArticle>) oin.readObject();
            oin.close();

        } catch (Exception e) {
            Log.e(TAG, "There was an error creating the array");
        }
        */
    }


    // Creates local storage file
    private void writeFile() {

        /*
        try {
            FileOutputStream fos = openFileOutput(saveFile, this.MODE_PRIVATE);


            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mArticleList);
            Log.i(TAG, "Object Saved Successfully");
            oos.close();

        } catch (Exception e) {
            Log.e(TAG, "Save Unsuccessful");
        }

        */
    }

}
