package com.aggregator.loop;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */


public class TutorialFragmentFive extends Fragment {

LinearLayout llTut;
    public TutorialFragmentFive() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv=inflater.inflate(R.layout.fragment_tutorial_fragment_five, container, false);

        llTut= (LinearLayout) vv.findViewById(R.id.ll_tut_five);

        llTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),RouteNew.class));
            }
        });

        return vv;
    }


}
