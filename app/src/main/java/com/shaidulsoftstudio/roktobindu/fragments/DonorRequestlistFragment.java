package com.shaidulsoftstudio.roktobindu.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.shaidulsoftstudio.roktobindu.activities.AboutUsActivity;
import com.shaidulsoftstudio.roktobindu.activities.MainActivity;
import com.shaidulsoftstudio.roktobindu.adapter.PostAdapter;
import com.shaidulsoftstudio.roktobindu.adapter.UserAdapter;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DonorRequestlistFragment extends Fragment {

    View view;
    private RecyclerView rcvPosts;
    private EditText searchEdt;
    private DatabaseReference databaseReference, databaseReference1;
    FirebaseAuth mAuth;
    String currentUserID;
    FirebaseUser firebaseUser;
    private PostAdapter postAdapter;
    private List<Users> postList = new ArrayList<>();
    LottieAnimationView progressLottie;

    public DonorRequestlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_donor_requestlist, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            currentUserID=firebaseUser.getUid();
        }
        rcvPosts = view.findViewById(R.id.postRCV);
        searchEdt=view.findViewById(R.id.search_list_Edit);

        progressLottie = view.findViewById(R.id.progressbar_lottie_requestlist);
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                postList.clear();

                String type = snapshot.child("Usertype").getValue().toString();
                if (type.equals("Donor")) {
                    readReceiver();
                } else {
                    readDonor();
                }

                postAdapter = new PostAdapter(getActivity(), postList,currentUserID);
                rcvPosts.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
                rcvPosts.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull final DatabaseError error) {

            }
        });

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchUser(s.toString().toLowerCase(Locale.ROOT));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUser(String searchText) {
        Query query= FirebaseDatabase.getInstance().getReference("User").orderByChild("searchtags")
                .startAt(searchText)
                .endAt(searchText+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();

                for (DataSnapshot ds:snapshot.getChildren()){

                    Users user=ds.getValue(Users.class);
                    assert  user!=null;
                    if (!user.getUid().equals(currentUserID)){

                        postList.add(user);
                    }
                }
                postAdapter=new PostAdapter(getContext(),postList,currentUserID);
                rcvPosts.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
                rcvPosts.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readReceiver() {
    }

    private void readDonor() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = dbref.orderByChild("donateNow").equalTo("OK");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Users usersmodel = ds.getValue(Users.class);
                    postList.add(usersmodel);
                }

                postAdapter.notifyDataSetChanged();
                progressLottie.setVisibility(View.GONE);
                if (postList.isEmpty()) {
                    progressLottie.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Sorry Not Found Donor wait for Update", Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addPostinView() {
        Query allPost = databaseReference.child("User");
        progressLottie.setVisibility(View.VISIBLE);

        postList.clear();
        allPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Users usersModel = ds.getValue(Users.class);
                        postList.add(usersModel);
                        postAdapter.notifyDataSetChanged();
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