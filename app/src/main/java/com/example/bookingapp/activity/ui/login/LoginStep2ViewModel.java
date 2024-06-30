package com.example.bookingapp.activity.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginStep2ViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public LoginStep2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is login step 2 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
