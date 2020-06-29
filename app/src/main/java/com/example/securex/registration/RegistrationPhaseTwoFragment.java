package com.example.securex.registration;

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
import com.example.securex.databinding.FragmentRegistrationPhaseTwoBinding;
import com.example.securex.viewmodel.RegistrationSharedViewModel;


public class RegistrationPhaseTwoFragment extends Fragment implements View.OnClickListener{

    private FragmentRegistrationPhaseTwoBinding binding;

    private NavController navController;
    RegistrationSharedViewModel model;
    String[] colorArray;
    int Size;
    String Color;

    public RegistrationPhaseTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationPhaseTwoBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        navController = Navigation.findNavController(view);
        model = new ViewModelProvider(requireActivity()).get(RegistrationSharedViewModel.class);

        binding.phase2next.setOnClickListener(this);
        setSize();
        binding.siezspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            case R.id.phase2next:
                Size=Integer.parseInt(binding.siezspinner.getSelectedItem().toString());
                Color=binding.colorspinner.getSelectedItem().toString();
                model.setColor(Color);
                model.setSize(Size);
//                navController.navigate(R.id.action_registrationPhaseTwoFragment_to_registrationPhaseThreeFragment);
                Navigation.findNavController(v).navigate(R.id.action_registrationPhaseTwoFragment_to_registrationPhaseThreeFragment);
                break;
        }

    }
    public void setSize() {
        String[] size = new String[]{"4","6","8"};
        ArrayAdapter sizeAd = new ArrayAdapter(getActivity().getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,size);
        sizeAd.setDropDownViewResource(R.layout.spinnertext);
        binding.siezspinner.setAdapter(sizeAd);
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
        binding.colorspinner.setAdapter(colorAd);
    }


}
