package com.fullsail.android.mdf3w4;

/**
 * Created by shaunthompson on 2/25/15.
 */

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.fullsail.android.mdf3w4.fragments.MFragment;


public class MainActivity extends FragmentActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* // Check to see whether or not there is a saved instance of the fragment
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MFragment())
                    .commit();
        }
        */

        MFragment frag = new MFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, frag).commit();
    }





    @Override
    protected void onResume() {
        super.onResume();
    }

}
