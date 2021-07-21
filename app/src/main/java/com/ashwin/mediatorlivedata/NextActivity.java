package com.ashwin.mediatorlivedata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class NextActivity extends AppCompatActivity {
    private SingletonRepository singletonRepository = SingletonRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Button updateOneButton = findViewById(R.id.update_one_button);
        updateOneButton.setOnClickListener(view -> {
            singletonRepository.incrementOne();
        });

        Button updateTwoButton = findViewById(R.id.update_two_button);
        updateTwoButton.setOnClickListener(view -> {
            singletonRepository.incrementTwo();
        });
    }
}
