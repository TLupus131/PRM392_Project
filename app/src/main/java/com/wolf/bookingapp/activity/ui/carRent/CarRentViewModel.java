package com.wolf.bookingapp.activity.ui.carRent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CarRentViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CarRentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is car rent fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}