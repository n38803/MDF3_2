package com.fullsail.android.mdf3w4;

/**
 * Created by shaunthompson on 2/25/15.
 */

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fullsail.android.mdf3w4.dataclass.LocationClass;
import com.fullsail.android.mdf3w4.fragments.AddFragment;
import com.fullsail.android.mdf3w4.fragments.MFragment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;



public class AddActivity extends Activity {

    final String TAG = "ADD-ACTIVITY";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Check to see whether or not there is a saved instance of the fragment
        if (savedInstanceState == null) {
            AddFragment frag = new AddFragment();
            getFragmentManager().beginTransaction().replace(R.id.addContainer, frag).commit();

            Log.i("ADD-ACTIVITY", "Launched");
        }


    }



    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
