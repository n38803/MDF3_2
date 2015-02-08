package fullsail.com.mdf3w1;

import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by shaunthompson on 2/8/15.
 */
public class FragmentMain extends Fragment implements ServiceConnection {


    ImageButton play;
    ImageButton stop;
    ImageButton forward;
    ImageButton back;


    public static FragmentMain newInstance() {
        FragmentMain frag = new FragmentMain();
        return frag;
    }



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




        // grab service intent & initiate service
        Intent intent = new Intent(FragmentMain.this.getActivity(), PlayerService.class);
        getActivity().startService(intent);







    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        // Assign button references
        play    = (ImageButton) getActivity().findViewById(R.id.play);
        stop    = (ImageButton) getActivity().findViewById(R.id.stop);
        forward = (ImageButton) getActivity().findViewById(R.id.forward);
        back    = (ImageButton) getActivity().findViewById(R.id.back);

        // TODO - assign textviews

        // TODO - set onClick listeners for buttons

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}


