package app.cap.foodreet.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.cap.foodreet.R;

/**
 * Created by clear on 2017-10-30.
 * 위치기반서비스용
 */

public class MapFragment extends Fragment{

    View view;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.map_fragment, container, false);
        return view;
    }
}
