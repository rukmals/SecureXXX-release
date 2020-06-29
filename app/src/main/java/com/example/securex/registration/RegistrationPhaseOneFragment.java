package com.example.securex.registration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.PatternsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.securex.R;
import com.example.securex.databinding.FragmentRegistrationPhaseOneBinding;
import com.example.securex.viewmodel.RegistrationSharedViewModel;


public class RegistrationPhaseOneFragment extends Fragment implements View.OnClickListener {

    private FragmentRegistrationPhaseOneBinding binding;
    private NavController navController;
    private RegistrationSharedViewModel model;

    private String Username;
    private String Email;

    Button button;
    EditText username_;
    EditText email_;

    private final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";

    public RegistrationPhaseOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationPhaseOneBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
   }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isForget(view);
        super.onViewCreated(view, savedInstanceState);
//        navController = Navigation.findNavController(view);

        model = new ViewModelProvider(requireActivity()).get(RegistrationSharedViewModel.class);



        binding.phase1next.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phase1next:
                Username = binding.username.getText().toString();
                Email = binding.email.getText().toString();
                String Validation = Validation(Username,Email);
                if(Validation.equals("valid")){
                    model.setUsername(Username);
                    model.setEmail(Email);
                    Navigation.findNavController(v).navigate(R.id.action_registrationPhaseOneFragment_to_registrationPhaseTwoFragment);

                }
                else if(Validation.equals("not valid")){
                    showUsernameError();
                    showEmailError();
                }
                else if (Validation.equals("Username not valid")){
                    showUsernameError();
                }
                else if (Validation.equals("Email not valid")){
                    showEmailError();
                }
                else {
                    showEmailError();
                    showUsernameError();
                }

                break;
        }
    }

    public String Validation(String username, String email) {
        String validateState;

        if (isValidEmail(email) && isValidUsername(username)) {
            validateState="valid";
        }

        else if (!isValidUsername(username) && !isValidEmail(email)) {
            validateState="not valid";
        }
        else if(!isValidUsername(username)){
            validateState="Username not valid";
        }
        else if(!isValidEmail(email)){
            validateState="Email not valid";
        }
        else {
            validateState="not valid";
        }

        return validateState;
    }



    public boolean isValidEmail(String email) {
        return (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() && email.length()>0);
    }
    public boolean isValidUsername(String username){
        return (username.matches(USERNAME_PATTERN) && username.length()>=6);
    }

    public void showUsernameError() {

        binding.username.setError("Not Valid");
    }

    public void showEmailError() {
        binding.email.setError("Not Valid");

    }

    public void isForget(View view) {
        SharedPreferences pref = getContext().getSharedPreferences("com.android.app.users",getContext().MODE_PRIVATE);
        String status = pref.getString("UserStatus","K");
        if(status.equals("ForgotPassword")){
                navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_registrationPhaseOneFragment_to_registrationPhaseTwoFragment);
        }
    }

}
