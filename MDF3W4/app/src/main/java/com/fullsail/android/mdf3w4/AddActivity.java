package com.fullsail.android.mdf3w4;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fullsail.android.mdf3w4.dataclass.LocationClass;
import com.fullsail.android.mdf3w4.fragments.AddFragment;
import com.fullsail.android.mdf3w4.fragments.MFragment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by shaunthompson on 2/25/15.
 */
public class AddActivity extends Activity {

    public static final String ACTION_ADD_ARTICLE = "fullsail.com.mdf3_w3.ACTION_ADD_ARTICLE";

    public TextView inputTitle;
    public TextView inputDetails;
    //public ImageView inputImage;

    private final String saveFile = "MDF3W4.txt";
    private final String TAG = "ADD ACTIVITY";


    private ArrayList<LocationClass> mLocationList;

    public String aTitle;
    public String aDetails;
    //public String aImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Check to see whether or not there is a saved instance of the fragment
        if (savedInstanceState == null) {
            AddFragment frag = new AddFragment();
            getFragmentManager().beginTransaction().replace(R.id.container, frag).commit();
        }




    }

    public void onCancel(View v){
        // Grabs intent from main activity
        Intent addIntent = getIntent();

        clearDisplay();
        finish();


    }




    public void onSave(View v){



        // assign references
        inputTitle = (TextView) findViewById(R.id.inputTitle);
        inputDetails = (TextView) findViewById(R.id.inputDetails);


        /*

        // assign input to string variables
        aTitle = inputTitle.getText().toString();
        aAuthor = inputAuthor.getText().toString();
        aDate = inputDate.getText().toString();

        // Grabs intent from main activity
        Intent aIntent = getIntent();

        // assign intent extra to string
        String iRequest = aIntent.getExtras().getString("Add");

        // conditional to determine which intent launched activity
        if(iRequest.equals("From_Widget"))
        {
            Log.d(TAG, "Saving information to disk for Widget update");

            mArticleList.add(new NewsArticle(aTitle, aAuthor, aDate));
            writeFile();


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this, CollectionWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.article_list);

        }
        else if(iRequest.equals("From_MainActivity"))
        {
            Log.d(TAG, "Sending information to Main Activity");

            aIntent.putExtra("articleTitle", aTitle);
            aIntent.putExtra("articleAuthor", aAuthor);
            aIntent.putExtra("articleDate", aDate);
            aIntent.putExtra("action", "add");
            setResult(RESULT_OK, aIntent);
        }




        clearDisplay();
        finish();
        */

    }

    // reset inputs
    private void clearDisplay(){
        inputTitle = (TextView) findViewById(R.id.inputTitle);
        inputDetails = (TextView) findViewById(R.id.inputDetails);


        inputTitle.setText("");
        inputDetails.setText("");

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




    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
