package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityProfileEditBinding;

public class ProfileEditActivity extends AppCompat {
    ActivityProfileEditBinding binding;

    String updateName, updateMobile, updateAdress, updateuserType, profilepicIntent;

    DatabaseReference refDb;

    ArrayAdapter<CharSequence> userTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        profilepicIntent = getIntent().getStringExtra("profilePic");
        Glide.with(ProfileEditActivity.this).load(profilepicIntent)
                .placeholder(R.drawable.profile_image).into(binding.imageView2);

        //*****************************************spinner start**************************************
        userTypeAdapter = ArrayAdapter.createFromResource(ProfileEditActivity.this,
                R.array.userType_update, R.layout.spinner_layout_blood);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.userTypeSpinnerupdate.setAdapter(userTypeAdapter);

        //*****************spinner end**************************************************************

        binding.backprofileedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileEditActivity.this,
                        ProfileActivity.class));
            }
        });


        refDb = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        refDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.typTv.setText(snapshot.child("Usertype").getValue().toString().trim());
                    binding.groupTv.setText(snapshot.child("bloodGroup").getValue().toString().trim());
                    binding.namedT.setText(snapshot.child("fullname").getValue().toString().trim());
                    binding.mobileedT.setText(snapshot.child("Mobileno").getValue().toString().trim());
                    binding.addresedT.setText(snapshot.child("village").getValue().toString().trim());
                    binding.userTypeSpinnerupdate.setSelected(Boolean.parseBoolean(snapshot.child("Usertype").getValue().toString().trim()));

                    String active_status = snapshot.child("ActiveStatus").getValue().toString().trim();


                    if (active_status.equals("Yes")) {

                        binding.profileEditLightRed.setVisibility(View.VISIBLE);

                    } else {
                        //Toast.makeText(ProfileEditActivity.this, "ok Donor", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    //Toast.makeText(ProfileEditActivity.this, "data set", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ProfileEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //update korte**********************************************************************************************
        binding.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            progressDialog.setMessage("Updating...");
                            progressDialog.show();

                            String bloodGroup = snapshot.child("bloodGroup").getValue().toString().trim();
                            String district = snapshot.child("District").getValue().toString().trim();
                            String upzila = snapshot.child("Upzila").getValue().toString().trim();
                            String union = snapshot.child("Union").getValue().toString().trim();

                            updateName = binding.namedT.getText().toString().trim();
                            updateMobile = binding.mobileedT.getText().toString().trim();
                            updateAdress = binding.addresedT.getText().toString().trim();
                            updateuserType = binding.userTypeSpinnerupdate.getSelectedItem().toString().trim();

                            String groupSearch = updateuserType + bloodGroup;
                            String districtSearch = updateuserType + bloodGroup + district;
                            String upzilaSearch = updateuserType + bloodGroup + district + upzila;
                            String unionSearch = updateuserType + bloodGroup + district + upzila + union;

                            HashMap<String, Object> updateProfileMap = new HashMap<>();
                            updateProfileMap.put("fullname", updateName);
                            updateProfileMap.put("village", updateAdress);
                            updateProfileMap.put("Mobileno", updateMobile);
                            updateProfileMap.put("Usertype", updateuserType);
                            updateProfileMap.put("Groupsearch", groupSearch);
                            updateProfileMap.put("Districtsearch", districtSearch);
                            updateProfileMap.put("Upzillasearch", upzilaSearch);
                            updateProfileMap.put("Unionsearch", unionSearch);

                            refDb.updateChildren(updateProfileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        updateDialog();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProfileEditActivity.this, "no update", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            });

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(ProfileEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void updateDialog() {
        Dialog dialog = new Dialog(ProfileEditActivity.this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();
        Button ok = dialog.findViewById(R.id.okbtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileEditActivity.this,
                        ProfileActivity.class));
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileEditActivity.this, ProfileActivity.class));
        finish();
    }

}




