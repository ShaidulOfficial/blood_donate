package com.shaidulsoftstudio.roktobindu.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.activities.MainActivity;
import com.shaidulsoftstudio.roktobindu.adapter.UserAdapter;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {

    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    List<Users> userList = new ArrayList<>();
    ;

    FirebaseUser firebaseUser;
    UserAdapter userAdapter;
    LottieAnimationView progressbar;
    String currentUserID;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.user_recyclerview);
        progressbar = view.findViewById(R.id.progressbar_lottie);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {

            currentUserID = firebaseUser.getUid();
        } else {
            Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                userList.clear();

                String type = snapshot.child("Usertype").getValue().toString();
                if (type.equals("Donor")) {
                    readReceiver();
                } else {
                    readDonor();
                }


                userAdapter = new UserAdapter(getActivity(), userList);
                recyclerView.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
                recyclerView.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull final DatabaseError error) {

            }
        });


    }

    private void readDonor() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = dbref.orderByChild("Usertype").equalTo("Donor");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Users usersmodel = ds.getValue(Users.class);
                    userList.add(usersmodel);
                }
                userAdapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);
                if (userList.isEmpty()) {

                    progressbar.setVisibility(View.GONE);
                    showAlert("Sorry Not Found Donor\n wait for Update");
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readReceiver() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = dbref.orderByChild("Usertype").equalTo("Receiver");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Users usersmodel = ds.getValue(Users.class);
                    userList.add(usersmodel);
                }
                userAdapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);
                if (userList.isEmpty()) {
                    Toast.makeText(getActivity(), "No receiver", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
}