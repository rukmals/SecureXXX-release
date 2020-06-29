package com.example.securex.passwordupdate;

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
import com.example.securex.databinding.FragmentUpdatePinPhaseOneBinding;
import com.example.securex.viewmodel.PasswordUpdateSharedViewModel;
import com.example.securex.viewmodel.RegistrationSharedViewModel;
import com.goodiebag.pinview.Pinview;


public class UpdatePinPhaseOneFragment extends Fragment {

    private FragmentUpdatePinPhaseOneBinding binding;
    private NavController navController;
    PasswordUpdateSharedViewModel model;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdatePinPhaseOneBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        model = new ViewModelProvider(requireActivity()).get(PasswordUpdateSharedViewModel.class);

        binding.updatepinview1.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                if(!binding.updatepinview1.getValue().equals("")){
                    model.setPIN(binding.updatepinview1.getValue());
                    navController.navigate(R.id.action_updatePinPhaseOneFragment_to_updatePinPhaseTwoFragment);


                }
            }
        });
    }
}
