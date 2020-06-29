package com.example.securex.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.securex.R;
import com.example.securex.databinding.FragmentForgetPasswordBinding;
import com.example.securex.registration.MainActivity;
import com.example.securex.utils.ForgetPassword;
import com.example.securex.viewmodel.LoginSharedViewModel;
import com.goodiebag.pinview.Pinview;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends Fragment {

    LoginSharedViewModel model;
    NavController navController;
    FragmentForgetPasswordBinding binding;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgetPasswordBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        model = new ViewModelProvider(requireActivity()).get(LoginSharedViewModel.class);
        pref = getContext().getSharedPreferences("com.android.app.users",getContext().MODE_PRIVATE);
        editor=pref.edit();

        binding.notgetthecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptalert();
            }
        });

        binding.pinviewforget.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                if (binding.pinviewforget.getValue().equals(model.getResetCode())){
                    editor.putString("UserStatus","ForgotPassword");
                    editor.apply();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                else{
                    Toast.makeText(getContext(),"WrongCode",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void promptalert() {
        Log.d("FORGET","CLICKED");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Forgot the Password? send Verification code to the email? ")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String resetCode = ForgetPassword.sendEmail(getContext());
                            model.setResetcode(resetCode);
                            navController.navigate(R.id.action_loginSpinFragment_to_forgetPasswordFragment);
                        }
                        catch (Exception e){
                            Toast.makeText(getContext(),"Something Wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
