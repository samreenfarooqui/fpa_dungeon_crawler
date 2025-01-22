package com.example.cs2340c_team38.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cs2340c_team38.R;
import com.example.cs2340c_team38.databinding.ActivityMainBinding;
import com.example.cs2340c_team38.viewmodels.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private MediaPlayer mediaPlayer;

    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_main
        );
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


        viewModel.getStartEvent().observe(this, message -> startActivity(
                new Intent(MainActivity.this, ConfigActivity.class)
        ));
        viewModel.getEndEvent().observe(this, message -> {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });
        mediaPlayer = MediaPlayer.create(this, R.raw.planning);
        mediaPlayer.start();
    }
    public void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        mediaPlayer.stop();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

}