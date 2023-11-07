package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.adapter.UserAdapter;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityUnionSelectedBinding;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.ArrayList;
import java.util.List;

public class UnionSelectedActivity extends AppCompatActivity {

    ActivityUnionSelectedBinding binding;
    List<Users> Union_usersList = new ArrayList<>();
    UserAdapter Union_userAdapter;
    String title_Union = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUnionSelectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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



        setSupportActionBar(binding.unionSelectToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Blood group "+title_Union);

        if (getIntent().getExtras() != null) {
            title_Union = getIntent().getStringExtra("group");

            getSupportActionBar().setTitle("Blood Group " + title_Union);


            if (title_Union.equals("My Union Donors")) {
                getUnionDonorUser();
                getSupportActionBar().setTitle("Blood Group " + title_Union);
            } else {
                readUsers();
            }

        } else {
            Toast.makeText(this, "ok test" + title_Union, Toast.LENGTH_SHORT).show();
        }

        Union_userAdapter = new UserAdapter(UnionSelectedActivity.this,
                Union_usersList);
        Union_userAdapter.notifyDataSetChanged();
        binding.recyclerviewUnion.setAdapter(Union_userAdapter);
        binding.recyclerviewUnion.setHasFixedSize(true);
    }

    private void getUnionDonorUser() {
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

                //   String Unionsearch = userType + bloodGroup + District + Upzila + Union;


                    String bloodGroup = snapshot.child("bloodGroup").getValue().toString().trim();
                    String district = snapshot.child("District").getValue().toString().trim();
                    String upzila = snapshot.child("Upzila").getValue().toString().trim();
                    String union = snapshot.child("Union").getValue().toString().trim();

                    DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Users");

                    Query union_query = dbrefer.orderByChild("Unionsearch").equalTo(result + bloodGroup+district+upzila+union);
                    union_query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Union_usersList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Users usersmodel = ds.getValue(Users.class);
                                Union_usersList.add(usersmodel);

                            }
                            Union_userAdapter.notifyDataSetChanged();
                            binding.progressbarForUnion.setVisibility(View.GONE);
                            if (Union_usersList.isEmpty()) {

                                binding.progressbarForUnion.setVisibility(View.GONE);
                                showAlert("Sorry Not Found "+bloodGroup+" Donor"+"\n" +" In"+union+"Union");
                            } else {
                                Toast.makeText(UnionSelectedActivity.this, " hey Donor", Toast.LENGTH_SHORT).show();

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

                Query query = dbrefer.orderByChild("Unionsearch").equalTo(result + title_Union);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Union_usersList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Users usersmodel = ds.getValue(Users.class);
                            Union_usersList.add(usersmodel);

                        }
                        Union_userAdapter.notifyDataSetChanged();
                        binding.progressbarForUnion.setVisibility(View.GONE);
                        if (Union_usersList.isEmpty()) {

                            binding.progressbarForUnion.setVisibility(View.GONE);
                            showAlert("OOPS Not Found " + title_Union + " Group");

                        } else {
                            Toast.makeText(UnionSelectedActivity.this, " hey Donor", Toast.LENGTH_SHORT).show();

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

        AlertDialog.Builder builder = new AlertDialog.Builder(UnionSelectedActivity.this);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(UnionSelectedActivity.this, MainActivity.class));
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
        startActivity(new Intent(UnionSelectedActivity.this,
                MainActivity.class));
        finish();
    }
}