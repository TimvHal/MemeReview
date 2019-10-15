package com.example.memereview.ui.owned;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OwnedViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OwnedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is own memes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}