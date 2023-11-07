package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;
import com.shaidulsoftstudio.roktobindu.adapter.UserAdapter;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.ArrayList;
import java.util.List;

import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityUpzilaDonorSelectedBinding;

public class UpzilaDonorSelectedActivity extends AppCompat {
ActivityUpzilaDonorSelectedBinding binding;
    List<Users> usersList = new ArrayList<>();
    UserAdapter userAdapter;
    String title = "";
    String bloodGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpzilaDonorSelectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.upzilaSelectToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            title = getIntent().getStringExtra("group");
            getSupportActionBar().setTitle("Same Blood Group My Upzilla");


            if (title.equals("My Upzilla Donors")) {
                getUpzilaDonorUser();
                getSupportActionBar().setTitle("Same Blood Group My Upzilla");
            } else {
                readUsers();
            }

        } else {
            Toast.makeText(this, "ok test" + title, Toast.LENGTH_SHORT).show();
        }

        userAdapter = new UserAdapter(UpzilaDonorSelectedActivity.this,
                usersList);
        userAdapter.notifyDataSetChanged();
        binding.recyclerview.setAdapter(userAdapter);
        binding.recyclerview.setHasFixedSize(true);



    }
    private void getUpzilaDonorUser() {
        DatabaseReference refDb = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        refDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String result = "";
                String type = snapshot.child("Usertype").getValue().toString();
                if (type.equals("Donor")) {
                    result = "Receiver";
                } else {
                    result = "Donor";
                }

                 bloodGroup = snapshot.child("bloodGroup").getValue().toString().trim();
                String district = snapshot.child("District").getValue().toString().trim();
                String upzila = snapshot.child("Upzila").getValue().toString().trim();


                DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Users");

                Query query = dbrefer.orderByChild("Upzillasearch").equalTo(result + bloodGroup+district+upzila);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usersList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Users usersmodel = ds.getValue(Users.class);
                            usersList.add(usersmodel);

                        }
                        userAdapter.notifyDataSetChanged();
                        binding.progressbar.setVisibility(View.GONE);
                        if (usersList.isEmpty()) {

                            binding.progressbar.setVisibility(View.GONE);
                            showAlert("Sorry Not Found "+bloodGroup+" Donor"+"\n" +" In"+district+"District "+upzila+" Upzila");
                        } else {
                            Toast.makeText(UpzilaDonorSelectedActivity.this, " hey Donor", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        DatabaseReference refDb = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        refDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String result = "";
                String type = snapshot.child("Usertype").getValue().toString().trim();
                if (type.equals("Donor")) {
                    result = "Receiver";
                } else {
                    result = "Donor";
                }

                DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Users");

                Query query = dbrefer.orderByChild("Upzillasearch").equalTo(result + title);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usersList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Users usersmodel = ds.getValue(Users.class);
                            usersList.add(usersmodel);

                        }
                        userAdapter.notifyDataSetChanged();
                        binding.progressbar.setVisibility(View.GONE);
                        if (usersList.isEmpty()) {

                            binding.progressbar.setVisibility(View.GONE);
                            showAlert("OOPS Not Found " + title + " Group");
                        } else {
                            Toast.makeText(UpzilaDonorSelectedActivity.this, " hey Donor", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showAlert(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UpzilaDonorSelectedActivity.this);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(UpzilaDonorSelectedActivity.this, MainActivity.class));
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
    }
}