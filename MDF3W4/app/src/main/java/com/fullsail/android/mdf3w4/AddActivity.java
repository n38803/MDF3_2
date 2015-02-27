package com.fullsail.android.mdf3w4;

/**
 * Created by shaunthompson on 2/25/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.fullsail.android.mdf3w4.fragments.AddFragment;




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
