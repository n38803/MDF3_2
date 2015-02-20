package fullsail.com.mdf3_w3;

/**
 * Created by shaunthompson on 2/17/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import fullsail.com.mdf3_w3.dataclass.NewsArticle;


public class DetailsActivity extends Activity {

    private final String TAG = "DETAIL ACTIVITY";

    private NewsArticle mArticle;

    public static final String EXTRA_ITEM = "fullsail.com.mdf3_w3.DetailsActivity.EXTRA_ITEM";
    //public static final String APP_ITEM = "com.fullsail.android.DetailsActivity.APP_ITEM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        mArticle = (NewsArticle) intent.getSerializableExtra(EXTRA_ITEM);


        // detect whether or not intent was called from application
        if (intent.getBooleanExtra("APP", false))
        {
            // intent is called from widget
            Log.d(TAG, "Detail Activity Launched from Widget");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear top of stack so back button returns home
        }
        else if (intent.getBooleanExtra("APP", true)){
            // intent is called from activity - do nothing.
            Log.d(TAG, "Detail Activity Launched from App");
        }

        if (mArticle == null) {
            finish();
            return;
        }



        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText(mArticle.getTitle());

        tv = (TextView) findViewById(R.id.author);
        tv.setText(mArticle.getAuthor());

        tv = (TextView) findViewById(R.id.date);
        tv.setText(mArticle.getDate());
    }

}

