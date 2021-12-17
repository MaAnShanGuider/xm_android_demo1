package com.zhangxueyou.android2.ui.homeMain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeMainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public HomeMainViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("12");
    }

    public LiveData<String> getText() {
        return mText;
    }
}