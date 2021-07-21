package com.ashwin.mediatorlivedata;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();

    private Observer<Integer> foreverObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer i) {
            Log.d("livedata-demo", "MainViewModel: foreverObserver.onChanged: " + i);
        }
    };

    public MainViewModel() {
        LiveData<Integer> liveDataOne = SingletonRepository.getInstance().getSingletonCountOneLiveData();
        LiveData<Integer> liveDataTwo = SingletonRepository.getInstance().getSingletonCountTwoLiveData();

        mediatorLiveData.addSource(liveDataOne, i -> {
            Log.d("livedata-demo", "MainViewModel: SingletonCountOneLiveData.onChanged: " + i);
            //mediatorLiveData.postValue(combineData(liveDataOne, liveDataTwo));
            mediatorLiveData.postValue(i.toString());
        });

        mediatorLiveData.addSource(liveDataTwo, i -> {
            Log.d("livedata-demo", "MainViewModel: SingletonCountTwoLiveData.onChanged: " + i);
            //mediatorLiveData.postValue(combineData(liveDataOne, liveDataTwo));
            mediatorLiveData.postValue(i.toString());
        });

        // This will be listening for changes even if the view is in onStop state.
        SingletonRepository.getInstance().getSingletonCountOneLiveData().observeForever(foreverObserver);
    }

    /*
     * If you need to combine multiple LiveData values before posting.
     */
    private String combineData(LiveData<Integer> oneLiveData, LiveData<Integer> twoLiveData) {
        Integer one = oneLiveData.getValue();
        Integer two = twoLiveData.getValue();

        // Don't send a success until we have both results
        if (one == null || two == null) {
            // Return last or loading value
            return mediatorLiveData.getValue();
        }
        return one.toString() + " " + two.toString();
    }

    // For many-to-one transformation
    public LiveData<String> getMediatorLiveData() {
        return mediatorLiveData;
    }

    // For one-to-one static transformation
    public LiveData<String> getSingletonOneLiveData() {
        // Uses MediatorLiveData internally
        return Transformations.map(SingletonRepository.getInstance().getSingletonCountOneLiveData(), i -> {
            return i.toString();
        });
    }

    // For one-to-one dynamic transformation
    public LiveData<String> getNewOneLiveData() {
        // Uses MediatorLiveData internally
        return Transformations.switchMap(SingletonRepository.getInstance().getSingletonCountOneLiveData(), i -> {
            return SingletonRepository.getInstance().getNewOneLiveData(i);
        });
    }

    public void incrementOne() {
        SingletonRepository.getInstance().incrementOne();
    }

    public void incrementTwo() {
        SingletonRepository.getInstance().incrementTwo();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("livedata-demo", "MainViewModel: onCleared");
        SingletonRepository.getInstance().getSingletonCountOneLiveData().removeObserver(foreverObserver);
    }
}
