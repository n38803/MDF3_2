package fullsail.com.mdf3_w3;

/**
 * Created by shaunthompson on 2/17/15.
 */

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Handler;

import fullsail.com.mdf3_w3.dataclass.NewsArticle;
import fullsail.com.mdf3_w3.fragments.AddFragment;
import fullsail.com.mdf3_w3.widgetclasses.CollectionWidgetProvider;


public class AddActivity extends Activity {

    public static final String ACTION_ADD_ARTICLE = "fullsail.com.mdf3_w3.ACTION_ADD_ARTICLE";

    public TextView inputTitle;
    public TextView inputAuthor;
    public TextView inputDate;

    private final String saveFile = "MDF3W3.txt";
    private final String TAG = "ADD ACTIVITY";

    CollectionWidgetProvider mAppReceiver = new CollectionWidgetProvider();

    private ArrayList<NewsArticle> mArticleList;

    public String aTitle;
    public String aAuthor;
    public String aDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Check to see whether or not there is a saved instance of the fragment
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new AddFragment())
                    .commit();
        }


        readFile();


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
        inputAuthor = (TextView) findViewById(R.id.inputAuthor);
        inputDate = (TextView) findViewById(R.id.inputDate);

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



    }

    // reset inputs
    private void clearDisplay(){
        inputTitle = (TextView) findViewById(R.id.inputTitle);
        inputAuthor = (TextView) findViewById(R.id.inputAuthor);
        inputDate = (TextView) findViewById(R.id.inputDate);

        inputTitle.setText("");
        inputAuthor.setText("");
        inputDate.setText("");
    }

    private void readFile() {


        try {
            FileInputStream fin = openFileInput(saveFile);
            ObjectInputStream oin = new ObjectInputStream(fin);
            mArticleList = (ArrayList<NewsArticle>) oin.readObject();
            oin.close();

        } catch (Exception e) {
            Log.e(TAG, "There was an error creating the array");
        }
    }


    // Creates local storage file
    private void writeFile() {

        try {
            FileOutputStream fos = openFileOutput(saveFile, this.MODE_PRIVATE);


            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mArticleList);
            Log.i(TAG, "Object Saved Successfully");
            oos.close();

        } catch (Exception e) {
            Log.e(TAG, "Save Unsuccessful");
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}