package com.example.cs2340c_team38.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.cs2340c_team38.R;
import com.example.cs2340c_team38.databinding.ActivityMainBinding;
import com.example.cs2340c_team38.viewmodels.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private MediaPlayer mediaPlayer;

    private Button buttonLogout;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_main
        );
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


//        viewModel.getStartEvent().observe(this, message -> startActivity(
//                new Intent(MainActivity.this, ConfigActivity.class)
//        ));

        viewModel.getStartEvent().observe(this, message -> {
            logEvent("navigation", "destination", "ConfigActivity");
            startActivity(new Intent(MainActivity.this, ConfigActivity.class));
        });

        viewModel.getEndEvent().observe(this, message -> {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });

//        // Initialize Firebase Analytics
//        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        enableDebugLogging();

        // Track app launch event
        logEvent("app_launch", "screen_name", "MainActivity");

        mediaPlayer = MediaPlayer.create(this, R.raw.planning);
        logEvent("media_player", "action", "start");
        mediaPlayer.start();

        forceAnalyticsUpload();
    }
    public void logoutUser(){
        logEvent("user_action", "action", "logout");
        FirebaseAuth.getInstance().signOut();
        mediaPlayer.stop();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void logEvent(String eventName, String paramKey, String paramValue) {
        Bundle bundle = new Bundle();
        bundle.putString(paramKey, paramValue);
        firebaseAnalytics.logEvent(eventName, bundle);
    }

    private void enableDebugLogging() {
        try {
            Runtime.getRuntime().exec("setprop debug.firebase.analytics.app com.example.cs2340c_team38");
            Log.d("FirebaseAnalytics", "Debug logging enabled.");
        } catch (Exception e) {
            Log.e("FirebaseAnalytics", "Error enabling debug logging", e);
        }
    }

    private void forceAnalyticsUpload() {
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        Log.d("FirebaseAnalytics", "Forced analytics upload enabled.");
    }


}