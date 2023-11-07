package com.shaidulsoftstudio.roktobindu.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.activities.AboutActivity;
import com.shaidulsoftstudio.roktobindu.activities.AboutUsActivity;
import com.shaidulsoftstudio.roktobindu.activities.DonorRegistrationActivity;
import com.shaidulsoftstudio.roktobindu.activities.FavouriteActivity;
import com.shaidulsoftstudio.roktobindu.activities.MapsActivity;
import com.shaidulsoftstudio.roktobindu.activities.ProfileActivity;


public class MenuFragment extends Fragment {

    View view;
    ProgressDialog progressDialog;
    ImageView verify_icon;
    CardView bmiCArdV, aboutus_cardV, setting_cardV, compass_fragment,
            profile_cardV, allmemberCardV, find_blood_CardV, favouritecardV,
            nearHospitalCardV;
    TextView action_title, verify_tv, verifiedTxt;
    AppCompatButton verify_btn;
    LinearLayout rateus_linlay;
    ReviewInfo reviewInfo;
    ReviewManager reviewManager;
    FirebaseAuth firebaseAuth;
    FirebaseUser fuser;

    public MenuFragment() {

        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);

        favouritecardV = view.findViewById(R.id.favouritecardV);
        setting_cardV = view.findViewById(R.id.setting_cardV);
        aboutus_cardV = view.findViewById(R.id.aboutus_cardV);
        allmemberCardV = view.findViewById(R.id.allmemberCardV);
        compass_fragment = view.findViewById(R.id.compass_fragment);
        find_blood_CardV = view.findViewById(R.id.find_blood_CardV);
        nearHospitalCardV = view.findViewById(R.id.nearHospitalCardV);
        profile_cardV = view.findViewById(R.id.profile_cardV);
        bmiCArdV = view.findViewById(R.id.bmiCArdV);
        rateus_linlay = view.findViewById(R.id.rateus_linlay);
        verify_btn = view.findViewById(R.id.verify_btn);
        verify_tv = view.findViewById(R.id.verify_tv);
        verifiedTxt = view.findViewById(R.id.verifiedTxt);
        verify_icon = view.findViewById(R.id.verify_icon);
        action_title = view.findViewById(R.id.title_tool);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        fuser = firebaseAuth.getCurrentUser();
        if (fuser.isEmailVerified()) {
            verify_btn.setVisibility(View.GONE);
            verify_tv.setVisibility(View.GONE);
            verify_icon.setVisibility(View.VISIBLE);
            verifiedTxt.setVisibility(View.VISIBLE);

        } else {
            verify_btn.setVisibility(View.VISIBLE);
            verify_tv.setVisibility(View.VISIBLE);
            verify_icon.setVisibility(View.GONE);
            verify_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fuser.isEmailVerified()) {
                        Toast.makeText(getActivity(), "Already verified", Toast.LENGTH_SHORT).show();
                    } else {

                        emailverificationDialog();
                    }

                }
            });
        }

        bmiCArdV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BMIFragment bmiFragment=new BMIFragment();
                FragmentTransaction bmifragTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                bmifragTransaction.replace(R.id.frameLayout_dashboard,bmiFragment)
                        .commit();
            }
        });

        rateus_linlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewManager = ReviewManagerFactory.create(getActivity());
                Task<ReviewInfo> managerInfotask = reviewManager.requestReviewFlow();
                managerInfotask.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                    @Override
                    public void onComplete(@NonNull Task<ReviewInfo> task) {
                        if (task.isSuccessful()) {
                            reviewInfo = task.getResult();
                            Task<Void> flow = reviewManager.launchReviewFlow(getActivity(), reviewInfo);
                            flow.addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "review failed to start", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        allmemberCardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment dashBoardFragment = new DashboardFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_dashboard, dashBoardFragment)
                        .commit();
            }
        });
        profile_cardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        favouritecardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FavouriteActivity.class);
                startActivity(intent);
            }
        });
        aboutus_cardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        compass_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment dashBoardFragment = new QiblaFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_dashboard, dashBoardFragment)
                        .commit();
            }
        });

        setting_cardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment settingFragment = new SettingFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_dashboard, settingFragment)
                        .commit();

            }
        });


        find_blood_CardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment allBloodGroupsFragment = new AllBloodGroupsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_dashboard, allBloodGroupsFragment)
                        .commit();
            }
        });
        nearHospitalCardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void emailverificationDialog() {
        showAlert();
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Verify Email... ");
        builder.setMessage("Are you sure?\nyou want to send verification mail\nyour Email." + fuser.getEmail() + "\nAfter send Check your Email inbox or spam");
        builder.setIcon(R.drawable.check_circle_24);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendMailVerification();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void sendMailVerification() {

        progressDialog
                .setMessage("Sending email verification link \n to your Email" + "\n" + fuser.getEmail());
        progressDialog.show();
        fuser.sendEmailVerification().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Link sent, Check your Email " + fuser.getEmail(), Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "failed due to" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}