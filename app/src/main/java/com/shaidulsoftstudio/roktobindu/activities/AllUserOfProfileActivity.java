package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;
import com.shaidulsoftstudio.roktobindu.normalClass.MyApplication;
import com.shaidulsoftstudio.roktobindu.model.Users;


import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityAllUserOfProfileBinding;


public class AllUserOfProfileActivity extends AppCompat {

    ActivityAllUserOfProfileBinding binding;
    DatabaseReference dbRef;
    Users model;
    Intent intent;
    String userId = "", Usertype = "", ActiveStatus = "", ActiveDate = "";
    boolean isMyfavorite = false;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllUserOfProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        userId = intent.getStringExtra("userId");

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            checkfavourite();
        } else {
            Toast.makeText(this, null, Toast.LENGTH_SHORT).show();
        }

        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(AllUserOfProfileActivity.this, "You are not log in", Toast.LENGTH_SHORT).show();
                } else {
                    if (isMyfavorite) {
                        MyApplication.removeFromFavorite(AllUserOfProfileActivity.this, userId);
                        DatabaseReference dbrefFavoRemove = FirebaseDatabase.getInstance().getReference("Users");
                        dbrefFavoRemove.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("OkFavourite").setValue("");
                    } else {
                        MyApplication.addToFavorite(AllUserOfProfileActivity.this, userId);
                        DatabaseReference dbrefFavoAdd = FirebaseDatabase.getInstance().getReference("Users");
                        dbrefFavoAdd.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("OkFavourite").setValue("Good");

                    }
                }

            }
        });
        binding.backprofileuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllUserOfProfileActivity.this,
                        MainActivity.class));
                finish();
            }
        });

        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Usertype = snapshot.child("Usertype").getValue().toString().trim();
                    if (Usertype.equals("Donor")) {
                        binding.userprofile.setText(R.string.donorprof);
                    } else {
                        binding.userprofile.setText(R.string.receverprof);
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                model = snapshot.getValue(Users.class);
                binding.typTv.setText(model.getUsertype());
                binding.groupTv.setText(model.getBloodGroup());
                binding.textView.setText(model.getFullname());
                binding.textView2.setText(model.getEmail());
                binding.mobileTv.setText(model.getMobileno());
                binding.genderTv.setText(model.getGender());
                binding.distTv.setText(model.getDistrict());
                binding.divisionTv.setText(model.getDivision());
                binding.upzilaTv.setText(model.getUpzila());
                binding.unionTv.setText(model.getUnion());
                binding.addresTv.setText(model.getVillage());
                binding.allprofileMemberIDno.setText(model.getMemberID());
                Glide.with(AllUserOfProfileActivity.this)
                        .load(model.getProfilepic()).into(binding.imageView2);

                String active_status_allusers = model.getActiveStatus();

                if (active_status_allusers.equals("Yes")) {

                    binding.allusersLightRed.setVisibility(View.VISIBLE);


                } else {
                    Toast.makeText(AllUserOfProfileActivity.this, "ok Donor", Toast.LENGTH_SHORT).show();
                }

                if (active_status_allusers.equals("No")) {

                    binding.allusersLightgreen.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(AllUserOfProfileActivity.this, "ok Donor", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllUserOfProfileActivity.this,
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = model.getMobileno();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                startActivity(intent);

            }
        });

    }

    private void checkfavourite() {

        DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Users");

        dbrefer.child(firebaseAuth.getUid()).child("Favorites").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isMyfavorite = snapshot.exists();
                        if (isMyfavorite) {

                            binding.imageView3.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                                    R.drawable.favorite_24, 0, 0);
                            binding.textView32.setText(R.string.removefavo);

                        } else {
                            binding.imageView3.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                                    R.drawable.ic_baseline_favorite_border_24, 0, 0);
                            binding.textView32.setText(R.string.addfavo);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AllUserOfProfileActivity.this, "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AllUserOfProfileActivity.this,
                MainActivity.class));
        finish();
    }
}


