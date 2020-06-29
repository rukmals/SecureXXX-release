package com.example.securex.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.securex.data.User;
import com.example.securex.data.UserRepository;

public class LoginSharedViewModel extends ViewModel {
    MutableLiveData<User> userLiveData = new MutableLiveData<>() ;
    MutableLiveData<String> resetcode = new MutableLiveData<>();





    public void setResetcode(String code){
        resetcode.setValue(code);
    }
    public String getResetCode(){
        return resetcode.getValue().toString();
    }




    public User getUser(Context context){
        UserRepository userRepository = new UserRepository(context);
        return userRepository.getUser();
    }
}
