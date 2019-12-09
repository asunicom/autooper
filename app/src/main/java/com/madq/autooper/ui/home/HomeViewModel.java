package com.madq.autooper.ui.home;

import android.os.Build;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("打开辅助开关");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mText.setValue("手机不支持本软件");
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}