package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.shaidulsoftstudio.roktobindu.BuildConfig;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityAboutUsBinding;
import com.shaidulsoftstudio.roktobindu.normalClass.Constant;

public class AboutUsActivity extends AppCompatActivity {
    ActivityAboutUsBinding binding;
    ReviewInfo reviewInfo;
    ReviewManager reviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
               super.onCreate(savedInstanceState);
        binding = ActivityAboutUsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        activateReviewInfo();

        binding.backaboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUsActivity.this, MainActivity.class));
                finish();
            }
        });
        binding.termsconditioninlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUsActivity.this, AboutActivity.class));
                finish();
            }
        });

        binding.websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri weburi = Uri.parse(Constant.website);
                startActivity(new Intent(Intent.ACTION_VIEW, weburi));
            }
        });

        binding.moreAppsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://search?q=pub:Shaidul Soft. Studio");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } catch (ActivityNotFoundException e) {
                    Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=Shaidul Soft. Studio");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri1));
                }
            }
        });

        binding.shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appName = getPackageName();
                try {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share Rokto Bindo");
                    String shareMessage = "http://play.google.com/store/apps/details?id=" + appName + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(intent, "Share by"));
                } catch (Exception e) {
                    Toast.makeText(AboutUsActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.rateuslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // final String appName = getPackageName();
                // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                activateReviewInfo();
            }
        });

        binding.company.setText(Constant.company_name);
        binding.email.setText(Constant.emailid);
        binding.website.setText(Constant.website);
        binding.contact.setText(Constant.contactno);
        binding.version.setText(BuildConfig.VERSION_CODE);

    }

    private void activateReviewInfo() {
        reviewManager = ReviewManagerFactory.create(AboutUsActivity.this);
        Task<ReviewInfo> managerInfotask = reviewManager.requestReviewFlow();
        managerInfotask.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                    Task<Void> flow = reviewManager.launchReviewFlow(AboutUsActivity.this, reviewInfo);
                    flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                } else {
                    Toast.makeText(AboutUsActivity.this, "review failed to start", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(0, 0);
                overridePendingTransition(0, 0);
                startActivity(new Intent(AboutUsActivity.this, MainActivity.class));
                finish();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AboutUsActivity.this, MainActivity.class));
        finish();
    }
}