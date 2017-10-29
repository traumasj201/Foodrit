package app.cap.foodreet.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.cap.foodreet.R;

/**
 * Created by clear on 2017-10-30.
 * 즐겨찾기용
 */

public class StoreFragment extends Fragment {

    View view;

    public StoreFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.store_fragment, container, false);
        return view;
    }
}
