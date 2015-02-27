package com.fullsail.android.mdf3w4;
/**
 * Created by shaunthompson on 2/25/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;



public class DetailsActivity extends Activity {

    private final String TAG = "DETAIL ACTIVITY";

    public String dTitle;
    public String dDetails;
    public String dImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Grabs intent from main activity
        Intent dIntent = getIntent();




        dTitle = dIntent.getStringExtra("Title");
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(dTitle);

        dDetails = dIntent.getStringExtra("Details");
        TextView details = (TextView) findViewById(R.id.details);
        details.setText(dDetails);

        dImage = dIntent.getStringExtra("Image");
        ImageView image = (ImageView) findViewById(R.id.image);
        image.setImageBitmap(BitmapFactory.decodeFile(dImage));




    }


}
