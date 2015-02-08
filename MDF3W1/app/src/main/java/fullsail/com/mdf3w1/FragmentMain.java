package fullsail.com.mdf3w1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by shaunthompson on 2/8/15.
 */
public class FragmentMain extends Fragment {


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


        // Assign button references
        play    = (ImageButton) getActivity().findViewById(R.id.play);
        stop    = (ImageButton) getActivity().findViewById(R.id.stop);
        forward = (ImageButton) getActivity().findViewById(R.id.forward);
        back    = (ImageButton) getActivity().findViewById(R.id.back);




    }


}


