package com.example.securex.passwordupdate;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.securex.R;
import com.example.securex.databinding.FragmentUpdateMenuBinding;


public class UpdateMenuFragment extends Fragment implements View.OnClickListener {

    private FragmentUpdateMenuBinding binding;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateMenuBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.chnageemailbutton.setOnClickListener(this);
        binding.chnagegrappassbutton.setOnClickListener(this);
        binding.changepinbutton.setOnClickListener(this);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.android.app.users",getContext().MODE_PRIVATE);

        binding.username.setText(sharedPreferences.getString("Username","User"));
        binding.email.setText(sharedPreferences.getString("Email","Email"));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chnageemailbutton:
                navController.navigate(R.id.action_updateMenuFragment_to_updateEmailFragment);
                break;
            case R.id.changepinbutton:
                navController.navigate(R.id.action_updateMenuFragment_to_updatePinPhaseOneFragment);
                break;
            case R.id.chnagegrappassbutton:
                navController.navigate(R.id.action_updateMenuFragment_to_updateGraphicalPhaseOneFragment);
                break;
        }
    }
}
