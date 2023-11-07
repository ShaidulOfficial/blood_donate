package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shaidulsoftstudio.roktobindu.model.Users;
import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityDonorRegistrationBinding;

import java.util.HashMap;

public class DonorRegistrationActivity extends AppCompat {

    ActivityDonorRegistrationBinding binding;
    ArrayAdapter<CharSequence> genderAdapter, divisionAdapter, districtAdapter, upzillaAdapter, unionAdapter, bloodGroupAdapter, userTypeAdapter;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    Users usersmodel;
    // String url="https://usitfo.com/Blood/register.php";
    String url;

    RadioGroup Radiogroup, radiogroup_for_usertype;
    RadioButton select_Radiobtn, select_Radiobtn_forusertype, Rbmale, Rbfemale, Rbother, RbDonor, RbReceiver;
    FirebaseAuth mAuth;
    String currentUserId, divisionSpin, districtSpin, upzelaSpin, unionSpin;
    DatabaseReference databaseReference;
    String gender = "", fullname = "", mobile = "", countryCodenumber = "",
            countryCodewithnumber = "", bloodGroup = "", District = "", Division = "",
            Upzila = "", Union = "", village = "", email = "", password = "", confirmPassword = "";
    Uri imageUri = null;
    long maxId;
    final int PROFILE_PIC = 101;
    String profilepicUrl, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonorRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Radiogroup = findViewById(R.id.Radiogroup);
        radiogroup_for_usertype = findViewById(R.id.radiogroup_for_usertype);
        Rbmale = findViewById(R.id.Rbmale);
        Rbfemale = findViewById(R.id.Rbfemale);
        Rbother = findViewById(R.id.Rbother);
        RbDonor = findViewById(R.id.RbDonor);
        RbReceiver = findViewById(R.id.RbReceiver);

