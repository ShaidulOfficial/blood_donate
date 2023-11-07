package com.shaidulsoftstudio.roktobindu.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.activities.CategorySelectedActivity;
import com.shaidulsoftstudio.roktobindu.activities.MainActivity;
import com.shaidulsoftstudio.roktobindu.adapter.UserAdapter;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.ArrayList;
import java.util.List;


public class AllBloodGroupsFragment extends Fragment {

    View view;
    List<Users> usersList = new ArrayList<>();
    UserAdapter userAdapter;
    String aptitle = "", amtitle = "", bptitle = "", bmtitle = "", abptitle = "", abmtitle = "", optitle = "", omtitle = "";
    LottieAnimationView progressbar;
    RecyclerView all_blood_grp_RCV;

    CardView a_plus, a_minus, b_plus, b_minus, ab_plus, ab_minus, o_plus, o_minus;
    TextView choosgrp,a_plusTv, a_minusTv, b_plusTv, b_minusTv, ab_plusTv, ab_minusTv, o_plusTv, o_minusTv;

    public AllBloodGroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_blood_groups, container, false);
        getActivity().setTitle("All Bloodgroup");


        all_blood_grp_RCV = view.findViewById(R.id.all_blood_grp_RCV);
        progressbar = view.findViewById(R.id.progressbar_allblood_group);

        choosgrp = view.findViewById(R.id.choosgrp);
        a_plusTv = view.findViewById(R.id.aplusTv);
        a_minusTv = view.findViewById(R.id.aminusTv);
        b_plusTv = view.findViewById(R.id.bplusTv);
        b_minusTv = view.findViewById(R.id.bminusTv);
        ab_plusTv = view.findViewById(R.id.abplusTv);
        ab_minusTv = view.findViewById(R.id.abminusTv);
        o_plusTv = view.findViewById(R.id.oplusTv);
        o_minusTv = view.findViewById(R.id.ominusTv);

        a_plus = view.findViewById(R.id.a_plus);
        a_minus = view.findViewById(R.id.a_minus);
        b_plus = view.findViewById(R.id.b_plus);
        b_minus = view.findViewById(R.id.b_minus);
        ab_plus = view.findViewById(R.id.ab_plus);
        ab_minus = view.findViewById(R.id.ab_minus);
        o_plus = view.findViewById(R.id.o_plus);
        o_minus = view.findViewById(R.id.o_minus);

        aptitle = a_plusTv.getText().toString().trim();
        amtitle = a_minusTv.getText().toString().trim();
        bptitle = b_plusTv.getText().toString().trim();
        bmtitle = b_minusTv.getText().toString().trim();
        abptitle = ab_plusTv.getText().toString().trim();
        abmtitle = ab_minusTv.getText().toString().trim();
        optitle = o_plusTv.getText().toString().trim();
        omtitle = o_minusTv.getText().toString().trim();

        userAdapter = new UserAdapter(getActivity(),
                usersList);
        userAdapter.notifyDataSetChanged();
        all_blood_grp_RCV.setAdapter(userAdapter);
        all_blood_grp_RCV.setHasFixedSize(true);

        a_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + aptitle);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usersList.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Users usersmodel = ds.getValue(Users.class);
                                    usersList.add(usersmodel);

                                }
                                userAdapter.notifyDataSetChanged();
                                choosgrp.setVisibility(View.GONE);
                                progressbar.setVisibility(View.GONE);
                                choosgrp.setVisibility(View.GONE);
                                if (usersList.isEmpty()) {

                                    progressbar.setVisibility(View.GONE);
                                    choosgrp.setVisibility(View.GONE);
                                    choosgrp.setVisibility(View.GONE);
                                    showAlert("দুঃখিত! "+ aptitle + " গ্রুপটি \n খুজে পাওয়া যাচ্ছে না");
                                } else {

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
        });

        a_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + amtitle);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usersList.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Users usersmodel = ds.getValue(Users.class);
                                    usersList.add(usersmodel);

                                }
                                userAdapter.notifyDataSetChanged();
                                progressbar.setVisibility(View.GONE);
                                choosgrp.setVisibility(View.GONE);
                                if (usersList.isEmpty()) {
                                    progressbar.setVisibility(View.GONE);
                                    choosgrp.setVisibility(View.GONE);
                                    showAlert("দুঃখিত! "+ amtitle + " গ্রুপটি \n খুজে পাওয়া যাচ্ছে না");
                                } else {

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
        });

        b_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + bptitle);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usersList.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Users usersmodel = ds.getValue(Users.class);
                                    usersList.add(usersmodel);

                                }
                                userAdapter.notifyDataSetChanged();
                                progressbar.setVisibility(View.GONE);
                                choosgrp.setVisibility(View.GONE);
                                if (usersList.isEmpty()) {

                                    progressbar.setVisibility(View.GONE);
                                    choosgrp.setVisibility(View.GONE);
                                    showAlert("দুঃখিত! "+ bptitle + " গ্রুপটি \n খুজে পাওয়া যাচ্ছে না");
                                } else {

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
        });

        b_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + bmtitle);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usersList.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Users usersmodel = ds.getValue(Users.class);
                                    usersList.add(usersmodel);

                                }
                                userAdapter.notifyDataSetChanged();
                                progressbar.setVisibility(View.GONE);
                                choosgrp.setVisibility(View.GONE);
                                if (usersList.isEmpty()) {

                                    progressbar.setVisibility(View.GONE);
                                    choosgrp.setVisibility(View.GONE);
                                    showAlert("দুঃখিত! "+ bmtitle + " গ্রুপটি \n খুজে পাওয়া যাচ্ছে না");
                                } else {

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
        });

        ab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + abptitle);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usersList.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Users usersmodel = ds.getValue(Users.class);
                                    usersList.add(usersmodel);

                                }
                                userAdapter.notifyDataSetChanged();
                                progressbar.setVisibility(View.GONE);
                                choosgrp.setVisibility(View.GONE);
                                if (usersList.isEmpty()) {

                                    progressbar.setVisibility(View.GONE);
                                    choosgrp.setVisibility(View.GONE);
                                    showAlert("দুঃখিত! "+ abptitle + " গ্রুপটি \n খুজে পাওয়া যাচ্ছে না");
                                } else {

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
        });

        ab_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + abmtitle);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usersList.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Users usersmodel = ds.getValue(Users.class);
                                    usersList.add(usersmodel);

                                }
                                userAdapter.notifyDataSetChanged();
                                progressbar.setVisibility(View.GONE);
                                choosgrp.setVisibility(View.GONE);
                                if (usersList.isEmpty()) {

                                    progressbar.setVisibility(View.GONE);
                                    choosgrp.setVisibility(View.GONE);
                                    showAlert("দুঃখিত! "+ abmtitle + " গ্রুপটি \n খুজে পাওয়া যাচ্ছে না");
                                       } else {

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
        });

        o_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + optitle);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usersList.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Users usersmodel = ds.getValue(Users.class);
                                    usersList.add(usersmodel);

                                }
                                userAdapter.notifyDataSetChanged();
                                progressbar.setVisibility(View.GONE);
                                choosgrp.setVisibility(View.GONE);
                                if (usersList.isEmpty()) {

                                    progressbar.setVisibility(View.GONE);
                                    choosgrp.setVisibility(View.GONE);
                                    showAlert("দুঃখিত! "+ optitle + " গ্রুপটি \n খুজে পাওয়া যাচ্ছে না");
                                } else {

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
        });

        o_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Query query = dbrefer.orderByChild("Groupsearch").equalTo(result + omtitle);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usersList.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Users usersmodel = ds.getValue(Users.class);
                                    usersList.add(usersmodel);

                                }
                                userAdapter.notifyDataSetChanged();
                                progressbar.setVisibility(View.GONE);
                                choosgrp.setVisibility(View.GONE);
                                if (usersList.isEmpty()) {

                                    progressbar.setVisibility(View.GONE);
                                    choosgrp.setVisibility(View.GONE);
                                    showAlert("দুঃখিত! "+ omtitle + " গ্রুপটি \n খুজে পাওয়া যাচ্ছে না");
                                } else {

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
        });
        return view;
    }

    private void showAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}