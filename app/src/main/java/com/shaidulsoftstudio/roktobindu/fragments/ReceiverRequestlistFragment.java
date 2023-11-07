package com.shaidulsoftstudio.roktobindu.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.shaidulsoftstudio.roktobindu.adapter.PostAdapter;
import com.shaidulsoftstudio.roktobindu.adapter.PostforReceiverAdapter;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.ArrayList;
import java.util.List;


public class ReceiverRequestlistFragment extends Fragment {


    View view;
    private RecyclerView rcvPosts;
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private PostforReceiverAdapter postforReceiverAdapter;
    private List<Users> postList_rcvr;

    LottieAnimationView progressLottie;

    public ReceiverRequestlistFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_receiver_requestlist, container, false);
        rcvPosts = view.findViewById(R.id.postRCV_rcvr);
        progressLottie = view.findViewById(R.id.progressbar_lottie_requestlist_recvr);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        postList_rcvr = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        addPostinViewinRecever();
        postforReceiverAdapter = new PostforReceiverAdapter(getContext(), postList_rcvr);
        postforReceiverAdapter.notifyDataSetChanged();
        rcvPosts.setAdapter(postforReceiverAdapter);
        rcvPosts.setHasFixedSize(true);


        return view;
    }

    private void addPostinViewinRecever() {
        Query allPost = databaseReference.child("Receiver_posts");
        progressLottie.setVisibility(View.VISIBLE);

        postList_rcvr.clear();
        allPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Users usersModel = ds.getValue(Users.class);
                        postList_rcvr.add(usersModel);
                        postforReceiverAdapter.notifyDataSetChanged();
                    }
                    progressLottie.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "Database is empty now!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}