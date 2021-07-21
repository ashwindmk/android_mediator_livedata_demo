package com.ashwin.mediatorlivedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView dataTextView = findViewById(R.id.data_text_view);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        /*
         * Even if MediatorLiveData has multiple values from multiple LiveData,
         * here you will only get the last value posted before onStart by the MediatorLiveData.
         * E.g. You will only get the default value of the last LiveData.
         */

        /*
         * Any value posted after onStart and before onStop will be observed here as usual.
         */
        mainViewModel.getMediatorLiveData().observe(this, str -> {
            Log.d("livedata-demo", "MainActivity: mediatorLiveData.onChanged: " + str);
            dataTextView.setText(str);
        });

        mainViewModel.getSingletonOneLiveData().observe(this, s -> {
            Log.d("livedata-demo", "MainActivity: SingletonOneLiveData.onChanged: " + s);
        });

        Button updateOneButton = findViewById(R.id.update_one_button);
        updateOneButton.setOnClickListener(view -> {
            mainViewModel.incrementOne();
        });

        Button updateTwoButton = findViewById(R.id.update_two_button);
        updateTwoButton.setOnClickListener(view -> {
            mainViewModel.incrementTwo();
        });

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, NextActivity.class));
        });
    }
}
