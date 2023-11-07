package com.shaidulsoftstudio.roktobindu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {
ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}