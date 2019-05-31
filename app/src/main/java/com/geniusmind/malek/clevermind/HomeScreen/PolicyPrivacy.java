package com.geniusmind.malek.clevermind.HomeScreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geniusmind.malek.clevermind.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PolicyPrivacy extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_policy_privacy, container, false);
    }

    // create new instance for PolicyPrivacy fragment . . . ;
    public static Fragment newInstance(){
        return new PolicyPrivacy();
    }

}
