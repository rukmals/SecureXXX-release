package com.example.securex.passwordupdate;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.securex.R;
import com.example.securex.databinding.FragmentRegistrationPhaseSixBinding;
import com.example.securex.databinding.FragmentUpdatePinPhaseTwoBinding;
import com.example.securex.login.LoginActivity;
import com.example.securex.viewmodel.PasswordUpdateSharedViewModel;
import com.example.securex.viewmodel.RegistrationSharedViewModel;
import com.goodiebag.pinview.Pinview;


public class UpdatePinPhaseTwoFragment extends Fragment {

    private FragmentUpdatePinPhaseTwoBinding binding;
    private NavController navController;
    PasswordUpdateSharedViewModel model;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdatePinPhaseTwoBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;  }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        model = new ViewModelProvider(requireActivity()).get(PasswordUpdateSharedViewModel.class);

        binding.updatepinview2.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                if(!binding.updatepinview2.getValue().equals("")){
                    if(binding.updatepinview2.getValue().equals(model.getPin().getValue().toString())){
                        model.updatePin(getContext());
                        showSuccess();
                        navController.navigate(R.id.action_updatePinPhaseTwoFragment_to_loginActivity);
                        getActivity().finish();

                    }
                    else{
                        showError();
                    }
                }
            }
        });
    }

    public void showSuccess(){
        Toast.makeText(getContext(),"SUCCESS",Toast.LENGTH_SHORT).show();;
    }

    public void showError(){
        Toast.makeText(getContext(),"ERROR",Toast.LENGTH_SHORT).show();;
    }

}
