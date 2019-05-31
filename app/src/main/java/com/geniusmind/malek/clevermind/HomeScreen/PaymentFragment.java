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
public class PaymentFragment extends Fragment {

    public static Fragment newInstance() {
        return new PaymentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_payment, container, false);






        return view;
    }

}
