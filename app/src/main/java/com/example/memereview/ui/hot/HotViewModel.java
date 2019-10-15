package com.example.memereview.ui.hot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HotViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HotViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is hot memes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}