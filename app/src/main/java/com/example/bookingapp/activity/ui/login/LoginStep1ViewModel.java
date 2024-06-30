package com.example.bookingapp.activity.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginStep1ViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LoginStep1ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is login step 1 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}