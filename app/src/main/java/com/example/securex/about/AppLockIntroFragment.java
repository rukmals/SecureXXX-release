package com.example.securex.about;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.securex.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppLockIntroFragment extends Fragment {

    public AppLockIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_lock_intro, container, false);
    }
}
