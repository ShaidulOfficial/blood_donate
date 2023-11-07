package com.shaidulsoftstudio.roktobindu.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.model.Users;

import java.util.Calendar;


public class PostReceiverFragment extends Fragment {

    View view;
    DatabaseReference databaseReference, databaseReference2;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Calendar cal;
    String uid;
    String profilePic = "", Mobileno = "", district = "", upzila = "", patientBlood_group = "",
            union = "", usertype = "", patientAge = "",patientname,hospitalname,bloodbag_quantity;
    String Time = "", Date = "";
    Spinner patient_blood_group_spinner;
    RadioGroup patientRadiogroup;
    RadioButton selectedRadiobtn, patientRbmale, patientRbfemale, patientRbbaby;
    EditText getMobileEdt_rcvr, getLocationEdt_rcvr, patientageEdt_rcvr,blood_bag_quantity,hospital_name,patient_name;
    AppCompatButton postBtn;
    TextView detaildistpostTv, detailupzilapostTv;

    public PostReceiverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_receiver, container, false);
        currentUser = mAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        getMobileEdt_rcvr = view.findViewById(R.id.getMobile_rcvr);
        blood_bag_quantity = view.findViewById(R.id.blood_bag_quantity);
        patient_name = view.findViewById(R.id.patient_name);
        hospital_name = view.findViewById(R.id.hospital_name);
        getLocationEdt_rcvr = view.findViewById(R.id.getLocation_rcvr);
        detaildistpostTv = view.findViewById(R.id.detaildistpost_rcvr);
        detailupzilapostTv = view.findViewById(R.id.detailupzilapost_rcvr);
        postBtn = view.findViewById(R.id.postbtn_rcvr);
        patientRadiogroup = view.findViewById(R.id.patientRadiogroup);
        patientRbmale = view.findViewById(R.id.patientRbmale);
        patientRbfemale = view.findViewById(R.id.patientRbfemale);
        patientRbbaby = view.findViewById(R.id.patientRbbaby);
        patient_blood_group_spinner = view.findViewById(R.id.patient_blood_group_spinner);
        patientageEdt_rcvr = view.findViewById(R.id.patientageEdt_rcvr);


        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Mobileno = snapshot.child("Mobileno").getValue().toString().trim();
                    district = snapshot.child("District").getValue().toString().trim();
                    usertype = snapshot.child("Usertype").getValue().toString().trim();
                    upzila = snapshot.child("Upzila").getValue().toString().trim();
                    union = snapshot.child("Union").getValue().toString().trim();
                    profilePic = snapshot.child("Profilepic").getValue().toString().trim();
                    getMobileEdt_rcvr.setText(Mobileno);
                    detaildistpostTv.setText(district);
                    detailupzilapostTv.setText(upzila);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        month += 1;

        String ampm = "AM";

        if (cal.get(Calendar.AM_PM) == 1) {
            ampm = "PM";
        }

        if (hour < 10) {
            Time += "0";
        }
        Time += hour;
        Time += ":";

        if (min < 10) {
            Time += "0";
        }

        Time += min;
        Time += (" " + ampm);

        Date = day + "/" + month + "/" + year;

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Receiver_posts");

        try {
            postBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedRadiobtn = patientRadiogroup.findViewById(patientRadiogroup.getCheckedRadioButtonId());
                    patientAge = patientageEdt_rcvr.getText().toString().trim();
                    patientBlood_group = patient_blood_group_spinner.getSelectedItem().toString().trim();

                    patientname=patient_name.getText().toString().trim();
                    hospitalname=hospital_name.getText().toString().trim();
                    bloodbag_quantity=blood_bag_quantity.getText().toString().trim();
                    final Query findname = firebaseDatabase.getReference("Users").child(uid);


                    if (getMobileEdt_rcvr.getText().length() == 0) {
                        getMobileEdt_rcvr.setError("Enter your contact number!");
                        Toast.makeText(getContext(), "Enter your contact number!",
                                Toast.LENGTH_LONG).show();
                    } else if (getLocationEdt_rcvr.getText().length() == 0) {
                        getLocationEdt_rcvr.setError("Enter your location!");
                        Toast.makeText(getContext(), "Enter your location!",
                                Toast.LENGTH_LONG).show();
                    } else if (patientageEdt_rcvr.getText().length() == 0) {
                        patientageEdt_rcvr.setError("Enter patient age");
                        Toast.makeText(getContext(), "Enter patient age", Toast.LENGTH_SHORT).show();

                    }else if (blood_bag_quantity.getText().length() == 0) {
                        blood_bag_quantity.setError("Enter blood quantity");
                        Toast.makeText(getContext(), "Enter blood quantity", Toast.LENGTH_SHORT).show();

                    }else if (hospital_name.getText().length() == 0) {
                        hospital_name.setError("Enter hospital name");
                        Toast.makeText(getContext(), "Enter hospital name", Toast.LENGTH_SHORT).show();

                    }else if (patient_name.getText().length() == 0) {
                        patient_name.setError("Enter patient name");
                        Toast.makeText(getContext(), "Enter patient name", Toast.LENGTH_SHORT).show();

                    }
                    else if (patientRadiogroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getContext(), "Please select Patient type", Toast.LENGTH_SHORT).show();
                    } else if (patientBlood_group.equals("Select Blood Group")) {
                        Toast.makeText(getContext(), "Blood group required!", Toast.LENGTH_SHORT).show();
                    } else {

                        findname.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    databaseReference.child(uid).child("fullname").setValue(snapshot.getValue(Users.class).getFullname());
                                    databaseReference.child(uid).child("Mobileno").setValue(getMobileEdt_rcvr.getText().toString().trim());
                                    databaseReference.child(uid).child("village").setValue(getLocationEdt_rcvr.getText().toString().trim());
                                    databaseReference.child(uid).child("District").setValue(district);
                                    databaseReference.child(uid).child("Upzila").setValue(upzila);
                                    databaseReference.child(uid).child("Union").setValue(union);
                                    databaseReference.child(uid).child("Usertype").setValue(usertype);
                                    databaseReference.child(uid).child("time").setValue(Time);
                                    databaseReference.child(uid).child("patientAge").setValue(patientAge);
                                    databaseReference.child(uid).child("patientType").setValue(selectedRadiobtn.getText().toString().trim());
                                    databaseReference.child(uid).child("patientBloodgroup").setValue(patientBlood_group);
                                    databaseReference.child(uid).child("date").setValue(Date);
                                    databaseReference.child(uid).child("Profilepic").setValue(profilePic);
                                    databaseReference.child(uid).child("PatientName").setValue(patientname);
                                    databaseReference.child(uid).child("HospitalName").setValue(hospitalname);
                                    databaseReference.child(uid).child("BloodBagQuantity").setValue(bloodbag_quantity);
                                    customDialog();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void customDialog() {
        Dialog dialog1 = new Dialog(getContext());
        dialog1.setContentView(R.layout.custom_layout_dialog_for_any);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.setCancelable(false);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog1.show();
        TextView msgtextView = dialog1.findViewById(R.id.msgTextv);
        msgtextView.setText(R.string.postMessege);
        Button ok = dialog1.findViewById(R.id.customokbtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
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