package com.shaidulsoftstudio.roktobindu.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;
import com.shaidulsoftstudio.roktobindu.normalClass.LanguageManager;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityLanguageBinding;

public class LanguageActivity extends AppCompat {
    ActivityLanguageBinding binding;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LanguageManager lang = new LanguageManager(LanguageActivity.this);

        binding.backlangu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanguageActivity.this,
                        MainActivity.class));
                finish();
            }
        });

binding.radioEnglish.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        lang.updateResourse("en");
        recreate();
    }
});
        binding.radioBangla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang.updateResourse("bn");
                recreate();
            }
        });

//        binding.radiogrplangu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.radioEnglish:
//                        lang.updateResourse("en");
//                        recreate();
//
//                        break;
//                    case R.id.radioBangla:
//                        lang.updateResourse("bn");
//                        recreate();
//                        break;
//                }
//            }
//        });
//    }
//
//    private void setLocale(String language) {
//
//        Resources resources = getResources();
//        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//        Configuration configuration = resources.getConfiguration();
//        configuration.locale = new Locale(language);
//        resources.updateConfiguration(configuration, displayMetrics);
//        onConfigurationChanged(configuration);
//    }
//
//    @Override
//    public void onConfigurationChanged(@NonNull Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        binding.nowtv.setText(R.string.now_language);
//        binding.radioEnglish.setText(R.string.english);
//        binding.radioBangla.setText(R.string.bangla);
//        binding.selectlangtoolbar.setText(R.string.selectlangu);
//
//    }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LanguageActivity.this,
                MainActivity.class));
        finish();
    }
}