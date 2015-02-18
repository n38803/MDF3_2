package fullsail.com.mdf3_w3.fragments;

/**
 * Created by shaunthompson on 2/17/15.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fullsail.com.mdf3_w3.R;


public class AddFragment extends Fragment {



    private final String TAG = "ADD FRAGMENT";




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







    }



}

