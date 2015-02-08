package fullsail.com.mdf3w1;

import android.app.Fragment;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by shaunthompson on 2/8/15.
 */
public class FragmentMain extends Fragment implements ServiceConnection {

    final String TAG = "Main_Activity";


    ImageButton play;
    ImageButton stop;
    ImageButton forward;
    ImageButton back;

    PlayerService pService;
    boolean pBound;








    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {

        // Create and return view for this fragment.
        View view = _inflater.inflate(R.layout.fragment_main, _container, false);
        return view;
    }



    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        Log.i(TAG, "Activity Created");

        // initiate intent & assign to service/binder
        Intent intent = new Intent(FragmentMain.this.getActivity(), PlayerService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);

    }



    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "Service Connected");

        // create binder
        PlayerService.PlayerServiceBinder binder = (PlayerService.PlayerServiceBinder)service;
        pService = binder.getService();
        pBound = true;


        // assign button references
        play    = (ImageButton) getActivity().findViewById(R.id.play);
        stop    = (ImageButton) getActivity().findViewById(R.id.stop);
        forward = (ImageButton) getActivity().findViewById(R.id.forward);
        back    = (ImageButton) getActivity().findViewById(R.id.back);

        // assign textview references
        final TextView artist = (TextView) getActivity().findViewById(R.id.artist);
        final TextView title = (TextView) getActivity().findViewById(R.id.title);

        play.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("BUTTON CLICK", "--> PLAY");
                        pService.onPlay();
                        artist.setText(pService.getArtist());
                        title.setText(pService.getTitle());
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
                        artist.setText(pService.getArtist());
                        title.setText(pService.getTitle());
                    }
                }
        );

        back.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("BUTTON CLICK", "--> BCK");
                        pService.onBack();
                        artist.setText(pService.getArtist());
                        title.setText(pService.getTitle());
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


