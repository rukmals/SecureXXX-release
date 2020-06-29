package com.example.securex.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securex.data.User;
import com.example.securex.data.UserRepository;

public class RegistrationSharedViewModel  extends ViewModel {

    private MutableLiveData<String> Username = new MutableLiveData<>();
    private MutableLiveData<String> Email = new MutableLiveData<>();
    private MutableLiveData<String> Color = new MutableLiveData<>();
    private MutableLiveData<String> Pin = new MutableLiveData<>();
    private MutableLiveData<Integer> Size = new MutableLiveData<>();
    private MutableLiveData<String> Password = new MutableLiveData<>();

    public void setUsername(String username){
        Username.setValue(username);
    }
    public void setEmail(String email){
        Email.setValue(email);
    }
    public void setColor(String color){
        Color.setValue(color);
    }
    public void setPIN(String pin){
        Pin.setValue(pin);
    }
    public void setPassword(String password){
        Password.setValue(password);
    }
    public void setSize(Integer size){
        Size.setValue(size);
    }


    public LiveData<String> getUsername(){
        return Username;
    }
    public LiveData<String> getEmail(){
        return Email;
    }
    public LiveData<String> getColor(){
        return Color;
    }
    public LiveData<String> getPin(){
        return Pin;
    }
    public LiveData<String> getPassword(){
        return Password;
    }
    public LiveData<Integer> getSize(){
        return Size;
    }




    public void saveUser(Context context){
        isForget(context);
        UserRepository userRepository = new UserRepository(context);
        User user = new User(getUsername().getValue().toString(),getEmail().getValue().toString(),getColor().getValue().toString(),getPin().getValue().toString(),Integer.parseInt(getSize().getValue().toString()),getPassword().getValue().toString());
        userRepository.saveUser(user);
    }
    public void isForget(Context context) {
        SharedPreferences pref = context.getSharedPreferences("com.android.app.users",context.MODE_PRIVATE);
        String status = pref.getString("UserStatus","K");
        if(status.equals("ForgotPassword")){
            setUsername(pref.getString("Username",null));
            setEmail(pref.getString("Email",null));
        }

    }

    public void saveDetails(Context context){
        UserRepository userRepository = new UserRepository(context);
        User user =userRepository.getUser();
        User newuser = new User(user.getUsername(),user.getEmail(),getColor().getValue().toString(),getPin().getValue().toString(),Integer.parseInt(getSize().getValue().toString()),getPassword().getValue().toString());
        userRepository.saveUser(newuser);

    }
}
