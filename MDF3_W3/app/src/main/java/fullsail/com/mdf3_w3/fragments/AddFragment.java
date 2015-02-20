package fullsail.com.mdf3_w3.fragments;

/**
 * Created by shaunthompson on 2/17/15.
 */

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import fullsail.com.mdf3_w3.R;
import fullsail.com.mdf3_w3.dataclass.NewsArticle;
import fullsail.com.mdf3_w3.widgetclasses.CollectionWidgetProvider;


public class AddFragment extends Fragment {


    private final String TAG = "ADD FRAGMENT";

    public static final String ACTION_ADD_ARTICLE = "fullsail.com.mdf3_w3.ACTION_ADD_ARTICLE";

    public TextView inputTitle;
    public TextView inputAuthor;
    public TextView inputDate;

    private final String saveFile = "MDF3W3.txt";

    CollectionWidgetProvider mAppReceiver = new CollectionWidgetProvider();

    private ArrayList<NewsArticle> mArticleList;

    public String aTitle;
    public String aAuthor;
    public String aDate;






    public AddFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // pull info from file
        readFile();

        // assign references
        Button save     = (Button) getActivity().findViewById(R.id.submitButton);
        Button cancel   = (Button) getActivity().findViewById(R.id.cancelButton);

        // create onClickListeners for each button w/execution of corresponding methods
        save.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        // call save method
                        onSave();

                    }
                }
        );

        cancel.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        // call save method
                        onCancel();

                    }
                }
        );



    }

    public void onCancel(){

        // Grabs intent from main activity
        Intent addIntent = getActivity().getIntent();

        clearDisplay();
        getActivity().finish();


    }




    public void onSave(){

        // assign references
        inputTitle = (TextView) getActivity().findViewById(R.id.inputTitle);
        inputAuthor = (TextView) getActivity().findViewById(R.id.inputAuthor);
        inputDate = (TextView) getActivity().findViewById(R.id.inputDate);

        // assign input to string variables
        aTitle = inputTitle.getText().toString();
        aAuthor = inputAuthor.getText().toString();
        aDate = inputDate.getText().toString();

        // Grabs intent from main activity
        Intent aIntent = getActivity().getIntent();

        // assign intent extra to string
        String iRequest = aIntent.getExtras().getString("Add");

        // conditional to determine which intent launched activity
        if(iRequest.equals("From_Widget"))
        {
            Log.d(TAG, "Saving information to disk for Widget update");

            // add info to array & save to file
            mArticleList.add(new NewsArticle(aTitle, aAuthor, aDate));
            writeFile();

            // force update
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(AddFragment.this.getActivity());
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(AddFragment.this.getActivity(), CollectionWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.article_list);

        }
        else if(iRequest.equals("From_MainActivity"))
        {
            Log.d(TAG, "Sending information to Main Activity");

            aIntent.putExtra("articleTitle", aTitle);
            aIntent.putExtra("articleAuthor", aAuthor);
            aIntent.putExtra("articleDate", aDate);
            aIntent.putExtra("action", "add");
            getActivity().setResult(getActivity().RESULT_OK, aIntent);
        }




        clearDisplay();
        getActivity().finish();



    }

    // reset inputs
    private void clearDisplay(){
        inputTitle = (TextView) getActivity().findViewById(R.id.inputTitle);
        inputAuthor = (TextView) getActivity().findViewById(R.id.inputAuthor);
        inputDate = (TextView) getActivity().findViewById(R.id.inputDate);

        inputTitle.setText("");
        inputAuthor.setText("");
        inputDate.setText("");
    }

    private void readFile() {


        try {
            FileInputStream fin = getActivity().openFileInput(saveFile);
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
            FileOutputStream fos = getActivity().openFileOutput(saveFile, AddFragment.this.getActivity().MODE_PRIVATE);


            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mArticleList);
            Log.i(TAG, "Object Saved Successfully");
            oos.close();

        } catch (Exception e) {
            Log.e(TAG, "Save Unsuccessful");
        }


    }

}

