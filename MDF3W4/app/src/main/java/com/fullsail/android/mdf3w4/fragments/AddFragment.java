package com.fullsail.android.mdf3w4.fragments;

/**
 * Created by shaunthompson on 2/25/15.
 */

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.android.mdf3w4.R;
import com.fullsail.android.mdf3w4.dataclass.LocationClass;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddFragment extends Fragment {

    public static final String ACTION_ADD_ARTICLE = "com.fullsail.android.mdf3w4.ACTION_ADD_ARTICLE";
    public static final int REQUEST_TAKE_PICTURE = 0x01001;


    public TextView inputTitle;
    public TextView inputDetails;
    public String inputImageName;
    //public ImageView inputImage;

    Button save;
    Button cancel;
    Button capture;
    ImageView preview;
    Uri previewUri;

    private final String saveFile = "MDF3W4.txt";
    private final String TAG = "ADD-FRAGMENT";


    private ArrayList<LocationClass> mLocationList;

    public String aTitle;
    public String aDetails;
    //public String aImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Log.i(TAG, "Launched");

        save    = (Button) getActivity().findViewById(R.id.save);
        cancel  = (Button) getActivity().findViewById(R.id.cancel);
        capture = (Button) getActivity().findViewById(R.id.capture);
        preview = (ImageView) getActivity().findViewById(R.id.imageView);

        save.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        onSave();

                    }
                }
        );

        cancel.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        onCancel();

                    }
                }
        );

        capture.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        // TODO- IMAGE CAPTURE
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        previewUri = getOutputUri();
                        if(previewUri != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, previewUri);
                        }
                        startActivityForResult(intent, REQUEST_TAKE_PICTURE);

                        // TODO- SET CAPTURED IMAGE TO IMAGEVIEW

                    }
                }
        );




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode == getActivity().RESULT_OK) {
            if(previewUri != null) {
                preview.setImageBitmap(BitmapFactory.decodeFile(previewUri.getPath()));
                addImageToGallery(previewUri);

                Log.i(TAG, "Picture Captured");
            } else {
                Log.e(TAG, "Picture Canceled");
                preview.setImageBitmap((Bitmap)data.getParcelableExtra("data"));
            }
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save) {
            onSave();

        }
        else if(item.getItemId() == R.id.cancel){
            onCancel();
        }
        return true;
    }

    private void addImageToGallery(Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        getActivity().sendBroadcast(scanIntent);
    }

    private Uri getOutputUri() {
        String imageName = new SimpleDateFormat("MMddyyyy_HHmmss")
                .format(new Date(System.currentTimeMillis()));
        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // Creating our own folder in the default directory.
        File appDir = new File(imageDir, "MDF3W4_APP");
        appDir.mkdirs();
        File image = new File(appDir, imageName + ".jpg");
        inputImageName = (imageName + ".jpg");
        try {
            image.createNewFile();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(image);
    }

    public void onCancel(){

        clearDisplay();
        getActivity().finish();


    }

    public void onSave(){



        // assign references
        inputTitle = (TextView) getActivity().findViewById(R.id.inputTitle);
        inputDetails = (TextView) getActivity().findViewById(R.id.inputDetails);


        // assign input to string variables
        aTitle      = inputTitle.getText().toString();
        aDetails    = inputDetails.getText().toString();

        // Grabs intent from main activity
        Intent aIntent = getActivity().getIntent();

        // assign intent extra to string
        String iRequest = aIntent.getExtras().getString("Add");

        // conditional to determine which intent launched activity
        if(iRequest.equals("From_MFragment"))
        {
            Log.d(TAG, "Sending information to Map Fragment");

            aIntent.putExtra("markerTitle", aTitle);
            aIntent.putExtra("markerDetails", aTitle);
            aIntent.putExtra("markerImage", inputImageName);
            aIntent.putExtra("action", "add");
            getActivity().setResult(getActivity().RESULT_OK, aIntent);
        }




        clearDisplay();
        getActivity().finish();


    }

    // reset inputs
    private void clearDisplay(){
        inputTitle = (TextView) getActivity().findViewById(R.id.inputTitle);
        inputDetails = (TextView) getActivity().findViewById(R.id.inputDetails);


        inputTitle.setText("");
        inputDetails.setText("");

    }

}
