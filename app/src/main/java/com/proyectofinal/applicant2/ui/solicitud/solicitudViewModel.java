package com.proyectofinal.applicant2.ui.solicitud;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class solicitudViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public solicitudViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}