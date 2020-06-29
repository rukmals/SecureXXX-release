package com.example.securex.login;

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

import com.example.securex.databinding.FragmentLoginPinBinding;
import com.example.securex.filesecurity.EncrptorHome;
import com.example.securex.viewmodel.LoginSharedViewModel;
import com.goodiebag.pinview.Pinview;


public class LoginPinFragment extends Fragment {

    private FragmentLoginPinBinding binding;
    private NavController navController;
    private LoginSharedViewModel model;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginPinBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        model = new ViewModelProvider(requireActivity()).get(LoginSharedViewModel.class);

        binding.PinUnlock.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                if(binding.PinUnlock.getValue().equals(model.getUser(getContext()).getPin())){
                    showSuccess();
                    startActivity(new Intent(getActivity(), EncrptorHome.class));
                    getActivity().finish();
                }
                else {
                    showError();
                    binding.PinUnlock.clearValue();
                }
            }
        });
    }

    private void showError() {

        Toast.makeText(getContext(),"NOT MATCH",Toast.LENGTH_SHORT).show();
    }

    private void showSuccess() {

//        Toast.makeText(getContext(),"SUCCESS",Toast.LENGTH_SHORT).show();
    }
}
