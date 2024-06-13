package com.example.bookingapp.activity.ui.stay;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StayViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public StayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is stay fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}