        usersmodel = new Users();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        storageReference = storage.getReference("Upload");

        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
        } else {
            Toast.makeText(this, "no account have now", Toast.LENGTH_SHORT).show();
        }
        progressDialog = new ProgressDialog(DonorRegistrationActivity.this);
        progressDialog.setTitle("Please wait!!");
        progressDialog.setMessage("Create your Account");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIcon(R.drawable.ic_baseline_error_outline_24);

        binding.backLoginActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DonorRegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });

        binding.backbtnregist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DonorRegistrationActivity.this,
                        LoginActivity.class));
                finish();
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxId = snapshot.getChildrenCount() + 100;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //================================= spinner start  ========================================================

        // genderAdapter = ArrayAdapter.createFromResource(DonorRegistrationActivity.this,
        //        R.array.gender, R.layout.spinner_layout_gender);
        bloodGroupAdapter = ArrayAdapter.createFromResource(DonorRegistrationActivity.this,
                R.array.blood_groups, R.layout.spinner_layout);
        //  userTypeAdapter = ArrayAdapter.createFromResource(DonorRegistrationActivity.this,
        //        R.array.userType, R.layout.spinner_layout);
        divisionAdapter = ArrayAdapter.createFromResource(DonorRegistrationActivity.this,
                R.array.allDivision, R.layout.spinner_layout);


        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // binding.userTypeSpinner.setAdapter(userTypeAdapter);
        binding.bloodgrpSpinner.setAdapter(bloodGroupAdapter);
        binding.divisionSpinner.setAdapter(divisionAdapter);
        // binding.genderSpinner.setAdapter(genderAdapter);

        binding.divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                divisionSpin = binding.divisionSpinner.getSelectedItem().toString().trim();

                int parentId = parent.getId();
                if (parentId == R.id.divisionSpinner) {
                    switch (divisionSpin) {
                        case "Select Your Division":
                            districtAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.defaultdistrict, R.layout.spinner_layout);
                            break;
                        case "Dhaka(ঢাকা)":
                            districtAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.dhaka_division, R.layout.spinner_layout);
                            break;
                        case "Rajshahi(রাজশাহী)":
                            districtAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.rajshahi_division, R.layout.spinner_layout);
                            break;
                        case "Sylhet(সিলেট)":
                            districtAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.sylhet_division, R.layout.spinner_layout);
                            break;
                        case "Mymensingh(ময়মনসিংহ)":
                            districtAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.mymensingh_division, R.layout.spinner_layout);
                            break;
                        case "Barishal(বরিশাল)":
                            districtAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.barishal_division, R.layout.spinner_layout);
                            break;
                        case "Rangpur(রংপুর)":
                            districtAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.rangpur_division, R.layout.spinner_layout);
                            break;
                        case "Chittagong(চট্টগ্রাম)":
                            districtAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.chittagong_division, R.layout.spinner_layout);
                            break;
                        case "Khulna(খুলনা)":
                            districtAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                    , R.array.khulna_division, R.layout.spinner_layout);
                            break;


                        default:
                            break;
                    }
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.districtSpinner.setAdapter(districtAdapter);
                    binding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            districtSpin = binding.districtSpinner.getSelectedItem().toString().trim();
                            int parentId = parent.getId();
                            if (parentId == R.id.districtSpinner) {
                                switch (districtSpin) {
                                    case "Select Your District":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.defaultupzilla, R.layout.spinner_layout);
                                        break;
                                    case "Bandarban(বান্দরবন)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Bandarban, R.layout.spinner_layout);
                                        break;
                                    case "Barguna(বরগুনা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Barguna, R.layout.spinner_layout);
                                        break;
                                    case "Bagura(বগুড়া)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Bogra, R.layout.spinner_layout);
                                        break;
                                    case "Brahmanbaria(ব্রাহ্মণবাড়িয়া)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Brahmanbaria, R.layout.spinner_layout);
                                        break;
                                    case "Cumilla(কুমিল্লা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.cumilla, R.layout.spinner_layout);
                                        break;
                                    case "Chandpur(চাঁদপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Chandpur, R.layout.spinner_layout);
                                        break;
                                    case "Bagerhat(বাগেরহাট)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Bagerhat, R.layout.spinner_layout);
                                        break;
                                    case "Barisal(বরিশাল)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Barisal, R.layout.spinner_layout);
                                        break;
                                    case "Bhola(ভোলা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Bhola, R.layout.spinner_layout);
                                        break;
                                    case "Chittagong(চিটাগাং)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Chittagong, R.layout.spinner_layout);
                                        break;
                                    case "Chuadanga(চুয়াডাঙ্গা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Chuadanga, R.layout.spinner_layout);
                                        break;
                                    case "Cox\'s Bazar(কক্সবাজার)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.CoxsBazar, R.layout.spinner_layout);
                                        break;
                                    case "Dinajpur (দিনাজপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Dinajpur, R.layout.spinner_layout);
                                        break;
                                    case "Dhaka(ঢাকা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Dhaka, R.layout.spinner_layout);
                                        break;
                                    case "Faridpur(ফরিদপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Faridpur, R.layout.spinner_layout);
                                        break;
                                    case "Feni(ফেনী)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.feni, R.layout.spinner_layout);
                                        break;
                                    case "Gaibandha(গাইবান্ধা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Gaibandha, R.layout.spinner_layout);
                                        break;
                                    case "Gazipur(গাজীপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Gazipur, R.layout.spinner_layout);
                                        break;
                                    case "Gopalganj(গোপালগঞ্জ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Gopalganj, R.layout.spinner_layout);
                                        break;
                                    case "Habiganj(হবিগঞ্জ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Habiganj, R.layout.spinner_layout);
                                        break;
                                    case "Joypurhat(জয়পুরহাট)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Joypurhat, R.layout.spinner_layout);
                                        break;
                                    case "Jamalpur(জামালপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Jamalpur, R.layout.spinner_layout);
                                        break;
                                    case "Jessore(যশোর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Jessore, R.layout.spinner_layout);
                                        break;
                                    case "Kurigram(কুড়িগ্রাম)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Kurigram, R.layout.spinner_layout);
                                        break;
                                    case "Kushtia(কুষ্টিয়া)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Kushtia, R.layout.spinner_layout);
                                        break;
                                    case "Lakshmipur(লক্ষ্মীপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Lakshmipur, R.layout.spinner_layout);
                                        break;
                                    case "Jhalakathi(ঝালকাঠী)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Jhalokati, R.layout.spinner_layout);
                                        break;
                                    case "Jhinaidah(ঝিনাইদাহ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Jhenaidah, R.layout.spinner_layout);
                                        break;
                                    case "Khagrachari(খাগড়াছড়ি)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Khagrachhari, R.layout.spinner_layout);
                                        break;
                                    case "Khulna(খুলনা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Khulna, R.layout.spinner_layout);
                                        break;
                                    case "Kishoreganj(কিশোরগঞ্জ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Kishoreganj, R.layout.spinner_layout);
                                        break;
                                    case "Lalmonirhat(লালমনিরহাট)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Lalmonirhat, R.layout.spinner_layout);
                                        break;
                                    case "Madaripur(মাদারীপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Madaripur, R.layout.spinner_layout);
                                        break;
                                    case "Magura(মাগুরা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Magura, R.layout.spinner_layout);
                                        break;
                                    case "Manikganj(মানিকগঞ্জ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Manikganj, R.layout.spinner_layout);
                                        break;
                                    case "Meherpur(মেহেরপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Meherpur, R.layout.spinner_layout);
                                        break;
                                    case "Moulavibazar(মৌলভীবাজার)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Moulvibazar, R.layout.spinner_layout);
                                        break;
                                    case "Munshiganj(মুন্সীগঞ্জ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Munshiganj, R.layout.spinner_layout);
                                        break;
                                    case "Mymensingh(ময়মনসিংহ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Mymensingh, R.layout.spinner_layout);
                                        break;
                                    case "Naogaon(নওগাঁ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Naogaon, R.layout.spinner_layout);
                                        break;
                                    case "Narayanganj(নারায়ণগঞ্জ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Narayanganj, R.layout.spinner_layout);
                                        break;
                                    case "Narsingdi(নরসিংদী)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Narsingdi, R.layout.spinner_layout);
                                        break;
                                    case "Natore(নাটোর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Natore, R.layout.spinner_layout);
                                        break;
                                    case "Chapainawabganj(চাঁপাইনবাবগঞ্জ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Chapainawabganj, R.layout.spinner_layout);
                                        break;
                                    case "Netrokona(নেত্রকোনা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Netrokona, R.layout.spinner_layout);
                                        break;
                                    case "Nilphamari(নীলফামারী)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Nilphamari, R.layout.spinner_layout);
                                        break;
                                    case "Noakhali(নোয়াখালী)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Noakhali, R.layout.spinner_layout);
                                        break;
                                    case "Norail(নড়াইল)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Narail, R.layout.spinner_layout);
                                        break;
                                    case "Pabna(পাবনা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Pabna, R.layout.spinner_layout);
                                        break;
                                    case "Panchagarh(পঞ্চগড়)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Panchagarh, R.layout.spinner_layout);
                                        break;
                                    case "Patuakhali(পটুয়াখালী)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Patuakhali, R.layout.spinner_layout);
                                        break;
                                    case "Pirojpur(পিরোজপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Pirojpur, R.layout.spinner_layout);
                                        break;
                                    case "Rajbari(রাজবাড়ী)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Rajbari, R.layout.spinner_layout);
                                        break;
                                    case "Rajshahi(রাজশাহী)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Rajshahi, R.layout.spinner_layout);
                                        break;
                                    case "Rangamati(রাঙ্গামাটি)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Rangamati, R.layout.spinner_layout);
                                        break;
                                    case "Rangpur(রংপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Rangpur, R.layout.spinner_layout);
                                        break;
                                    case "Shariyatpur(শরীয়তপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Shariatpur, R.layout.spinner_layout);
                                        break;
                                    case "Satkhira(সাতক্ষীরা)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Satkhira, R.layout.spinner_layout);
                                        break;
                                    case "Sherpur(শেরপুর)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Sherpur, R.layout.spinner_layout);
                                        break;
                                    case "Sirajganj(সিরাজগঞ্জ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Sirajganj, R.layout.spinner_layout);
                                        break;
                                    case "Sunamganj(সুনামগঞ্জ)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Sunamganj, R.layout.spinner_layout);
                                        break;
                                    case "Sylhet(সিলেট)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Sylhet, R.layout.spinner_layout);
                                        break;
                                    case "Thakurgaon(ঠাকুরগাঁও)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Thakurgaon, R.layout.spinner_layout);
                                        break;
                                    case "Tangail(টাঙ্গাইল)":
                                        upzillaAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                , R.array.Tangail, R.layout.spinner_layout);
                                        break;


                                    default:
                                        break;
                                }
                                upzillaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.upzillaSpinner.setAdapter(upzillaAdapter);
                                binding.upzillaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        upzelaSpin = binding.upzillaSpinner.getSelectedItem().toString().trim();
                                        int parentId = parent.getId();
                                        if (parentId == R.id.upzillaSpinner) {

                                            switch (upzelaSpin) {
                                                case "Select Your Upzila":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.defaultunion, R.layout.spinner_layout);
                                                    break;

                                                case "কুমিল্লা সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.cumillasic, R.layout.spinner_layout);
                                                    break;
                                                case "চট্টগ্রাম সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.chittagongsic, R.layout.spinner_layout);
                                                    break;
                                                case "রাজশাহী সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.rajshahingsic, R.layout.spinner_layout);
                                                    break;
                                                case "খুলনা সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.khulnasic, R.layout.spinner_layout);
                                                    break;
                                                case "সিলেট সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.sylhetsic, R.layout.spinner_layout);
                                                    break;
                                                case "বরিশাল সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.barishalsic, R.layout.spinner_layout);
                                                    break;
                                                case "রংপুর সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.rangpursic, R.layout.spinner_layout);
                                                    break;
                                                case "ময়মনসিংহ সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.maymanshingsic, R.layout.spinner_layout);
                                                    break;
                                                case "ঢাকা উত্তর সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.dhakaNorthsic, R.layout.spinner_layout);
                                                    break;
                                                case "ঢাকা দক্ষিণ সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.dhakaSouthsic, R.layout.spinner_layout);
                                                    break;
                                                case "গাজীপুর সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.gazisic, R.layout.spinner_layout);
                                                    break;
                                                case "নারায়ণগঞ্জ সিটি কর্পোরেশন":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.naryanganjsic, R.layout.spinner_layout);
                                                    break;
                                                case "Debidwar(দেবিদ্বার)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Debidwar, R.layout.spinner_layout);
                                                    break;
                                                case "Burichang(বুড়িচং)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Burichang, R.layout.spinner_layout);
                                                    break;
                                                case "Barura(বরুড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Barura, R.layout.spinner_layout);
                                                    break;
                                                case "Brahmanpara(ব্রাহ্মণপাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Brahmanpara, R.layout.spinner_layout);
                                                    break;

                                                case "Adarsha Sadar(আদর্শ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Comilla_Adarsh_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Sadar Dakshin(সদর দক্ষিণ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Comilla_Sadar_Dakkhin, R.layout.spinner_layout);
                                                    break;
                                                case "Chandina(চান্দিনা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chandina, R.layout.spinner_layout);
                                                    break;
                                                case "Chauddagram(চৌদ্দগ্রাম)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chauddagram, R.layout.spinner_layout);
                                                    break;
                                                case "Daudkandi(দাউদকান্দি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Daudkandi, R.layout.spinner_layout);
                                                    break;
                                                case "Homna(হোমনা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Homna, R.layout.spinner_layout);
                                                    break;
                                                case "Lalmai(লালমাই)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Lalmai, R.layout.spinner_layout);
                                                    break;

                                                case "Laksam(লাকসাম)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Laksam, R.layout.spinner_layout);
                                                    break;
                                                case "Muradnagar(মুরাদনগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Muradnagar, R.layout.spinner_layout);
                                                    break;
                                                case "Nangalkot(নাঙ্গলকোট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nangalkot, R.layout.spinner_layout);
                                                    break;
                                                case "Meghna(মেঘনা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Meghna, R.layout.spinner_layout);
                                                    break;
                                                case "Manoharganj(মনোহরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Manoharganj, R.layout.spinner_layout);
                                                    break;

                                                case "Titas(তিতাস)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Titas, R.layout.spinner_layout);
                                                    break;


                                                case "Chagalnaiya(ছাগলনাইয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chagalnaiya, R.layout.spinner_layout);
                                                    break;
                                                case "Feni Sadar(ফেনী সদর) ":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Feni_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Sonagazi(সোনাগাজী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sonagazi, R.layout.spinner_layout);
                                                    break;
                                                case "Fulgazi(ফুলগাজী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Fulgazi, R.layout.spinner_layout);
                                                    break;
                                                case "Parshuram(পরশুরাম)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Parashuram, R.layout.spinner_layout);
                                                    break;
                                                case "Daganbhuna(দাগনভূঞা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Daganbhuiyan, R.layout.spinner_layout);
                                                    break;

                                                case "Brahmanbaria Sadar(ব্রাহ্মণবাড়িয়া সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Brahmanbaria_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Kasba(কসবা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kasba, R.layout.spinner_layout);
                                                    break;
                                                case "Nasirnagar(নাসিরনগর) ":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nasirnagar, R.layout.spinner_layout);
                                                    break;
                                                case "Sarail (সরাইল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sarail, R.layout.spinner_layout);
                                                    break;
                                                case "Akhaura(আখাউড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Akhaura, R.layout.spinner_layout);
                                                    break;
                                                case "Ashuganj(আশুগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ashuganj, R.layout.spinner_layout);
                                                    break;
                                                case "Nabinagar(নবীনগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nabinagar, R.layout.spinner_layout);
                                                    break;
                                                case "Bancharampur(বাঞ্ছারামপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bancharampur, R.layout.spinner_layout);
                                                    break;
                                                case "Vijaynagar(বিজয়নগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Vijaynagar, R.layout.spinner_layout);
                                                    break;
                                                case "Rangamati Sadar(রাঙ্গামাটি সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rangamati_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Kaptai(কাপ্তাই)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaptai, R.layout.spinner_layout);
                                                    break;
                                                case "Kaukhali(কাউখালী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaukhali, R.layout.spinner_layout);
                                                    break;
                                                case "Baghaichhari(বাঘাইছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Baghaichhari, R.layout.spinner_layout);
                                                    break;
                                                case "Barkal(বরকল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Barkal, R.layout.spinner_layout);
                                                    break;
                                                case " Langadu(লংগদু)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Langadu, R.layout.spinner_layout);
                                                    break;
                                                case "Rajsthali(রাজস্থলী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rajsthali, R.layout.spinner_layout);
                                                    break;
                                                case "Bilaichhari(বিলাইছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bilaichhari, R.layout.spinner_layout);
                                                    break;
                                                case "Jurachhari(জুরাছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jurachhari, R.layout.spinner_layout);
                                                    break;
                                                case "Naniyarchar(নানিয়ারচর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Naniyarchar, R.layout.spinner_layout);
                                                    break;
                                                case "Noakhali Sadar(নোয়াখালী সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Noakhali_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Companyganj(কোম্পানীগঞ্জ) ":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Companiganj_noakhali, R.layout.spinner_layout);
                                                    break;
                                                case "Begumganj(বেগমগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Begumganj, R.layout.spinner_layout);
                                                    break;
                                                case "Hatia(হাতিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Hatia, R.layout.spinner_layout);
                                                    break;
                                                case "Subarnachar(সুবর্ণচর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Subarnachar, R.layout.spinner_layout);
                                                    break;
                                                case "Kabirhat(কবিরহাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kabirhat, R.layout.spinner_layout);
                                                    break;
                                                case "Senbagh(সেনবাগ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Senbagh, R.layout.spinner_layout);
                                                    break;
                                                case "Chatkhil(চাটখিল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chatkhil, R.layout.spinner_layout);
                                                    break;
                                                case "Sonaimuri(সোনাইমুড়ী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sonaimuri, R.layout.spinner_layout);
                                                    break;
                                                case "Haimchar(হাইমচর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Haimchar, R.layout.spinner_layout);
                                                    break;
                                                case "Kachua(কচুয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kachua_chandpur, R.layout.spinner_layout);
                                                    break;
                                                case "Shahrasti(শাহরাস্তি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shahrasti, R.layout.spinner_layout);
                                                    break;
                                                case "Chandpur Sadar(চাঁদপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chandpur_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Matlab North(মতলব উত্তর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Matlab_north, R.layout.spinner_layout);
                                                    break;
                                                case "Matlab South(মতলব দক্ষিণ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Matlab_South, R.layout.spinner_layout);
                                                    break;
                                                case "Hajiganj(হাজীগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Hajiganj, R.layout.spinner_layout);
                                                    break;
                                                case "Faridganj(ফরিদগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Faridganj, R.layout.spinner_layout);
                                                    break;

                                                case "Lakshmipur Sadar(লক্ষ্মীপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Lakshmipur_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Kamalnagar(কমলনগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kamalnagar, R.layout.spinner_layout);
                                                    break;
                                                case "Raipur(রায়পুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Raipur_Lakshmipur, R.layout.spinner_layout);
                                                    break;
                                                case "Ramgati(রামগতি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ramgati, R.layout.spinner_layout);
                                                    break;
                                                case "Ramganj(রামগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ramganj, R.layout.spinner_layout);
                                                    break;

                                                case "Rangunia(রাঙ্গুনিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rangunia, R.layout.spinner_layout);
                                                    break;
                                                case "Sitakunda(সীতাকুন্ড)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sitakunda, R.layout.spinner_layout);
                                                    break;
                                                case "Mirsarai(মীরসরাই)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mirsarai, R.layout.spinner_layout);
                                                    break;
                                                case "Patia(পটিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Patia, R.layout.spinner_layout);
                                                    break;
                                                case "Sandwip(সন্দ্বীপ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sandwip, R.layout.spinner_layout);
                                                    break;
                                                case "Bashkhali(বাঁশখালী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bashkhali, R.layout.spinner_layout);
                                                    break;
                                                case "Boalkhali(বোয়ালখালী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Boalkhali, R.layout.spinner_layout);
                                                    break;
                                                case "Anwara(আনোয়ারা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Anwara, R.layout.spinner_layout);
                                                    break;
                                                case "Chandnaish(চন্দনাইশ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chandnaish, R.layout.spinner_layout);
                                                    break;
                                                case "Satkania(সাতকানিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Satkania, R.layout.spinner_layout);
                                                    break;
                                                case "Lohagara(লোহাগাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Lohagara, R.layout.spinner_layout);
                                                    break;
                                                case "Hathazari(হাটহাজারী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Hathazari, R.layout.spinner_layout);
                                                    break;
                                                case "Fatikchhari(ফটিকছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Fatikchhari, R.layout.spinner_layout);
                                                    break;
                                                case "Raozan(রাউজান)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Raozan, R.layout.spinner_layout);
                                                    break;
                                                case "Karnafuli(কর্ণফুলী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Karnafuli, R.layout.spinner_layout);
                                                    break;
                                                case "Cox\'s Bazar Sadar(কক্সবাজার সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.CoxBazar_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Chakaria(চকরিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chakaria, R.layout.spinner_layout);
                                                    break;
                                                case "Kutubdia(কুতুবদিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kutubdia, R.layout.spinner_layout);
                                                    break;
                                                case "Ukhia(উখিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ukhia, R.layout.spinner_layout);
                                                    break;
                                                case "Maheshkhali(মহেশখালী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Maheshkhali, R.layout.spinner_layout);
                                                    break;
                                                case "Pekua(পেকুয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Pekua, R.layout.spinner_layout);
                                                    break;
                                                case "Ramu(রামু)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ramu, R.layout.spinner_layout);
                                                    break;
                                                case "Teknaf(টেকনাফ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Teknaf, R.layout.spinner_layout);
                                                    break;
                                                case "Khagrachhari Sadar(খাগড়াছড়ি সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Khagrachhari_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Dighinala(দিঘীনালা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dighinala, R.layout.spinner_layout);
                                                    break;
                                                case "Panchhari(পানছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Panchhari, R.layout.spinner_layout);
                                                    break;
                                                case "Lakkhichhari(লক্ষীছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Laxmichari, R.layout.spinner_layout);
                                                    break;
                                                case "Mahalchhari(মহালছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mahalchhari, R.layout.spinner_layout);
                                                    break;
                                                case "Manikchhari(মানিকছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Manikchhari, R.layout.spinner_layout);
                                                    break;
                                                case "Ramgarh(রামগড়)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ramgarh, R.layout.spinner_layout);
                                                    break;
                                                case "Matiranga(মাটিরাঙ্গা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Matiranga, R.layout.spinner_layout);
                                                    break;
                                                case "Guimara(গুইমারা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Guimara, R.layout.spinner_layout);
                                                    break;

                                                case "Bandarban Sadar(বান্দরবান সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bandarban_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Alikadam(আলীকদম)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Alikadam, R.layout.spinner_layout);
                                                    break;
                                                case "Naikhyangchhari(নাইক্ষ্যংছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Naikhyangchhari, R.layout.spinner_layout);
                                                    break;
                                                case "Roangchhari(রোয়াংছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Roangchhari, R.layout.spinner_layout);
                                                    break;
                                                case "Lama(লামা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Lama, R.layout.spinner_layout);
                                                    break;
                                                case "Ruma(রুমা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ruma, R.layout.spinner_layout);
                                                    break;
                                                case "Thanchi(থানচি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Thanchi, R.layout.spinner_layout);
                                                    break;
                                                case "Belkuchi(বেলকুচি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Belkuchi, R.layout.spinner_layout);
                                                    break;
                                                case "Chowhali(চৌহালি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chowhali, R.layout.spinner_layout);
                                                    break;
                                                case "Kamarkhand(কামারখন্দ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kamarkhand, R.layout.spinner_layout);
                                                    break;
                                                case "Kazipur(কাজীপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kazipur, R.layout.spinner_layout);
                                                    break;
                                                case "Raiganj(রায়গঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Raiganj, R.layout.spinner_layout);
                                                    break;
                                                case "Shahjadpur(শাহজাদপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shahjadpur, R.layout.spinner_layout);
                                                    break;
                                                case "Sirajganj Sadar(সিরাজগঞ্জ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sirajganj_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Tarash(তাড়াশ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tarash, R.layout.spinner_layout);
                                                    break;

                                                case "Ullapara(উল্লাপাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ullapara, R.layout.spinner_layout);
                                                    break;
                                                case "Sujanagar(সুজানগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sujanagar, R.layout.spinner_layout);
                                                    break;
                                                case "Ishwardi(ঈশ্বরদী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ishwardi, R.layout.spinner_layout);
                                                    break;
                                                case "Bhangura(ভাঙ্গুড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bhangura, R.layout.spinner_layout);
                                                    break;
                                                case "Atgharia(আটঘরিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Atgharia, R.layout.spinner_layout);
                                                    break;
                                                case "Bera(বেড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bera, R.layout.spinner_layout);
                                                    break;
                                                case "Pabna Sadar(পাবনা সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Pabna_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Chatmohar(চাটমোহর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chatmohar, R.layout.spinner_layout);
                                                    break;
                                                case "Sathia(সাঁথিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sathia, R.layout.spinner_layout);
                                                    break;
                                                case "Faridpur(ফরিদপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Faridpur_pabna, R.layout.spinner_layout);
                                                    break;

                                                case "Panchagarh Sadar(পঞ্চগড় সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Panchagarh_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Debiganj(দেবীগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Debiganj, R.layout.spinner_layout);
                                                    break;
                                                case "Boda(বোদা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Boda, R.layout.spinner_layout);
                                                    break;
                                                case "Atwari(আটোয়ারী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Atwari, R.layout.spinner_layout);
                                                    break;
                                                case "Tetulia(তেতুলিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tetulia, R.layout.spinner_layout);
                                                    break;
                                                case "Nawabganj(নবাবগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nawabganj, R.layout.spinner_layout);
                                                    break;
                                                case "Birganj(বীরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Birganj, R.layout.spinner_layout);
                                                    break;
                                                case "Ghoraghat(ঘোড়াঘাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ghoraghat, R.layout.spinner_layout);
                                                    break;
                                                case "Birampur(বিরামপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Birampur, R.layout.spinner_layout);
                                                    break;

                                                case "Parbatipur(পার্বতীপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Parbatipur, R.layout.spinner_layout);
                                                    break;

                                                case "Bochaganj(বোচাগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bochaganj, R.layout.spinner_layout);
                                                    break;

                                                case "Kaharol(কাহারোল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaharol, R.layout.spinner_layout);
                                                    break;

                                                case "Fulbari(ফুলবাড়ী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Fulbari_dinajpur, R.layout.spinner_layout);
                                                    break;

                                                case "Dinajpur Sadar(দিনাজপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dinajpur_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Hakimpur(হাকিমপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Hakimpur, R.layout.spinner_layout);
                                                    break;

                                                case "Khansama(খানসামা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Khansama, R.layout.spinner_layout);
                                                    break;

                                                case "Biral(বিরল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Biral, R.layout.spinner_layout);
                                                    break;

                                                case "Chirirbandar(চিরিরবন্দর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chirirbandar, R.layout.spinner_layout);
                                                    break;

                                                case "Lalmonirhat Sadar(লালমনিরহাট সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Lalmonirhat_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Kaliganj(কালীগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaliganj_lalmonirhat, R.layout.spinner_layout);
                                                    break;

                                                case "Hatibandha(হাতীবান্ধা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Hatibandha, R.layout.spinner_layout);
                                                    break;

                                                case "Patgram(পাটগ্রাম)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Patgram, R.layout.spinner_layout);
                                                    break;

                                                case "Aditmari(আদিতমারী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Aditmari, R.layout.spinner_layout);
                                                    break;
                                                case "Nilphamari Sadar(নীলফামারী সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nilphamari_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Syedpur(সৈয়দপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Syedpur, R.layout.spinner_layout);
                                                    break;
                                                case "Domar(ডোমার)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Domar, R.layout.spinner_layout);
                                                    break;
                                                case "Dimla(ডিমলা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dimla, R.layout.spinner_layout);
                                                    break;
                                                case "Jaldhaka(জলঢাকা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jaldhaka, R.layout.spinner_layout);
                                                    break;

                                                case "Kishoreganj(কিশোরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kishoreganj_nilfamari, R.layout.spinner_layout);
                                                    break;

                                                case "Kahalu(কাহালু)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kahalu, R.layout.spinner_layout);
                                                    break;

                                                case "Bogra Sadar(বগুড়া সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bogra_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Sariakandi(সারিয়াকান্দি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sariakandi, R.layout.spinner_layout);
                                                    break;

                                                case "Shajahanpur(শাজাহানপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shajahanpur, R.layout.spinner_layout);
                                                    break;

                                                case "Dupchachinya(দুপচাচিঁয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dupchanchia, R.layout.spinner_layout);
                                                    break;

                                                case "Adamdighi (আদমদিঘি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Adamdighi, R.layout.spinner_layout);
                                                    break;

                                                case "Nandigram(নন্দিগ্রাম)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nandigram, R.layout.spinner_layout);
                                                    break;

                                                case "Sonatala(সোনাতলা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sonatala, R.layout.spinner_layout);
                                                    break;

                                                case "Dhunat(ধুনট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dhunat, R.layout.spinner_layout);
                                                    break;

                                                case "Gabtali(গাবতলী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gabtali, R.layout.spinner_layout);
                                                    break;

                                                case "Sherpur(শেরপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sherpur_bogra, R.layout.spinner_layout);
                                                    break;

                                                case "Shibganj(শিবগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shibganj_bogra, R.layout.spinner_layout);
                                                    break;

                                                case "Paba(পবা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Paba, R.layout.spinner_layout);
                                                    break;

                                                case "Durgapur(দুর্গাপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Durgapur, R.layout.spinner_layout);
                                                    break;

                                                case "Mohanpur(মোহনপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mohanpur, R.layout.spinner_layout);
                                                    break;

                                                case "Charghat(চারঘাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Charghat, R.layout.spinner_layout);
                                                    break;

                                                case "Puthia(পুঠিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Puthia, R.layout.spinner_layout);
                                                    break;

                                                case "Bagha(বাঘা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bagha, R.layout.spinner_layout);
                                                    break;

                                                case "Godagari(গোদাগাড়ী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Godagari, R.layout.spinner_layout);
                                                    break;

                                                case "Tanore(তানোর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tanore, R.layout.spinner_layout);
                                                    break;

                                                case "Bagmara(বাগমারা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bagmara, R.layout.spinner_layout);
                                                    break;

                                                case "Natore Sadar(নাটোর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Natore_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Singra(সিংড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Singra, R.layout.spinner_layout);
                                                    break;
                                                case "Baraigram(বড়াইগ্রাম)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Baraigram, R.layout.spinner_layout);
                                                    break;
                                                case "Bagatipara(বাগাতিপাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bagatipara, R.layout.spinner_layout);
                                                    break;
                                                case "Lalpur(লালপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Lalpur, R.layout.spinner_layout);
                                                    break;
                                                case "Gurudaspur(গুরুদাসপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gurudaspur, R.layout.spinner_layout);
                                                    break;
                                                case "Naldanga(নলডাঙ্গা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Naldanga, R.layout.spinner_layout);
                                                    break;

                                                case "Akkelpur(আক্কেলপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Akkelpur, R.layout.spinner_layout);
                                                    break;
                                                case "Kalai(কালাই)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kalai, R.layout.spinner_layout);
                                                    break;
                                                case "Khetlal(ক্ষেতলাল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Khetlal, R.layout.spinner_layout);
                                                    break;
                                                case "Pachbibi(পাঁচবিবি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Panchbibi, R.layout.spinner_layout);
                                                    break;
                                                case "Joypurhat Sadar(জয়পুরহাট সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Joypurhat_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Chapainawabganj Sadar(চাঁপাইনবাবগঞ্জ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chapainawabganj_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Gomstapur(গোমস্তাপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gomstapur, R.layout.spinner_layout);
                                                    break;
                                                case "Nachol(নাচোল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nachol, R.layout.spinner_layout);
                                                    break;
                                                case "Bholahat(ভোলাহাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bholahat, R.layout.spinner_layout);
                                                    break;
                                                case "Shibganj (শিবগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shibganj_chapai, R.layout.spinner_layout);
                                                    break;

                                                case "Mahadevpur(মহাদেবপুর) ":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mahadevpur, R.layout.spinner_layout);
                                                    break;
                                                case "Badalgachhi(বদলগাছী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Badalgachhi, R.layout.spinner_layout);
                                                    break;
                                                case "Patnitla(পত্নিতলা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Patnitala, R.layout.spinner_layout);
                                                    break;
                                                case "Dhamairhat(ধামইরহাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dhamairhat, R.layout.spinner_layout);
                                                    break;
                                                case "Niamatpur(নিয়ামতপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Niamatpur, R.layout.spinner_layout);
                                                    break;
                                                case "Manda(মান্দা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Manda, R.layout.spinner_layout);
                                                    break;
                                                case "Atrai(আত্রাই)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Atrai, R.layout.spinner_layout);
                                                    break;
                                                case "Raninagar(রাণীনগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Raninagar, R.layout.spinner_layout);
                                                    break;
                                                case "Naogaon Sadar(নওগাঁ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Naogaon_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Porsha(পোরশা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Porsha, R.layout.spinner_layout);
                                                    break;
                                                case "Sapahar(সাপাহার)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sapahar, R.layout.spinner_layout);
                                                    break;
                                                case "Manirampur(মণিরামপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Monirampur, R.layout.spinner_layout);
                                                    break;
                                                case "Abhaynagar(অভয়নগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Abhaynagar, R.layout.spinner_layout);
                                                    break;
                                                case "Bagharpara(বাঘারপাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bagherpara, R.layout.spinner_layout);
                                                    break;
                                                case "Chougachha(চৌগাছা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chougachha, R.layout.spinner_layout);
                                                    break;
                                                case "Jhikargachha(ঝিকরগাছা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jhikargachha, R.layout.spinner_layout);
                                                    break;
                                                case "Keshabpur(কেশবপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Keshabpur, R.layout.spinner_layout);
                                                    break;
                                                case "Jessore Sadar(যশোর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jessore_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Sharsha(শার্শা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sharsha, R.layout.spinner_layout);
                                                    break;
                                                case "Asashuni(আশাশুনি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Asashuni, R.layout.spinner_layout);
                                                    break;
                                                case "Debhata(দেবহাটা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Debhata, R.layout.spinner_layout);
                                                    break;
                                                case "Kalaroa(কলারোয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kalaroa, R.layout.spinner_layout);
                                                    break;
                                                case "Satkhira Sadar(সাতক্ষীরা সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Satkhira_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Shyamnagar(শ্যামনগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shyamnagar, R.layout.spinner_layout);
                                                    break;
                                                case "Tala(তালা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tala, R.layout.spinner_layout);
                                                    break;
                                                case "Kaliganj(কালিগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaliganj_satkhira, R.layout.spinner_layout);
                                                    break;

                                                case "Mujibnagar(মুজিবনগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mujibnagar, R.layout.spinner_layout);
                                                    break;

                                                case "Meherpur Sadar(মেহেরপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Meherpur_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Gangni(গাংনী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gangni, R.layout.spinner_layout);
                                                    break;
                                                case "Narail Sadar(নড়াইল সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Narail_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Lohagara(লোহাগড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Lohagarh, R.layout.spinner_layout);
                                                    break;
                                                case "Kalia(কালিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kalia, R.layout.spinner_layout);
                                                    break;
                                                case "Chuadanga Sadar(চুয়াডাঙ্গা সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chuadanga_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Alamdanga(আলমডাঙ্গা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Alamdanga, R.layout.spinner_layout);
                                                    break;
                                                case "Damurhuda(দামুড়হুদা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Damurhuda, R.layout.spinner_layout);
                                                    break;
                                                case "Jivannagar(জীবননগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jivannagar, R.layout.spinner_layout);
                                                    break;
                                                case "Kushtia Sadar(কুষ্টিয়া সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kustia_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Kumarkhali(কুমারখালী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kumarkhali, R.layout.spinner_layout);
                                                    break;
                                                case "Khoksa(খোকসা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Khoksa, R.layout.spinner_layout);
                                                    break;
                                                case "Mirpur(মিরপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mirpur, R.layout.spinner_layout);
                                                    break;
                                                case "Daulatpur(দৌলতপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Daulatpur_kustia, R.layout.spinner_layout);
                                                    break;
                                                case "Bheramara(ভেড়ামারা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bheramara, R.layout.spinner_layout);
                                                    break;

                                                case "Shalikha(শালিখা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shalikha, R.layout.spinner_layout);
                                                    break;
                                                case "Sreepur(শ্রীপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sreepur_magura, R.layout.spinner_layout);
                                                    break;
                                                case "Magura Sadar(মাগুরা সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Magura_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Mohammadpur(মহম্মদপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mohammadpur, R.layout.spinner_layout);
                                                    break;
                                                case "Paikgachha(পাইকগাছা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Paikgachha, R.layout.spinner_layout);
                                                    break;
                                                case "Fultala(ফুলতলা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Fultala, R.layout.spinner_layout);
                                                    break;
                                                case "Dighlia(দিঘলিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dighalia, R.layout.spinner_layout);
                                                    break;
                                                case "Rupsha(রূপসা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rupsha, R.layout.spinner_layout);
                                                    break;
                                                case "Terkhada(তেরখাদা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Terkhada, R.layout.spinner_layout);
                                                    break;
                                                case "Dumuria(ডুমুরিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dumuria, R.layout.spinner_layout);
                                                    break;
                                                case "Batiaghata(বটিয়াঘাটা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Batiaghata, R.layout.spinner_layout);
                                                    break;
                                                case "Dakop(দাকোপ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dakop, R.layout.spinner_layout);
                                                    break;
                                                case "Koyra(কয়রা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Koyra, R.layout.spinner_layout);
                                                    break;
                                                case "Fakirhat(ফকিরহাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Fakirhat, R.layout.spinner_layout);
                                                    break;
                                                case "Bagerhat Sadar(বাগেরহাট সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bagerhat_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Mollahat(মোল্লাহাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mollahat, R.layout.spinner_layout);
                                                    break;
                                                case "Sharankhola(শরণখোলা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sharankhola, R.layout.spinner_layout);
                                                    break;
                                                case "Rampal(রামপাল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rampal, R.layout.spinner_layout);
                                                    break;
                                                case "Morelganj(মোড়েলগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Morelganj, R.layout.spinner_layout);
                                                    break;
                                                case "Kachua(কচুয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kachua, R.layout.spinner_layout);
                                                    break;
                                                case "Mongla(মোংলা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mongla, R.layout.spinner_layout);
                                                    break;


                                                case "Chitalmari(চিতলমারী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chitalmari, R.layout.spinner_layout);
                                                    break;

                                                case "Jhenaidah Sadar(ঝিনাইদহ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jhenaidah_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Shailkupa(শৈলকুপা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shailkupa, R.layout.spinner_layout);
                                                    break;

                                                case "Harinakundu(হরিণাকুন্ডু)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Harinakundu, R.layout.spinner_layout);
                                                    break;

                                                case "Kaliganj (কালীগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaliganj_jhenaidah, R.layout.spinner_layout);
                                                    break;

                                                case "Kotchandpur(কোটচাঁদপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kotchandpur, R.layout.spinner_layout);
                                                    break;

                                                case "Maheshpur(মহেশপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Maheshpur, R.layout.spinner_layout);
                                                    break;

                                                case "Jhalokati Sadar(ঝালকাঠি সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jhalokati_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Kathalia(কাঠালিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kathalia, R.layout.spinner_layout);
                                                    break;

                                                case "Nalchiti(নলছিটি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nalchiti, R.layout.spinner_layout);
                                                    break;

                                                case "Rajapur(রাজাপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rajapur, R.layout.spinner_layout);
                                                    break;

                                                case "Baufal(বাউফল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Baufal, R.layout.spinner_layout);
                                                    break;

                                                case "Patuakhali Sadar(পটুয়াখালী সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Patuakhali_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Dumki(দুমকি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dumki, R.layout.spinner_layout);
                                                    break;

                                                case "Dashmina(দশমিনা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dashmina, R.layout.spinner_layout);
                                                    break;

                                                case "Kalapara(কলাপাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kalapara, R.layout.spinner_layout);
                                                    break;

                                                case "Mirzaganj(মির্জাগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mirzaganj, R.layout.spinner_layout);
                                                    break;

                                                case "Galachipa(গলাচিপা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Galachipa, R.layout.spinner_layout);
                                                    break;

                                                case "Rangabali(রাঙ্গাবালী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rangabali, R.layout.spinner_layout);
                                                    break;


                                                case "Pirojpur Sadar(পিরোজপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Pirojpur_Sadar, R.layout.spinner_layout);
                                                    break;


                                                case "Nazirpur(নাজিরপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nazirpur, R.layout.spinner_layout);
                                                    break;


                                                case "Kaukhali (কাউখালী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaukhali_pirojpur, R.layout.spinner_layout);
                                                    break;


                                                case "Vandaria(ভান্ডারিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bhandaria, R.layout.spinner_layout);
                                                    break;


                                                case "Mathbaria(মঠবাড়ীয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mathbaria, R.layout.spinner_layout);
                                                    break;


                                                case "Nesarabad(নেছারাবাদ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nesharabad, R.layout.spinner_layout);
                                                    break;


                                                case "Indurkani(ইন্দুরকানী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Indurkani, R.layout.spinner_layout);
                                                    break;


                                                case "Barisal Sadar(বরিশাল সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Barisal_Sadar, R.layout.spinner_layout);
                                                    break;


                                                case "Bakerganj(বাকেরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bakerganj, R.layout.spinner_layout);
                                                    break;


                                                case "Babuganj(বাবুগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Babuganj, R.layout.spinner_layout);
                                                    break;


                                                case "Wazirpur(উজিরপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Wazirpur, R.layout.spinner_layout);
                                                    break;


                                                case "Banaripara(বানারীপাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Banaripara, R.layout.spinner_layout);
                                                    break;


                                                case "Gournadi(গৌরনদী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gournadi, R.layout.spinner_layout);
                                                    break;


                                                case "Agailjhara(আগৈলঝাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Agailjhara, R.layout.spinner_layout);
                                                    break;


                                                case "Mehendiganj(মেহেন্দিগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mehendiganj, R.layout.spinner_layout);
                                                    break;


                                                case "Muladi(মুলাদী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rangabali, R.layout.spinner_layout);
                                                    break;


                                                case "Hijla(হিজলা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Hijla, R.layout.spinner_layout);
                                                    break;


                                                case "Vola Sadar(ভোলা সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bhola_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "Borhan Uddin(বোরহান উদ্দিন)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Borhanuddin, R.layout.spinner_layout);
                                                    break;


                                                case "Char Fashion(চরফ্যাশন)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Char_Fashion, R.layout.spinner_layout);
                                                    break;


                                                case "Daulatkhan(দৌলতখান)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Daulatkhan, R.layout.spinner_layout);
                                                    break;


                                                case "Monpura(মনপুরা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Monpura, R.layout.spinner_layout);
                                                    break;


                                                case "Tajumuddin(তজুমদ্দিন)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tajumuddin, R.layout.spinner_layout);
                                                    break;


                                                case "Lalmohan(লালমোহন)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Lalmohan, R.layout.spinner_layout);
                                                    break;


                                                case "Amtali(আমতলী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Amtali, R.layout.spinner_layout);
                                                    break;
                                                case "Barguna Sadar(বরগুনা সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Barguna_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Betagi(বেতাগী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Betagi, R.layout.spinner_layout);
                                                    break;
                                                case "Bamna(বামনা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bamna, R.layout.spinner_layout);
                                                    break;
                                                case "Patharghata(পাথরঘাটা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Patharghata, R.layout.spinner_layout);
                                                    break;
                                                case "Taltali(তালতলি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Taltoli, R.layout.spinner_layout);
                                                    break;

                                                case "Balaganj(বালাগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Balaganj, R.layout.spinner_layout);
                                                    break;
                                                case "Beanibazar(বিয়ানীবাজার)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Beanibazar, R.layout.spinner_layout);
                                                    break;
                                                case "Bishwanath(বিশ্বনাথ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Taltoli, R.layout.spinner_layout);
                                                    break;
                                                case "Companyganj(কোম্পানীগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bishwanath, R.layout.spinner_layout);
                                                    break;
                                                case "Fenchuganj(ফেঞ্চুগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Fenchuganj, R.layout.spinner_layout);
                                                    break;
                                                case "Golapganj(গোলাপগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Golapganj, R.layout.spinner_layout);
                                                    break;
                                                case "Gowainghat(গোয়াইনঘাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Goainghat, R.layout.spinner_layout);
                                                    break;
                                                case "Jaintapur(জৈন্তাপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jaintapur, R.layout.spinner_layout);
                                                    break;
                                                case "Kanaighat(কানাইঘাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kanaighat, R.layout.spinner_layout);
                                                    break;
                                                case "Sylhet Sadar(সিলেট সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sylhet_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Zakiganj(জকিগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Zakiganj, R.layout.spinner_layout);
                                                    break;
                                                case "South Surma(দক্ষিণ সুরমা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.South_Surma, R.layout.spinner_layout);
                                                    break;
                                                case "Osmaninagar(ওসমানীনগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Osmaninagar, R.layout.spinner_layout);
                                                    break;

                                                case "Baralekha(বড়লেখা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Baralekha, R.layout.spinner_layout);
                                                    break;
                                                case "Kamalganj(কমলগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kamalganj, R.layout.spinner_layout);
                                                    break;
                                                case "Kulaura(কুলাউড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kulaura, R.layout.spinner_layout);
                                                    break;
                                                case "Moulvibazar Sadar(মৌলভীবাজার সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Moulvibazar_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Rajnagar(রাজনগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rajnagar, R.layout.spinner_layout);
                                                    break;
                                                case "Srimangal(শ্রীমঙ্গল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Srimangal, R.layout.spinner_layout);
                                                    break;
                                                case "Juri(জুড়ী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Juri, R.layout.spinner_layout);
                                                    break;
                                                case "Nabiganj(নবীগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nabiganj, R.layout.spinner_layout);
                                                    break;
                                                case "Bahubal(বাহুবল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bahubal, R.layout.spinner_layout);
                                                    break;
                                                case "Ajmiriganj(আজমিরীগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ajmiriganj, R.layout.spinner_layout);
                                                    break;
                                                case "Baniachang(বানিয়াচং)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Baniachang, R.layout.spinner_layout);
                                                    break;
                                                case "Lakhai(লাখাই)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Lakhai, R.layout.spinner_layout);
                                                    break;
                                                case "Chunarughat(চুনারুঘাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chunarughat, R.layout.spinner_layout);
                                                    break;
                                                case "Habiganj Sadar(হবিগঞ্জ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Habiganj_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Madhabpur(মাধবপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Madhabpur, R.layout.spinner_layout);
                                                    break;
                                                case "Shayestaganj(শায়েস্তাগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shayestaganj, R.layout.spinner_layout);
                                                    break;
                                                case "Sunamganj Sadar(সুনামগঞ্জ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sunamganj_Sadar, R.layout.spinner_layout);
                                                    break;

                                                case "South Sunamganj(দক্ষিণ সুনামগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sunamganj_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Bishwambharpur(বিশ্বম্ভরপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bishwambharpur, R.layout.spinner_layout);
                                                    break;
                                                case "Chhatak(ছাতক)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chhatak, R.layout.spinner_layout);
                                                    break;
                                                case "Jagannathpur(জগন্নাথপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jagannathpur, R.layout.spinner_layout);
                                                    break;
                                                case "Doarabazar(দোয়ারাবাজার)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Doarabazar, R.layout.spinner_layout);
                                                    break;
                                                case "Tahirpur(তাহিরপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tahirpur, R.layout.spinner_layout);
                                                    break;
                                                case "Dharmapasha(ধর্মপাশা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dharmapasha, R.layout.spinner_layout);
                                                    break;
                                                case "Jamalganj(জামালগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jamalganj, R.layout.spinner_layout);
                                                    break;
                                                case "Shalla(শাল্লা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shalla, R.layout.spinner_layout);
                                                    break;
                                                case "Dirai(দিরাই)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dirai, R.layout.spinner_layout);
                                                    break;
                                                case "Belabo(বেলাবো)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Belabo, R.layout.spinner_layout);
                                                    break;
                                                case "Manohardi(মনোহরদী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Manohardi, R.layout.spinner_layout);
                                                    break;
                                                case "Narsingdi Sadar(নরসিংদী সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Narsingdi_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Palash(পলাশ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Palash, R.layout.spinner_layout);
                                                    break;
                                                case "Raipura(রায়পুরা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Raipura, R.layout.spinner_layout);
                                                    break;
                                                case "Shibpur(শিবপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shibpur, R.layout.spinner_layout);
                                                    break;
                                                case "Kaliganj ( কালীগঞ্জ )":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaliganj_gazipur, R.layout.spinner_layout);
                                                    break;
                                                case "Kaliakair(কালিয়াকৈর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaliakair, R.layout.spinner_layout);
                                                    break;
                                                case "Kapasia(কাপাসিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kapasia, R.layout.spinner_layout);
                                                    break;
                                                case "Gazipur Sadar(গাজীপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gazipur_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Shariatpur Sadar(শরিয়তপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shariatpur_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Naria(নড়িয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Naria, R.layout.spinner_layout);
                                                    break;
                                                case "Jajira(জাজিরা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jajira, R.layout.spinner_layout);
                                                    break;
                                                case "Gosairhat(গোসাইরহাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gosairhat, R.layout.spinner_layout);
                                                    break;
                                                case "Vedarganj(ভেদরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shibpur, R.layout.spinner_layout);
                                                    break;
                                                case "Damudya(ডামুড্যা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shibpur, R.layout.spinner_layout);
                                                    break;
                                                case "Araihazar(আড়াইহাজার)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Araihazar, R.layout.spinner_layout);
                                                    break;
                                                case "Bandar(বন্দর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bandar, R.layout.spinner_layout);
                                                    break;
                                                case "Narayanganj Sadar(নারায়নগঞ্জ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Narayanganj_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Rupganj(রূপগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rupganj, R.layout.spinner_layout);
                                                    break;
                                                case "Sonargaon(সোনারগাঁ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sonargaon, R.layout.spinner_layout);
                                                    break;
                                                case "Basail(বাসাইল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Basail, R.layout.spinner_layout);
                                                    break;
                                                case "Bhuapur(ভুয়াপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bhuapur, R.layout.spinner_layout);
                                                    break;
                                                case "Delduar(দেলদুয়ার)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Delduar, R.layout.spinner_layout);
                                                    break;
                                                case "Ghatail(ঘাটাইল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ghatail, R.layout.spinner_layout);
                                                    break;
                                                case "Gopalpur(গোপালপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gopalpur, R.layout.spinner_layout);
                                                    break;
                                                case "Madhupur(মধুপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Madhupur, R.layout.spinner_layout);
                                                    break;
                                                case "Mirzapur(মির্জাপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mirzapur, R.layout.spinner_layout);
                                                    break;
                                                case "Nagarpur(নাগরপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nagarpur, R.layout.spinner_layout);
                                                    break;
                                                case "Sakhipur(সখিপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sakhipur, R.layout.spinner_layout);
                                                    break;
                                                case "Tangail Sadar(টাঙ্গাইল সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tangail_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Kalihati(কালিহাতী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kalihati, R.layout.spinner_layout);
                                                    break;
                                                case "Dhanbari(ধনবাড়ী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dhanbari, R.layout.spinner_layout);
                                                    break;
                                                case "Itna(ইটনা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Itna, R.layout.spinner_layout);
                                                    break;
                                                case "Katiadi(কটিয়াদী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Katiadi, R.layout.spinner_layout);
                                                    break;
                                                case "Bhairab(ভৈরব)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bhairab, R.layout.spinner_layout);
                                                    break;
                                                case "Tarail(তাড়াইল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tarail, R.layout.spinner_layout);
                                                    break;
                                                case "Hoshenpur(হোসেনপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Hoshenpur, R.layout.spinner_layout);
                                                    break;
                                                case "Pakundia(পাকুন্দিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Pakundia, R.layout.spinner_layout);
                                                    break;
                                                case "Kuliyarchar(কুলিয়ারচর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kuliyarchar, R.layout.spinner_layout);
                                                    break;
                                                case "Kishoreganj Sadar(কিশোরগঞ্জ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Karimganj, R.layout.spinner_layout);
                                                    break;
                                                case "Karimganj(করিমগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shibpur, R.layout.spinner_layout);
                                                    break;
                                                case "Bajitpur(বাজিতপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bajitpur, R.layout.spinner_layout);
                                                    break;
                                                case "Ashtagram(অষ্টগ্রাম)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ashtagram, R.layout.spinner_layout);
                                                    break;
                                                case "Mithamin(মিঠামইন)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mithamin, R.layout.spinner_layout);
                                                    break;
                                                case "Nikli(নিকলী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nikli, R.layout.spinner_layout);
                                                    break;
                                                case "Harirampur(হরিরামপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Harirampur, R.layout.spinner_layout);
                                                    break;
                                                case "Saturia(সাটুরিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Saturia, R.layout.spinner_layout);
                                                    break;
                                                case "Manikganj Sadar(মানিকগঞ্জ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Manikganj_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Ghior(ঘিওর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ghior, R.layout.spinner_layout);
                                                    break;
                                                case "Daulatpur (দৌলতপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Daulatpur, R.layout.spinner_layout);
                                                    break;
                                                case "Shibalaya(শিবালয়)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shibalaya, R.layout.spinner_layout);
                                                    break;
                                                case "Singair(সিংগাইর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Singair, R.layout.spinner_layout);
                                                    break;
                                                case "Savar(সাভার)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Savar, R.layout.spinner_layout);
                                                    break;
                                                case "Dhamrai(ধামরাই)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dhamrai, R.layout.spinner_layout);
                                                    break;
                                                case "Keraniganj(কেরাণীগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Keraniganj, R.layout.spinner_layout);
                                                    break;
                                                case "Nawabganj (নবাবগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nawabganj_dhaka, R.layout.spinner_layout);
                                                    break;
                                                case "Dohar(দোহার)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dohar, R.layout.spinner_layout);
                                                    break;
                                                case "Munshiganj Sadar(মুন্সিগঞ্জ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Munshiganj_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Srinagar(শ্রীনগর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Srinagar, R.layout.spinner_layout);
                                                    break;
                                                case "Sirajdikhan(সিরাজদিখান)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sirajdikhan, R.layout.spinner_layout);
                                                    break;
                                                case "Louhjong(লৌহজং)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Louhjong, R.layout.spinner_layout);
                                                    break;
                                                case "Gazaria(গজারিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gazaria, R.layout.spinner_layout);
                                                    break;
                                                case "Tongibari(টংগীবাড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tongibari, R.layout.spinner_layout);
                                                    break;
                                                case "Rajbari Sadar(রাজবাড়ী সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rajbari_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Goalanda(গোয়ালন্দ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Goalanda, R.layout.spinner_layout);
                                                    break;
                                                case "Pangsha(পাংশা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Pangsha, R.layout.spinner_layout);
                                                    break;
                                                case "Baliakandi(বালিয়াকান্দি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Baliakandi, R.layout.spinner_layout);
                                                    break;
                                                case "Kalukhali(কালুখালী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaukhali, R.layout.spinner_layout);
                                                    break;
                                                case "Madaripur Sadar(মাদারীপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Madaripur_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Shibchar(শিবচর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Shibchar, R.layout.spinner_layout);
                                                    break;
                                                case "Kalkini(কালকিনি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kalkini, R.layout.spinner_layout);
                                                    break;
                                                case "Rajoir(রাজৈর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rajoir, R.layout.spinner_layout);
                                                    break;
                                                case "Gopalganj Sadar(গোপালগঞ্জ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gopalganj_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Kashiani(কাশিয়ানী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kashiani, R.layout.spinner_layout);
                                                    break;
                                                case "Tungipara(টুংগীপাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tungipara, R.layout.spinner_layout);
                                                    break;
                                                case "Kotalipara(কোটালীপাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kotalipara, R.layout.spinner_layout);
                                                    break;
                                                case "Muksudpur(মুকসুদপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Muksudpur, R.layout.spinner_layout);
                                                    break;
                                                case "Faridpur Sadar(ফরিদপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Faridpur_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Alfadanga(আলফাডাঙ্গা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Alfadanga, R.layout.spinner_layout);
                                                    break;
                                                case "Boalmari(বোয়ালমারী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Boalmari, R.layout.spinner_layout);
                                                    break;
                                                case "Sadarpur(সদরপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sadarpur, R.layout.spinner_layout);
                                                    break;
                                                case "Nagarkanda(নগরকান্দা))":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nagarkanda, R.layout.spinner_layout);
                                                    break;

                                                case "Vanga(ভাঙ্গা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bhanga, R.layout.spinner_layout);
                                                    break;

                                                case "Charbhadrasan(চরভদ্রাসন)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Charbhadrasan, R.layout.spinner_layout);
                                                    break;

                                                case "Madhukhali(মধুখালী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Madhukhali, R.layout.spinner_layout);
                                                    break;

                                                case "Saltha(সালথা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Saltha, R.layout.spinner_layout);
                                                    break;
                                                case "Sadullapur(সাদুল্লাপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sadullapur, R.layout.spinner_layout);
                                                    break;
                                                case "Gaibandha Sadar (গাইবান্ধা সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gaibandha_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Palashbari(পলাশবাড়ী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Palashbari, R.layout.spinner_layout);
                                                    break;
                                                case "Saghata(সাঘাটা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Saghata, R.layout.spinner_layout);
                                                    break;
                                                case "Gobindganj (গোবিন্দগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gobindaganj, R.layout.spinner_layout);
                                                    break;
                                                case "Sundarganj(সুন্দরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sundarganj, R.layout.spinner_layout);
                                                    break;
                                                case "Fulchhari(ফুলছড়ি)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Fulchhari, R.layout.spinner_layout);
                                                    break;
                                                case "Thakurgaon Sadar(ঠাকুরগাঁও সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Thakurgaon_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Pirganj (পীরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Pirganj, R.layout.spinner_layout);
                                                    break;
                                                case "Ranishankail(রাণীশংকৈল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ranishankail, R.layout.spinner_layout);
                                                    break;
                                                case "Haripur(হরিপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Haripur, R.layout.spinner_layout);
                                                    break;
                                                case "Baliadangi(বালিয়াডাঙ্গী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Baliadangi, R.layout.spinner_layout);
                                                    break;
                                                case "Rangpur Sadar(রংপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rangpur_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Gangachara(গংগাচড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gangachara, R.layout.spinner_layout);
                                                    break;
                                                case "Taraganj(তারাগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Taraganj, R.layout.spinner_layout);
                                                    break;
                                                case "Badarganj(বদরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Badarganj, R.layout.spinner_layout);
                                                    break;
                                                case "Mithapukur(মিঠাপুকুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mithapukur, R.layout.spinner_layout);
                                                    break;
                                                case "Pirganj(পীরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Pirganj, R.layout.spinner_layout);
                                                    break;
                                                case "Kaunia(কাউনিয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kaunia, R.layout.spinner_layout);
                                                    break;
                                                case "Pirgachha(পীরগাছা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Pirgachha, R.layout.spinner_layout);
                                                    break;
                                                case "Kurigram Sadar(কুড়িগ্রাম সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kurigram_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Nageshwari (নাগেশ্বরী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nageshwari, R.layout.spinner_layout);
                                                    break;
                                                case "Bhurungamari (ভুরুঙ্গামারী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bhurungamari, R.layout.spinner_layout);
                                                    break;
                                                case "Fulbari (ফুলবাড়ী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Fulbari_Kurigram, R.layout.spinner_layout);
                                                    break;
                                                case "Rajarhat(রাজারহাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Rajarhat, R.layout.spinner_layout);
                                                    break;
                                                case "Ulipur (উলিপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ulipur, R.layout.spinner_layout);
                                                    break;
                                                case "Chilmari(চিলমারী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Chilmari, R.layout.spinner_layout);
                                                    break;
                                                case "Char Rajibpur(চর রাজিবপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Char_Rajibpur, R.layout.spinner_layout);
                                                    break;
                                                case "Roumari(রৌমারী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Roumari, R.layout.spinner_layout);
                                                    break;
                                                case "Sherpur Sadar (শেরপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sherpur_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Nalitabari (নালিতাবাড়ী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nalitabari, R.layout.spinner_layout);
                                                    break;
                                                case "Sreebardi (শ্রীবরদী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sreevardi, R.layout.spinner_layout);
                                                    break;
                                                case "Nakla(নকলা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nakla, R.layout.spinner_layout);
                                                    break;
                                                case "Jhenaigati(ঝিনাইগাতী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jhenaigati, R.layout.spinner_layout);
                                                    break;
                                                case "Fulbaria (ফুলবাড়ীয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Fulbaria, R.layout.spinner_layout);
                                                    break;
                                                case "Trishal(ত্রিশাল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Trishal, R.layout.spinner_layout);
                                                    break;
                                                case "Bhaluka (ভালুকা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bhaluka, R.layout.spinner_layout);
                                                    break;
                                                case "Muktagachha(মুক্তাগাছা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Muktagachha, R.layout.spinner_layout);
                                                    break;
                                                case "Mymensingh Sadar (ময়মনসিংহ সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mymensingh_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Dhobaura(ধোবাউড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dhobaura, R.layout.spinner_layout);
                                                    break;
                                                case "Phulpur(ফুলপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Phulpur, R.layout.spinner_layout);
                                                    break;
                                                case "Haluaghat(হালুয়াঘাট)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Haluaghat, R.layout.spinner_layout);
                                                    break;

                                                case "Gauripur (গৌরীপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gauripur, R.layout.spinner_layout);
                                                    break;
                                                case "Gafargaon (গফরগাঁও)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Gafargaon, R.layout.spinner_layout);
                                                    break;
                                                case "Ishwarganj (ঈশ্বরগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Ishwarganj, R.layout.spinner_layout);
                                                    break;
                                                case "Nandail (নান্দাইল)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Nandail, R.layout.spinner_layout);
                                                    break;
                                                case "Tarakanda(তারাকান্দা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Tarakanda, R.layout.spinner_layout);
                                                    break;
                                                case "Jamalpur Sadar (জামালপুর সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Jamalpur_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Melandah (মেলান্দহ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Melandah, R.layout.spinner_layout);
                                                    break;
                                                case "Islampur (ইসলামপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Islampur, R.layout.spinner_layout);
                                                    break;
                                                case "Dewanganj (দেওয়ানগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Dewanganj, R.layout.spinner_layout);
                                                    break;
                                                case "Sarishabari(সরিষাবাড়ী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Sarishabari, R.layout.spinner_layout);
                                                    break;
                                                case "Madarganj (মাদারগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Madarganj, R.layout.spinner_layout);
                                                    break;
                                                case "Bakshiganj (বকশীগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Bakshiganj, R.layout.spinner_layout);
                                                    break;
                                                case "Netrokona Sadar(নেত্রকোণা সদর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Netrokona_Sadar, R.layout.spinner_layout);
                                                    break;
                                                case "Atpara (আটপাড়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Atpara, R.layout.spinner_layout);
                                                    break;
                                                case "Barhatta(বারহাট্টা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Barhatta, R.layout.spinner_layout);
                                                    break;
                                                case "Durgapur (দুর্গাপুর)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Durgapur, R.layout.spinner_layout);
                                                    break;

                                                case "Kalmakanda (কলমাকান্দা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kalmakanda, R.layout.spinner_layout);
                                                    break;
                                                case "Kendua(কেন্দুয়া)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Kendua, R.layout.spinner_layout);
                                                    break;
                                                case "Madan(মদন))":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Madan, R.layout.spinner_layout);
                                                    break;
                                                case "Mohanganj(মোহনগঞ্জ)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Mohanganj, R.layout.spinner_layout);
                                                    break;
                                                case "Purbadhala (পূর্বধলা)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Purbadhala, R.layout.spinner_layout);
                                                    break;

                                                case "Khaliajuri (খালিয়াজুরী)":
                                                    unionAdapter = ArrayAdapter.createFromResource(parent.getContext()
                                                            , R.array.Khaliajuri, R.layout.spinner_layout);
                                                    break;

                                                default:
                                                    break;

                                            }
                                            unionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            binding.unionSpinner.setAdapter(unionAdapter);
                                            binding.unionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    unionSpin = binding.unionSpinner.getSelectedItem().toString().trim();
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    Toast.makeText(DonorRegistrationActivity.this,
                            "Have no Upzila", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//spinner end=============================================================================================

        // handle click for pic image
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(DonorRegistrationActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(550)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(PROFILE_PIC);
            }
        });

        // already hav an account
        binding.alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Toast.makeText(getApplicationContext(), "Back to Login", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        binding.registerbtnDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select_Radiobtn = Radiogroup.findViewById(Radiogroup.getCheckedRadioButtonId());
                select_Radiobtn_forusertype = radiogroup_for_usertype.findViewById(radiogroup_for_usertype.getCheckedRadioButtonId());
                fullname = binding.nameEdt.getText().toString().trim();
                mobile = binding.phonenumberEdt.getText().toString().trim();
                bloodGroup = binding.bloodgrpSpinner.getSelectedItem().toString().trim();
                Division = binding.divisionSpinner.getSelectedItem().toString().trim();
                District = binding.districtSpinner.getSelectedItem().toString().trim();
                Upzila = binding.upzillaSpinner.getSelectedItem().toString().trim();
                Union = binding.unionSpinner.getSelectedItem().toString().trim();
                village = binding.addresEdT.getText().toString().trim();
                countryCodewithnumber = binding.countryPicker.getSelectedCountryCodeWithPlus() + " " + mobile;
                countryCodenumber = binding.countryPicker.getSelectedCountryCodeWithPlus();
                email = binding.registeremailEdt.getText().toString().trim();
                password = binding.registerpassEdt.getText().toString().trim();
                gender = select_Radiobtn.getText().toString().trim();
                confirmPassword = binding.confirmpassEdt.getText().toString().trim();

                if (TextUtils.isEmpty(fullname)) {
                    binding.nameEdt.setError("Name required");

                } else if (Radiogroup.getCheckedRadioButtonId() == -1) {
                    showAlert("Gender is required");
                    Toast.makeText(DonorRegistrationActivity.this, "Gender is required", Toast.LENGTH_SHORT).show();

                } else if (bloodGroup.equals("Select your Blood Group")) {
                    Toast.makeText(DonorRegistrationActivity.this, "Blood group is required", Toast.LENGTH_SHORT).show();

                } else if (radiogroup_for_usertype.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(DonorRegistrationActivity.this, "User type is required", Toast.LENGTH_SHORT).show();

                } else if (Division.equals("Select Your Division")) {
                    Toast.makeText(DonorRegistrationActivity.this, "District is required", Toast.LENGTH_SHORT).show();
                    showAlert("Field is required");

                } else if (District.equals("Select Your District")) {
                    Toast.makeText(DonorRegistrationActivity.this, "District is required", Toast.LENGTH_SHORT).show();
                    showAlert("Field is required");

                } else if (Upzila.equals("Select Your Upzila")) {
                    Toast.makeText(DonorRegistrationActivity.this, "Upzila is required", Toast.LENGTH_SHORT).show();
                    showAlert("Field is required");

                } else if (Union.equals("Select Your Union")) {
                    Toast.makeText(DonorRegistrationActivity.this, "Union is required", Toast.LENGTH_SHORT).show();
                    showAlert("Field is required");

                } else if (TextUtils.isEmpty(village)) {
                    binding.addresEdT.setError("Village required");

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.registeremailEdt.setError("Email required");
                } else if (TextUtils.isEmpty(password)) {
                    binding.registerpassEdt.setError("Password required");

                } else if (password.length() < 6) {
                    binding.registerpassEdt.setError("Password atleast 6 Char");
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    binding.confirmpassEdt.setError("Confirm pass required");

                } else if (!password.equals(confirmPassword)) {
                    showAlert("Password don\'t match");
                    Toast.makeText(DonorRegistrationActivity.this, "Password don\'t match", Toast.LENGTH_SHORT).show();
                } else if (mobile.length() != 11) {
                    showAlert("Invalid Phone number.Enter at least 11 char");
                }
//                else if (imageUri == null) {
//                    showAlert("Select a Image Please 🙏");
//                }
                else {
                    register(fullname, email, password);

//                    url="https://usitfo.com/Blood/register.php?n=" +fullname+
//                    "&e=" + email +"&m="+mobile + "&g="+gender+"&u="+userid;
                }
            }
        });

    }

    private void register(String fullname, String email, String password) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Timestamp
                        long timestamp = System.currentTimeMillis();
                        userid = mAuth.getUid();
                        //setup data on RealDB
                        String Groupsearch = select_Radiobtn_forusertype.getText().toString().trim() + bloodGroup;
                        String Districtsearch = select_Radiobtn_forusertype.getText().toString().trim() + bloodGroup + District;
                        String Upzillasearch = select_Radiobtn_forusertype.getText().toString().trim() + bloodGroup + District + Upzila;
                        String Querysearch = bloodGroup + District + Upzila;
                        String Unionsearch = select_Radiobtn_forusertype.getText().toString().trim() + bloodGroup + District + Upzila + Union;
                        String DonorsearchTags = fullname.toLowerCase() + "," + bloodGroup.toLowerCase() + "," + District.toLowerCase() + "," + Upzila.toLowerCase() + "," + Union.toLowerCase() + "," + "RB" + maxId;

                        HashMap<String, Object> usermap = new HashMap<>();
                        usermap.put("uid", userid);
                        usermap.put("memberID", "RB" + maxId);
                        usermap.put("Usertype", select_Radiobtn_forusertype.getText().toString().trim());//possible value are user,admin: will make admin manually in RDB changes developer
                        usermap.put("fullname", fullname);
                        usermap.put("Mobileno", mobile);
                        usermap.put("countryCode", countryCodenumber);
                        usermap.put("countryCodewithnumber", countryCodewithnumber);
                        usermap.put("gender", select_Radiobtn.getText().toString().trim());
                        usermap.put("bloodGroup", bloodGroup);
                        usermap.put("ActiveStatus", "No");
                        usermap.put("donateNow", "");
                        usermap.put("OkFavourite", "");
                        usermap.put("ActiveDate", "");
                        usermap.put("District", District);
                        usermap.put("Division", Division);
                        usermap.put("Upzila", Upzila);
                        usermap.put("Union", Union);
                        usermap.put("village", village);
                        usermap.put("email", email);
                        usermap.put("password", password);
                        usermap.put("Profilepic", profilepicUrl);
                        usermap.put("Groupsearch", Groupsearch);
                        usermap.put("DonorSearchTag", DonorsearchTags);
                        usermap.put("Districtsearch", Districtsearch);
                        usermap.put("Upzillasearch", Upzillasearch);
                        usermap.put("Querysearch", Querysearch);
                        usermap.put("Unionsearch", Unionsearch);
                        usermap.put("timestamp", timestamp);
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                        databaseReference.child(userid).setValue(usermap).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                mAuth.signOut();

                                startActivity(new Intent(DonorRegistrationActivity.this, LoginActivity.class));
                                finish();

                            } else {
                                progressDialog.dismiss();
                                showAlert(task.getException().getMessage());
                            }
                        });

                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    showAlert(e.getMessage());
                });

    }

    // register method end here .........=================***************************=========================================================
    private void showAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DonorRegistrationActivity.this);
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

    // showAlert end here ==============================================================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_PIC && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            binding.profileImage.setImageURI(imageUri);
            sendImagetoStorage(imageUri);
        } else {
            Toast.makeText(this, "not ok", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImagetoStorage(Uri imageUri) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.setIcon(R.drawable.ic_baseline_error_outline_24);
        pd.show();
        final StorageReference store = storageReference.child("ProfileImages");
        StorageReference imagename = store.child("userProfile" + System.currentTimeMillis());
        imagename.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                pd.dismiss();
                Toast.makeText(DonorRegistrationActivity.this, "img uploaded", Toast.LENGTH_SHORT).show();


                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        profilepicUrl = String.valueOf(uri);

                        Toast.makeText(DonorRegistrationActivity.this, "done", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DonorRegistrationActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                int count = (int) progressPercent;
                pd.setMessage("Progress: " + (count++) + " %");
            }
        });

    }


}