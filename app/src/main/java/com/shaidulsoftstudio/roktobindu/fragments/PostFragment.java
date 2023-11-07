package com.shaidulsoftstudio.roktobindu.fragments;

import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.activities.MainActivity;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.Calendar;
import java.util.TimeZone;

public class PostFragment extends Fragment {
    View view;
    private Calendar calendar, cal;
    String  activeTime = "", activeDate = "";
    private ProgressDialog pd;
    String  countryCode = "";

    EditText getLocation, getMobile;
    TextView donateInfo, settotalDonate, setLastDonate, nextDonate, bloodgroup_postTv;
    AppCompatButton btnYes, btnNo;
    LinearLayout detailsLinLay;
    DatabaseReference user_ref, db_ref, databaseReference, databaseReference2;
    FirebaseAuth mAuth;
    private String[] bloodgroup, districtlist;
    private String lastDate;
    private int cur_day, cur_month, cur_year, day, month, year, totday, TotalDonate;
    private int totalDonation = 0;



    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post, container, false);

        bloodgroup = getResources().getStringArray(R.array.blood_groups);
        districtlist = getResources().getStringArray(R.array.allDistrict);

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);


        donateInfo = view.findViewById(R.id.donateInfo);
        settotalDonate = view.findViewById(R.id.settotalDonate);
        setLastDonate = view.findViewById(R.id.setLastDonate);
        nextDonate = view.findViewById(R.id.nextDonate);
        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);
        getMobile = view.findViewById(R.id.getMobile);
        getLocation = view.findViewById(R.id.getLocation);
        bloodgroup_postTv = view.findViewById(R.id.bloodgroup_postTv);
        detailsLinLay = view.findViewById(R.id.detailsLinLay);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String type = snapshot.child("Usertype").getValue().toString().trim();
                    if (type.equals("Receiver")) {

                        recevercustDialog();
                    } else {

                    }
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cal = Calendar.getInstance();
        int Day = cal.get(Calendar.DAY_OF_MONTH);
        int Month = cal.get(Calendar.MONTH);
        int Year = cal.get(Calendar.YEAR);
        int Hour = cal.get(Calendar.HOUR);
        int Min = cal.get(Calendar.MINUTE);
        month += 1;

        String ampm = "AM";

        if (cal.get(Calendar.AM_PM) == 1) {
            ampm = "PM";
        }

        if (Hour < 10) {
            activeTime += "0";
        }
        activeTime += Hour;
        activeTime += ":";

        if (Min < 10) {
            activeTime += "0";
        }

        activeTime += Min;
        activeTime += (" " + ampm);

        activeDate = Day + "/" + Month + "/" + Year;

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    getLocation.setText(snapshot.child("village").getValue().toString().trim());
                    getMobile.setText(snapshot.child("Mobileno").getValue().toString().trim());
                    bloodgroup_postTv.setText(snapshot.child("bloodGroup").getValue().toString().trim());
                    countryCode = snapshot.child("countryCode").getValue().toString().trim();


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mAuth = FirebaseAuth.getInstance();
        db_ref = FirebaseDatabase.getInstance().getReference("Users");
        user_ref = FirebaseDatabase.getInstance().getReference("Users");
        Query userQ = user_ref.child(mAuth.getCurrentUser().getUid());
        try {

            userQ.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        final Query donorQ = db_ref.child(mAuth.getCurrentUser().getUid());

                        donorQ.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    final Users donorData = dataSnapshot.getValue(Users.class);
                                    //settotalDonate.setText(donorData.getTotalDonate() + " times");
                                    if (donorData.getTotalDonate() == 0) {
                                        lastDate = "01/01/2022";
                                        setLastDonate.setText(R.string.donotdonateyet);
                                    } else {
                                        lastDate = String.valueOf(donorData.getLastDonate());
                                        setLastDonate.setText(donorData.getLastDonate());
                                    }

                                    totday = 0;
                                    if (lastDate.length() != 0) {

                                        int cnt = 0;
                                        int tot = 0;
                                        for (int i = 0; i < lastDate.length(); i++) {
                                            if (cnt == 0 && lastDate.charAt(i) == '/') {
                                                day = tot;
                                                tot = 0;
                                                cnt += 1;

                                            } else if (cnt == 1 && lastDate.charAt(i) == '/') {
                                                cnt += 1;
                                                month = tot;
                                                tot = 0;

                                            } else tot = tot * 10 + (lastDate.charAt(i) - '0');
                                        }
                                        year = tot;
                                        calendar = Calendar.getInstance(TimeZone.getDefault());
                                        cur_day = calendar.get(Calendar.DAY_OF_MONTH);
                                        cur_month = calendar.get(Calendar.MONTH) + 1;
                                        cur_year = calendar.get(Calendar.YEAR);

                                        if (day > cur_day) {
                                            cur_day += 30;
                                            cur_month -= 1;
                                        }
                                        totday += (cur_day - day);

                                        if (month > cur_month) {
                                            cur_month += 12;
                                            cur_year -= 1;
                                        }
                                        totday += ((cur_month - month) * 30);
                                        totday += ((cur_year - year) * 365);

                                        try {
                                            if (totday > 90) {
                                                donateInfo.setText(R.string.have_donatet_today);
                                                nextDonate.setVisibility(View.GONE);

                                                cur_day = calendar.get(Calendar.DAY_OF_MONTH);
                                                cur_month = calendar.get(Calendar.MONTH) + 1;
                                                cur_year = calendar.get(Calendar.YEAR);

                                                btnYes.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                       totalDonation++;
                                                       settotalDonate.setText(totalDonation+"");
                                                       String totalDonateCount=settotalDonate.getText().toString().trim();
                                                        if (getMobile.getText().length() == 0) {
                                                            getMobile.setError("Enter your contact number!");
                                                            Toast.makeText(getContext(), "Enter your contact number!",
                                                                    Toast.LENGTH_LONG).show();
                                                        }else if (getMobile.length() != 11) {
                                                            showAlert("Invalid Phone number.Enter at least 11 char");
                                                        }
                                                        else if (getLocation.getText().length() == 0) {
                                                            getLocation.setError("Enter your location!");
                                                            Toast.makeText(getContext(), "Enter your location!",
                                                                    Toast.LENGTH_LONG).show();
                                                        } else {

                                                            final String mobileno = getMobile.getText().toString().trim();
                                                            final String location = getLocation.getText().toString().trim();

                                                            TotalDonate = donorData.getTotalDonate() + 1;
                                                            db_ref.child(mAuth.getCurrentUser().getUid())
                                                                    .child("LastDonate").setValue(cur_day + "/" + cur_month + "/" + cur_year);
                                                            db_ref.child(mAuth.getCurrentUser().getUid())
                                                                    .child("TotalDonate").setValue(TotalDonate);
                                                            db_ref.child(mAuth.getCurrentUser().getUid())
                                                                    .child("ActiveStatus").setValue("Yes");
                                                            db_ref.child(mAuth.getCurrentUser().getUid())
                                                                    .child("nextDonate").setValue(90);
                                                            db_ref.child(mAuth.getCurrentUser().getUid())
                                                                    .child("TotalDonation").setValue(totalDonateCount);
                                                            db_ref.child(mAuth.getCurrentUser().getUid())
                                                                    .child("InactiveTime").setValue(System.currentTimeMillis());
                                                            db_ref.child(mAuth.getCurrentUser().getUid())
                                                                    .child("Mobileno").setValue(mobileno);
                                                            db_ref.child(mAuth.getCurrentUser().getUid())
                                                                    .child("village").setValue(location);
                                                            db_ref.child(mAuth.getCurrentUser().getUid())
                                                                    .child("countryCodewithnumber").setValue(countryCode + mobileno);

                                                        }


                                                    }
                                                });
                                            } else {
                                                pd.dismiss();
                                                donateInfo.setText(R.string.next_donatet_time);
                                                btnYes.setVisibility(View.GONE);
                                                detailsLinLay.setVisibility(View.GONE);
                                                nextDonate.setVisibility(View.VISIBLE);
                                                nextDonate.setText((90 - totday) + "\n" + "Days");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {

                                    Toast.makeText(getContext(), "Update your profile to be a donor first.", Toast.LENGTH_SHORT).show();
                                }
                                pd.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    } else {
                        Toast.makeText(getContext(), "You are not a user." + districtlist[0] + " " + bloodgroup[0], Toast.LENGTH_LONG)
                                .show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                btnYes.setVisibility(View.VISIBLE);
                db_ref.child(mAuth.getCurrentUser().getUid())
                        .child("LastDonate").removeValue();

                db_ref.child(mAuth.getCurrentUser().getUid())
                        .child("nextDonate").removeValue();

                db_ref.child(mAuth.getCurrentUser().getUid())
                        .child("ActiveDate").setValue(activeDate);

                db_ref.child(mAuth.getCurrentUser().getUid())
                        .child("ActiveTime").setValue(activeTime);
                db_ref.child(mAuth.getCurrentUser().getUid())
                        .child("donateNow").setValue("OK");

                db_ref.child(mAuth.getCurrentUser().getUid())
                        .child("TotalDonate").removeValue();

                db_ref.child(mAuth.getCurrentUser().getUid())
                        .child("LastDonate").removeValue();
                db_ref.child(mAuth.getCurrentUser().getUid())
                        .child("ActiveStatus").setValue("No");
                db_ref.child(mAuth.getCurrentUser().getUid())
                        .child("nextDonate").removeValue();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        return view;


    }

    private void recevercustDialog() {
        Dialog dialog1 = new Dialog(getContext());
        dialog1.setContentView(R.layout.custom_fail_dialog);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        dialog1.setCancelable(false);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog1.show();
        TextView msgtextView = dialog1.findViewById(R.id.msgtextView);
        msgtextView.setText(R.string.congra1);
        Button ok = dialog1.findViewById(R.id.okbtn1);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),
                        MainActivity.class));

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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