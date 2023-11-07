package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.adapter.FavouriteAdapter;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityFavouriteBinding;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompat {
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private List<Users> modelList;
    private FavouriteAdapter favouriteAdapter;
    ActivityFavouriteBinding binding;
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

// ======================== internet connection start============================================


        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo wificonn=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if ( (wificonn==null && wificonn.isConnected())||  networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.no_internet);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            dialog.show();

            AppCompatButton tryagain = dialog.findViewById(R.id.try_btn);
            AppCompatButton connect = dialog.findViewById(R.id.connect_btn);

            tryagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });
            connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });

        } else {

            Toast.makeText(this, "Connection success", Toast.LENGTH_SHORT).show();

        }
// ======================== internet connection end============================================
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        loadfabouriteusers();

        binding.backBtnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this,
                        MainActivity.class));
                finish();
            }
        });

        favouriteAdapter = new FavouriteAdapter(FavouriteActivity.this,
                modelList);
        favouriteAdapter.notifyDataSetChanged();
        binding.favouriteRcv.setAdapter(favouriteAdapter);
        binding.favouriteRcv.setHasFixedSize(true);

    }

    private void loadfabouriteusers() {
        modelList = new ArrayList<>();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users");
        dbref.child(firebaseAuth.getUid()).child("Favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String Uid = (String) ds.child("uid").getValue();
                           Users model = new Users();
                            model.setUid(Uid);
                            modelList.add(model);

                        }

                        binding.favoCount.setText("" + modelList.size());
                        favouriteAdapter = new FavouriteAdapter(FavouriteActivity.this
                         , modelList);
                        favouriteAdapter.notifyDataSetChanged();
                        binding.favouriteRcv.setHasFixedSize(true);
                        binding.favouriteRcv.setAdapter(favouriteAdapter);
                        binding.progressbar.setVisibility(View.GONE);
                        if (modelList.isEmpty()) {

                            binding.progressbar.setVisibility(View.GONE);
                            showAlert("Sorry You didn\'t add to favourite list");
                        } else {
                            Toast.makeText(FavouriteActivity.this, " hey Donor", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void showAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteActivity.this);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(FavouriteActivity.this, MainActivity.class));
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        startActivity(new Intent(FavouriteActivity.this,
                MainActivity.class));
        finish();
    }
}