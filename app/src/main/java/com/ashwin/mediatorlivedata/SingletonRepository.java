package com.ashwin.mediatorlivedata;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

public class SingletonRepository {
    private static final SingletonRepository INSTANCE = new SingletonRepository();

    private MutableLiveData<Integer> singletonCountOneLiveData;
    private MutableLiveData<Integer> singletonCountTwoLiveData;

    private SingletonRepository() {
        singletonCountOneLiveData = new MutableLiveData<>(0);
        singletonCountTwoLiveData = new MutableLiveData<>(1000);
    }

    public static SingletonRepository getInstance() {
        return INSTANCE;
    }

    public LiveData<Integer> getSingletonCountOneLiveData() {
        return singletonCountOneLiveData;
    }

    public LiveData<Integer> getSingletonCountTwoLiveData() {
        return singletonCountTwoLiveData;
    }

    public LiveData<String> getNewOneLiveData(int initial) {
        MediatorLiveData<String> liveData = new MediatorLiveData<>();
        liveData.setValue(String.valueOf(initial));

        liveData.addSource(singletonCountOneLiveData, i -> {
            liveData.postValue(String.valueOf(i));
        });

        return liveData;
    }

    public void incrementOne() {
        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                int newValue = singletonCountOneLiveData.getValue() + 1;
                Log.d("livedata-demo", "SingletonRepository: updating singletonCountOneLiveData: " + newValue);
                singletonCountOneLiveData.postValue(newValue);
            }
        }, 5000L);
    }

    public void incrementTwo() {
        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                int newValue = singletonCountTwoLiveData.getValue() + 1;
                Log.d("livedata-demo", "SingletonRepository: updating singletonCountTwoLiveData: " + newValue);
                singletonCountTwoLiveData.postValue(newValue);
            }
        }, 5000L);
    }
}
