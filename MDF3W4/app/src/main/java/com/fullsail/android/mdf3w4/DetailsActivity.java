package com.fullsail.android.mdf3w4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullsail.android.mdf3w4.dataclass.LocationClass;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by shaunthompson on 2/25/15.
 */
public class DetailsActivity extends Activity {

    private final String TAG = "DETAIL ACTIVITY";

    private LocationClass mLocation;

    public static final String EXTRA_ITEM = "com.fullsail.android.mdf3w4.DetailsActivity.EXTRA_ITEM";
    //public static final String APP_ITEM = "com.fullsail.android.DetailsActivity.APP_ITEM";

    public String dTitle;
    public String dDetails;
    public String dImage;
    public LatLng dLatLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Grabs intent from main activity
        Intent dIntent = getIntent();
        //mLocation = (LocationClass) dIntent.getSerializableExtra(EXTRA_ITEM);



        dTitle = dIntent.getStringExtra("Title");
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText(dTitle);

        //tv = (TextView) findViewById(R.id.details);
        //tv.setText(mLocation.getDetail());


        /*if (mLocation == null) {
            finish();
            return;
        }
        */



    }


}
