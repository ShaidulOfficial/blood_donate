package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityPolicyBinding;
import com.shaidulsoftstudio.roktobindu.normalClass.Constant;

public class PolicyActivity extends AppCompatActivity {
    ActivityPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PolicyActivity.this, MainActivity.class));
            }
        });

        WebSettings webSettings=binding.privacyPolicyWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        binding.privacyPolicyWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                setProgress(newProgress * 100); //Make the bar disappear after URL is loaded

                // Return the notespad name after finish loading
                if(newProgress == 100){

                    binding.progressbarLottiePP.setVisibility(View.GONE);
                }
            }
        });
        binding.privacyPolicyWebView.loadUrl(Constant.privacyPolicy);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PolicyActivity.this, MainActivity.class));
        finish();
    }
}