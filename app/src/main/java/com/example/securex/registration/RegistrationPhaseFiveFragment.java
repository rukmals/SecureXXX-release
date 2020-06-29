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

import com.example.securex.R;
import com.example.securex.databinding.FragmentRegistrationPhaseFiveBinding;
import com.example.securex.viewmodel.RegistrationSharedViewModel;
import com.goodiebag.pinview.Pinview;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationPhaseFiveFragment extends Fragment {

    private FragmentRegistrationPhaseFiveBinding binding;
    private NavController navController;
    RegistrationSharedViewModel model;

    public RegistrationPhaseFiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationPhaseFiveBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
  }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        model = new ViewModelProvider(requireActivity()).get(RegistrationSharedViewModel.class);


        binding.pinview1.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                if(!binding.pinview1.getValue().equals("")){
                    model.setPIN(binding.pinview1.getValue());
                    navController.navigate(R.id.action_registrationPhaseFiveFragment_to_registrationPhaseSixFragment);


                }
            }
        });

    }

}
