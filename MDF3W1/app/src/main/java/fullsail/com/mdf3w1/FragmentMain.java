package fullsail.com.mdf3w1;

import android.app.Fragment;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.os.Handler;

/**
 * Created by shaunthompson on 2/8/15.
 */
public class FragmentMain extends Fragment implements ServiceConnection {

    final String TAG = "Main_Activity";


    ImageButton play;
    ImageButton stop;
    ImageButton forward;
    ImageButton back;
    SeekBar sBar;
    Button loop;

    PlayerService pService;
    boolean pBound;
    boolean checkLoop = false;

    int sPosition;
    int sLength;
    int sCurrent;

    View view;


    private Handler progressHandler = new Handler();



    public boolean portraitMode;

    public void getOrientation(){

        // vertical returns 1 // horizontal returns 2
        int orientation = getResources().getConfiguration().orientation;

        // DEVICE IS IN HORIZONTAL
        if (orientation == 2) {
            portraitMode = false;
            Log.i(TAG, "Orientation is Horizontal");

        }
        // DEVICE IS IN HORIZONTAL
        else if (orientation == 1){
            portraitMode = true;
            Log.i(TAG, "Orientation is Vertical");


        }
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {

        // identify orientation
        getOrientation();


        // Create and return view for this fragment.
        if (portraitMode == true){ // portrait/vertical view
            view = _inflater.inflate(R.layout.fragment_main, _container, false);
        }
        else if (portraitMode == false){ // landscape/horizontal view
            view = _inflater.inflate(R.layout.fragment_landscape, _container, false);
        }

        return view;
    }

    public void setSongInfo(){

        // Assign view references
        final TextView artist = (TextView) getActivity().findViewById(R.id.artist);
        final TextView title = (TextView) getActivity().findViewById(R.id.title);
        final ImageView art = (ImageView) getActivity().findViewById(R.id.image);


        // set current song information to views
        artist.setText(pService.getArtist());
        title.setText(pService.getTitle());
        art.setImageResource(pService.getArt());



    }

    public void getSongMetrics(){
        sLength = pService.getSongLength();
        sPosition = pService.getSongPosition();
    }

    public void resetProgress(){

        SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.hseek);
        sBar.setProgress(0);
        sLength = 0;
        sPosition = 0;
        sCurrent = 0;

    }


    private Runnable updateTime = new Runnable() {

        @Override
        public void run() {

            SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.hseek);
            TextView time = (TextView) getActivity().findViewById(R.id.hsongProgress);

            if(pService.isNotNull())
            {
                DateFormat dformat = new SimpleDateFormat("mm:ss");
                String current = dformat.format(pService.getSongPosition());
                time.setText("[ " + current + " ] ");


                sCurrent = pService.getSongPosition() / 1000;
                sBar.setProgress(sCurrent);
                progressHandler.postDelayed(this, 1000);
            }
            else if (!pService.isNotNull())
            {
                sBar.setProgress(0);
            }

        }
    };

    private void updateProgress(){
        progressHandler.postDelayed(updateTime, 100);
    }



    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        Log.i(TAG, "Activity Created");

        // initiate intent & assign to service/binder
        Intent intent = new Intent(FragmentMain.this.getActivity(), PlayerService.class);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);



    }



    @Override
    public void onServiceConnected(ComponentName name, final IBinder service) {
        Log.i(TAG, "Service Connected");

        // create binder
        PlayerService.PlayerServiceBinder binder = (PlayerService.PlayerServiceBinder)service;
        pService = binder.getService();
        pBound = true;


        // assign references
        play    = (ImageButton) getActivity().findViewById(R.id.play);
        stop    = (ImageButton) getActivity().findViewById(R.id.stop);
        forward = (ImageButton) getActivity().findViewById(R.id.forward);
        back    = (ImageButton) getActivity().findViewById(R.id.back);
        sBar    = (SeekBar) getActivity().findViewById(R.id.hseek);
        loop    = (Button) getActivity().findViewById(R.id.hloop);


        // initial establishment of song & info
        setSongInfo();

        // start progress bar
        updateProgress();

        //sBar.setMax(maxTime);

        // seekbar listener for user interaction
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // set song position to coincide with seekbar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                if(pService.isNotNull() && fromUser)
                {
                    getSongMetrics();
                    int croppedLength = (sLength / 100);
                    int newTime = (progress*croppedLength);
                    Log.e(TAG, "Progress: " + progress + " / " + newTime);
                    // set song to current bar position
                    pService.setSongPosition(newTime);

                }

            }


            // what to do when listener starts tracking bar location
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                //updateProgress();
                //sBar.setVerticalScrollbarPosition(mService.getSongPosition());

            }

            // what to do when listener stops tracking bar location
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }




        });


        // assign textview references
        final TextView artist = (TextView) getActivity().findViewById(R.id.artist);
        final TextView title = (TextView) getActivity().findViewById(R.id.title);

        // set textviews based on initial song load
        artist.setText(pService.getArtist());
        title.setText(pService.getTitle());

        // create onClickListeners for each button w/execution of corresponding methods
        loop.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        // check whether or not looping is turned on
                        checkLoop = pService.getLooping();

                        // conditional handler
                        if(checkLoop == true){
                            loop.setTextColor(Color.RED);
                            pService.setLooping(false);
                        }
                        if(checkLoop == false){
                            loop.setTextColor(Color.GREEN);
                            pService.setLooping(true);
                        }

                    }
                }
        );


        play.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("BUTTON CLICK", "--> PLAY");
                        pService.onPlay();
                        setSongInfo();
                    }
                }
        );

        stop.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("BUTTON CLICK", "--> Stop");
                        pService.onStop();

                    }
                }
        );

        forward.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("BUTTON CLICK", "--> FWD");
                        pService.onForward();
                        setSongInfo();
                    }
                }
        );

        back.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("BUTTON CLICK", "--> BCK");
                        pService.onBack();
                        setSongInfo();
                    }
                }
        );


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        Log.i("Main_Activity", "Service Disconnected");

        if(pBound){
            pBound = false;
            pService = null;
        }

    }
}


