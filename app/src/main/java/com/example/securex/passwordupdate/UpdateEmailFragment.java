package com.example.securex.passwordupdate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.securex.R;
import com.example.securex.data.UserRepository;
import com.example.securex.databinding.FragmentUpdateEmailBinding;
import com.example.securex.databinding.FragmentUpdateMenuBinding;


public class UpdateEmailFragment extends Fragment {

    private FragmentUpdateEmailBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateEmailBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        binding.updateemailconfirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email =binding.updateemailinput.getText().toString();
                if(isValidEmail(Email)){
                    UserRepository userRepository = new UserRepository(getContext());
                    userRepository.changeEmail(Email);
                    navController.navigate(R.id.action_updateEmailFragment_to_updateMenuFragment);
                }
                else {
                    binding.updateemailinput.setError("NOT VALID");
                }
            }
        });
    }

    public boolean isValidEmail(String email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.length()>0);
    }
}
