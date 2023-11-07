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
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.adapter.UserAdapter;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityCategorySelectedBinding;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.ArrayList;
import java.util.List;


public class CategorySelectedActivity extends AppCompat {
    ActivityCategorySelectedBinding binding;

    List<Users> usersList = new ArrayList<>();
    UserAdapter userAdapter;
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategorySelectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.categoryToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (getIntent().getExtras() != null) {
            title = getIntent().getStringExtra("group");

            getSupportActionBar().setTitle("Blood Group " + title);


            if (title.equals("Compatible with me")) {
                getcompetibleUser();
                getSupportActionBar().setTitle("Who will give me Blood Now ?");
            } else {
                readUsers();
            }

        } else {
            Toast.makeText(this, "ok test" + title, Toast.LENGTH_SHORT).show();
        }



        userAdapter = new UserAdapter(CategorySelectedActivity.this,
                usersList);
        userAdapter.notifyDataSetChanged();
        binding.recyclerview.setAdapter(userAdapter);
        binding.recyclerview.setHasFixedSize(true);
    }

    private void getcompetibleUser() {
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

                String bloodGroup = snapshot.child("bloodGroup").getValue().toString().trim();


                DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Users");

                Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + bloodGroup);
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
                            showAlert("Sorry Not Found your competitive " + bloodGroup + " Group");
                        } else {
                            Toast.makeText(CategorySelectedActivity.this, " hey Donor", Toast.LENGTH_SHORT).show();

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

                Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + title);
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
                            Toast.makeText(CategorySelectedActivity.this, " hey Donor", Toast.LENGTH_SHORT).show();

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

        AlertDialog.Builder builder = new AlertDialog.Builder(CategorySelectedActivity.this);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(CategorySelectedActivity.this, MainActivity.class));
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

}