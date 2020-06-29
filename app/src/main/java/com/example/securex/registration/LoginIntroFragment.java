package com.example.securex.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.securex.R;


public class LoginIntroFragment extends Fragment {

    Handler mHandler  = new Handler();
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = (Button) view.findViewById(R.id.fruitpickupbutton);
        navController = Navigation.findNavController(view);
        button.performClick();
        mHandler.postDelayed(new Runnable(){

            public void run(){
                navController.navigate(R.id.action_loginIntroFragment_to_registrationPhaseFiveFragment);

                }

        }, 10000);


    }
}