package com.example.securex.passwordupdate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.securex.R;
import com.example.securex.databinding.FragmentUpdateGraphicalPhaseOneBinding;
import com.example.securex.login.LoginActivity;
import com.example.securex.registration.Colors;
import com.example.securex.registration.MainActivity;
import com.example.securex.viewmodel.PasswordUpdateSharedViewModel;


public class UpdateGraphicalPhaseOneFragment extends Fragment implements View.OnClickListener {
    private FragmentUpdateGraphicalPhaseOneBinding binding;
    private NavController navController;
    private PasswordUpdateSharedViewModel model;

    String[] colorArray;
    int Size;
    String Color;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateGraphicalPhaseOneBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
          }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        model = new ViewModelProvider(requireActivity()).get(PasswordUpdateSharedViewModel.class);
        binding.updategraphicalphaseonenext.setOnClickListener(this);
        setSize();
        binding.updatesiezspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    setColors(4);
                }
                else if(position==1){
                    setColors(6);
                }
                else if (position==2){
                    setColors(8);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updategraphicalphaseonenext:
                Size=Integer.parseInt(binding.updatesiezspinner.getSelectedItem().toString());
                Color=binding.updatecolorspinner.getSelectedItem().toString();
                model.setColor(Color);
                model.setSize(Size);
                navController.navigate(R.id.action_updateGraphicalPhaseOneFragment_to_updateGraphicalPhaseTwoFragment);
                break;
        }

    }
    public void setSize() {
        String[] size = new String[]{"4","6","8"};
        ArrayAdapter sizeAd = new ArrayAdapter(getActivity().getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,size);
        sizeAd.setDropDownViewResource(R.layout.spinnertext);
        binding.updatesiezspinner.setAdapter(sizeAd);
    }

    public void setColors(int Size) {
        Colors colors = new Colors();
        if(Size==4){

            colorArray=colors.getColorsFour();
        }
        else if(Size==6){
            colorArray=colors.getColorsSix();
        }
        else if(Size==8){
            colorArray=colors.getColorsEight();
        }
        ArrayAdapter colorAd = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,colorArray);
        colorAd.setDropDownViewResource(R.layout.spinnertext);
        binding.updatecolorspinner.setAdapter(colorAd);
    }


    }
