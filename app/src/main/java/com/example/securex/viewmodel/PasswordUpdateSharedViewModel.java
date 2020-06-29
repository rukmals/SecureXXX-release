package com.example.securex.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securex.data.User;
import com.example.securex.data.UserRepository;

public class PasswordUpdateSharedViewModel extends ViewModel {


    private MutableLiveData<String> Color = new MutableLiveData<>();
    private MutableLiveData<Integer> Size = new MutableLiveData<>();
    private MutableLiveData<String> Password = new MutableLiveData<>();
    private MutableLiveData<String> Pin = new MutableLiveData<>();


    public void setColor(String color){
        Color.setValue(color);
    }
    public void setPassword(String password){
        Password.setValue(password);
    }
    public void setSize(Integer size){
        Size.setValue(size);
    }
    public void setPIN(String pin){
        Pin.setValue(pin);
    }



    public LiveData<String> getColor(){
        return Color;
    }
    public LiveData<String> getPassword(){
        return Password;
    }
    public LiveData<Integer> getSize(){
        return Size;
    }
    public LiveData<String> getPin(){
        return Pin;
    }


    public void updateGraphical(Context context){
        UserRepository userRepository =  new UserRepository(context);
        userRepository.changeGraphical(getColor().getValue().toString(),Integer.parseInt(getSize().getValue().toString()),getPassword().getValue().toString());
    }

    public void updatePin(Context context){
        UserRepository userRepository = new UserRepository(context);
        userRepository.updatePin(getPin().getValue().toString());
    }



}
