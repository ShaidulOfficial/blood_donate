package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shaidulsoftstudio.roktobindu.adapter.FavouriteAdapter;
import com.shaidulsoftstudio.roktobindu.model.Users;
import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityProfileBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompat {
    ActivityProfileBinding binding;
    DatabaseReference dbref;
    String activeStatus;
    String profileimage;
    Uri imageUri = null;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private List<Users> modelList;
    String profilepicUrl = "", adminBloodGroup = "";
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        dbref = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        storageReference = FirebaseStorage.getInstance().getReference("Upload");
        modelList = new ArrayList<>();
        loadfavouritelist();

        binding.allFavouritesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FavouriteActivity.class));
                finish();
            }
        });
        binding.picImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfileActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(101);
            }
        });

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    adminBloodGroup = snapshot.child("bloodGroup").getValue().toString().trim();
                    activeStatus = snapshot.child("ActiveStatus").getValue().toString().trim();

                    binding.typTvAdmin.setText(snapshot.child("Usertype").getValue().toString().trim());
                    binding.groupTvAdmin.setText(adminBloodGroup);
                    binding.textViewAdmin.setText(snapshot.child("fullname").getValue().toString().trim());
                    binding.textView2Admin.setText(snapshot.child("email").getValue().toString().trim());
                    binding.mobileTvAdmin.setText(snapshot.child("Mobileno").getValue().toString().trim());
                    binding.genderTvAdmin.setText(snapshot.child("gender").getValue().toString().trim());
                    binding.divisionTvAdmin.setText(snapshot.child("Division").getValue().toString().trim());
                    binding.distTvAdmin.setText(snapshot.child("District").getValue().toString().trim());
                    binding.upzilaTvAdmin.setText(snapshot.child("Upzila").getValue().toString().trim());
                    binding.unionTvAdmin.setText(snapshot.child("Union").getValue().toString().trim());
                    binding.addresTvAdmin.setText(snapshot.child("village").getValue().toString().trim());
                    binding.profileMemberIDno.setText(snapshot.child("memberID").getValue().toString().trim());

                    profileimage = snapshot.child("Profilepic").getValue().toString();
                    Glide.with(ProfileActivity.this).load(profileimage).placeholder(R.drawable.profile_image).into(binding.imageView2);


                } else {
                    Toast.makeText(ProfileActivity.this, "" + null, Toast.LENGTH_SHORT).show();
                }


                if (activeStatus.equals("Yes")) {

                    binding.imgRedProfile.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(ProfileActivity.this, "" + null, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.backprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,
                        MainActivity.class));
                finish();
            }
        });

        binding.bloodGroupProfileImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.blood_group_who_give_donte, null);
                TextView yourBloodGroup;
                TextView canGive;
                TextView canTake;
                yourBloodGroup = dialogView.findViewById(R.id.your_blood_groupTv);
                canGive = dialogView.findViewById(R.id.give_blood_Tv);
                canTake = dialogView.findViewById(R.id.take_blood_Tv);

                yourBloodGroup.setText(adminBloodGroup);
                if (adminBloodGroup.equals("A+")) {
                    canGive.setText("A+ \nAB+");
                    canTake.setText("A+ , A- \nO+ , O-");

                } else if (adminBloodGroup.equals("A-")) {
                    canGive.setText("A+ , A- \nAB+ , AB-");
                    canTake.setText("A- \nO-");

                } else if (adminBloodGroup.equals("B+")) {
                    canGive.setText("B+ \nAB+");
                    canTake.setText("B+ , B- \nO+ , O-");
                } else if (adminBloodGroup.equals("B-")) {
                    canGive.setText("B+ , B- \nAB+ , AB-");
                    canTake.setText("B- \nO-");

                } else if (adminBloodGroup.equals("AB+")) {
                    canGive.setText("AB+");
                    canTake.setText("All Group \n(সকল গ্রুপ)");

                } else if (adminBloodGroup.equals("AB-")) {
                    canGive.setText("AB+  AB-");
                    canTake.setText("A- , B-\nO- , AB-");

                } else if (adminBloodGroup.equals("O+")) {
                    canGive.setText("A+ , B+ \nAB+ , O+");
                    canTake.setText("O+ , O-");

                } else if (adminBloodGroup.equals("O-")) {
                    canGive.setText("All Group \n(সকল গ্রুপ)");
                    canTake.setText("O-");
                } else {

                }
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();
            }
        });

        binding.updatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            HashMap<String, Object> updatePicMap = new HashMap<>();

                            updatePicMap.put("Profilepic", profilepicUrl);

                            dbref.updateChildren(updatePicMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        updateDialog();
                                    } else {

                                        Toast.makeText(ProfileActivity.this, "no update image", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        } else {
                            Toast.makeText(ProfileActivity.this, null, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        binding.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,
                        ProfileEditActivity.class);
                intent.putExtra("profilePic", profileimage);
                startActivity(intent);
            }
        });

    }

    private void loadfavouritelist() {
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
                        binding.favouriteCountTv.setText("" + modelList.size());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updateDialog() {

        Dialog dialog = new Dialog(ProfileActivity.this);
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
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            binding.imageView2.setImageURI(imageUri);
            sendImagetoStorage(imageUri);
            binding.updatePic.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(this, "not ok", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImagetoStorage(Uri imageUri) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.setIcon(R.drawable.ic_baseline_error_outline_24);
        pd.show();
        StorageReference store = storageReference.child("ProfileImages");
        StorageReference imagename = store.child("userProfile" + System.currentTimeMillis());


        imagename.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                pd.dismiss();
                Toast.makeText(ProfileActivity.this, "img uploaded", Toast.LENGTH_SHORT).show();


                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        profilepicUrl = String.valueOf(uri);

                        Toast.makeText(ProfileActivity.this, "done", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                int count = (int) progressPercent;
                pd.setMessage("Progress: " + (count++) + " %");
            }
        });

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
        startActivity(new Intent(ProfileActivity.this,
                MainActivity.class));
        finish();
    }


}