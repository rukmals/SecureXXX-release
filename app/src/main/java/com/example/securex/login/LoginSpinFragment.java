package com.example.securex.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.example.securex.R;
import com.example.securex.data.Spin;
import com.example.securex.data.SpinEight;
import com.example.securex.data.SpinFour;
import com.example.securex.data.SpinSix;
import com.example.securex.data.User;
import com.example.securex.databinding.FragmentLoginSpinBinding;
import com.example.securex.filesecurity.EncrptorHome;
import com.example.securex.utils.ForgetPassword;
import com.example.securex.viewmodel.LoginSharedViewModel;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class LoginSpinFragment extends Fragment implements View.OnClickListener {

    private FragmentLoginSpinBinding binding;
    private NavController navController;
    private LoginSharedViewModel model;

    Spin spin;
    Context context;
    User user;

    float oldDegree;
    float newDegree;
    int Degree;
    int Size;
    private String UserColor;
    ArrayList<String> ColorsArray;
    ArrayList<String> FruitsArray;
    private int Color_Index;
    int PhaseChange;
    private String UserPassword;
    private String MatchingPassword;
    int SelectedFruits;
    int Attempts;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginSpinBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        model = new ViewModelProvider(requireActivity()).get(LoginSharedViewModel.class);
        setSpins();
        binding.spinleftbutton.setOnClickListener(this);
        binding.spinrightbutton.setOnClickListener(this);
        binding.fruitpickupbutton.setOnClickListener(this);
        binding.gotopinbutton.setOnClickListener(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            fingerprint();
            binding.biobutton.setOnClickListener(this);
        }
        else{
            binding.biobutton.setVisibility(View.GONE);
        }

        binding.forgetbutton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.spinleftbutton:
                spinleft();
                break;
            case R.id.spinrightbutton:
                spinright();
                break;
            case R.id.fruitpickupbutton:
                confirmButtonClicked();
                break;
            case R.id.gotopinbutton:
                navController.navigate(R.id.action_loginSpinFragment_to_loginFragment);
                break;
            case R.id.biobutton:
                biometricPrompt.authenticate(promptInfo);
                break;
            case  R.id.forgetbutton:

                promptalert();
                break;
        }
    }

    private void promptalert() {
        Log.d("FORGET","CLICKED");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Forgot the Password? send Verification code to the emal? ")
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

    private void spinright() {
        float d = newDegree+Degree;
        spin(oldDegree,d);
        oldDegree=d;
        newDegree+=Degree;
        if(Color_Index>=PhaseChange){
            Color_Index=0;
        }
        else{
            Color_Index++;
        }
        Log.d("s",Integer.toString(Color_Index));
    }

    private void spinleft() {
        float d = newDegree-Degree;
        spin(oldDegree,d);
        oldDegree=d;
        newDegree-=Degree;
        if(Color_Index<=-PhaseChange){
            Color_Index=0;
        }
        else{
            Color_Index--;
        }
        Log.d("s",Integer.toString(Color_Index));
    }

    public void setSpins() {
        user = model.getUser(getContext());
        int Size = user.getSize();
        if(Size==4){
            spin = new SpinFour();
        }
        else if(Size==6){
            spin = new SpinSix();
        }
        else if (Size==8){
            spin = new SpinEight();
        }

        binding.colorspin.setImageDrawable(getResources().getDrawable(spin.getColorRing()));
        binding.fruitspin.setImageDrawable(getResources().getDrawable(spin.getFruitRing()));

        setVariables(spin,user);
    }

    public void setVariables(Spin spin, User user) {
        oldDegree = 0;
        newDegree = 0;
        Size = user.getSize();
        UserColor = user.getColor();
        UserPassword=user.getPassword();
        MatchingPassword="";
        SelectedFruits=0;
        Attempts=0;

        Degree = spin.getDegree();
        ColorsArray = spin.getColors();
        FruitsArray = spin.getFruits();
        PhaseChange = spin.getPhaseChange();

        Color_Index = ColorsArray.indexOf(UserColor);
    }

    public void spin(float oldDegree,float newDegree){
        RotateAnimation rotateAnimation = new RotateAnimation(oldDegree,newDegree,RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(100);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // we empty the result text view when the animation start

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // we display the correct sector pointed by the triangle at the end of the rotate animation

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.colorspin.startAnimation(rotateAnimation);
    }

    public void confirmButtonClicked() {
        SelectedFruits++;

        if (Color_Index < 0) {
            Color_Index = Size + Color_Index;
        }


        MatchingPassword += FruitsArray.get(Color_Index);
        Log.d("USER_PASSWORD",UserPassword);
        Log.d("PASSWORD",MatchingPassword);


        if (MatchingPassword.equals(UserPassword)) {
            SelectedFruits = 0;
            MatchingPassword="";
            showSuccess();
            startActivity(new Intent(getActivity(), EncrptorHome.class));
            getActivity().finish();

        }
        else if (!MatchingPassword.equals(UserPassword) && SelectedFruits == FruitsArray.size()) {
            showError();
            SelectedFruits = 0;
            MatchingPassword = "";
            Attempts++;
        }
        if (Attempts > 3) {
            binding.fruitpickupbutton.setEnabled(false);
            navController.navigate(R.id.action_loginSpinFragment_to_loginFragment);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Attempts = 0;
                    SelectedFruits=0;
                    MatchingPassword="";
                    binding.fruitpickupbutton.setEnabled(true);
                }
            }, 5000);

        }


            setCount(SelectedFruits);

    }

    private void setCount(int selectedFruits) {
        binding.textView.setText(Integer.toString(selectedFruits));
    }

    private void showError() {

        Toast.makeText(getContext(),"NOT MATCH",Toast.LENGTH_SHORT).show();
    }

    private void showSuccess() {

//        Toast.makeText(getContext(),"SUCCESS",Toast.LENGTH_SHORT).show();
    }

    void fingerprint(){
        executor = ContextCompat.getMainExecutor(getContext());
        biometricPrompt = new BiometricPrompt(getActivity(),
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                showSuccess();
                startActivity(new Intent(getActivity(), EncrptorHome.class));
                getActivity().finish();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

    }

}
