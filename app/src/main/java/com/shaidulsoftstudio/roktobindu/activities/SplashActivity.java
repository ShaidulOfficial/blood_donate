package com.shaidulsoftstudio.roktobindu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseUser;

import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompat {

    ActivitySplashBinding binding;
    Animation top_animation, bottom_animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        top_animation= AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottom_animation= AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        binding.logo.setAnimation(top_animation);
        binding.title.setAnimation(bottom_animation);
        binding.slogan.setAnimation(bottom_animation);
        binding.ssslogo.setAnimation(top_animation);

        int splashtime=1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,
                        LoginActivity.class));
                finish();
            }
        },splashtime);

    }
}