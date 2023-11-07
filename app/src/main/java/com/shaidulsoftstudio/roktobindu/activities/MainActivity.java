package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
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
import com.intuit.sdp.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.shaidulsoftstudio.roktobindu.fragments.AllBloodGroupsFragment;
import com.shaidulsoftstudio.roktobindu.fragments.DashboardFragment;
import com.shaidulsoftstudio.roktobindu.fragments.DonorRequestlistFragment;
import com.shaidulsoftstudio.roktobindu.fragments.MenuFragment;
import com.shaidulsoftstudio.roktobindu.fragments.NotificationFragment;
import com.shaidulsoftstudio.roktobindu.fragments.PostFragment;
import com.shaidulsoftstudio.roktobindu.fragments.PostReceiverFragment;
import com.shaidulsoftstudio.roktobindu.fragments.ReceiverRequestlistFragment;
import com.shaidulsoftstudio.roktobindu.fragments.SearchFragment;
import com.shaidulsoftstudio.roktobindu.fragments.SettingFragment;
import com.shaidulsoftstudio.roktobindu.model.Users;
import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;

import java.util.List;

import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityMainBinding;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompat {

    ReviewInfo reviewInfo;
    ReviewManager reviewManager;
    FirebaseAuth firebaseAuth;
    String profileimage1;
    private static final int TIME_INTERVAL = 2000;
    private long backPressed;
    Toolbar toolbar;
    Users model;
    ActivityMainBinding binding;
    CircleImageView nav_profile_image;
    ImageView nav_activeLightred, nav_activeLightgreen,bloodGroupImv;
    TextView  action_title, nav_fullname, emailnavTv, activeStatustv, usertypeTv, nav_bloodGroupTv, nave_memberIDno;
    ImageView imgRed, imgGreen;
    CircleImageView profile_imagetoolbar;
    DatabaseReference userRef, databaseReference;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activateReviewInfo();
        model=new Users();
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.main_toolbar);
        action_title = findViewById(R.id.title_tool);
        profile_imagetoolbar = findViewById(R.id.profile_imagetoolbar);
        imgGreen = findViewById(R.id.activeLightgreentoolbar);
        imgRed = findViewById(R.id.activeLightredtoolbar);
        fragmentManager = getSupportFragmentManager();


        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

//        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        NetworkInfo wificonn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if ((wificonn == null && wificonn.isConnected()) || networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
//
//            Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.no_internet);
//            dialog.setCancelable(false);
//            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.MATCH_PARENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
//            dialog.show();
//
//            AppCompatButton tryagain = dialog.findViewById(R.id.try_btn);
//            AppCompatButton connect = dialog.findViewById(R.id.connect_btn);
//
//            tryagain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    recreate();
//                }
//            });
//            connect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                }
//            });
//
//        } else {
//
//            Toast.makeText(this, "Connection success", Toast.LENGTH_SHORT).show();
//
//        }


        //========================================================================================================

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//
//            }
//        });
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//       binding.mainBanneradView.loadAd(adRequest);

        binding.mewbottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.homebottom));
        binding.mewbottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.search_24));
        binding.mewbottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.add_circle_24));
        binding.mewbottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.view_list_24));
        binding.mewbottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.notifications_24));
        binding.mewbottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                switch (item.getId()) {

                    case 1:
                        action_title.setText(R.string.dashboard);
                        Fragment fragmentdashboard = new MenuFragment();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.frameLayout_dashboard, fragmentdashboard).commit();
                        break;

                    case 2:
                        action_title.setText(R.string.blood_search);
                        Fragment fragmentsearch = new SearchFragment();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frameLayout_dashboard, fragmentsearch).commit();
                        break;

                    case 3:

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String type = snapshot.child("Usertype").getValue().toString();
                                if (type.equals("Receiver")) {
                                    action_title.setText(R.string.postTitle);
                                    Fragment fragmentpostRecever = new PostReceiverFragment();
                                    FragmentTransaction fragmentTransactionRecever = getSupportFragmentManager().beginTransaction();
                                    fragmentTransactionRecever.replace(R.id.frameLayout_dashboard, fragmentpostRecever).commit();
                                } else {
                                    action_title.setText(R.string.activity);
                                    Fragment fragmentpostdonor = new PostFragment();
                                    FragmentTransaction fragmentTransactiondonor = getSupportFragmentManager().beginTransaction();
                                    fragmentTransactiondonor.replace(R.id.frameLayout_dashboard, fragmentpostdonor).commit();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        break;

                    case 4:

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String type = snapshot.child("Usertype").getValue().toString();

                                if (type.equals("Receiver")) {
                                    action_title.setText(R.string.donorList);
                                    Fragment fragmentlistdonor = new DonorRequestlistFragment();
                                    FragmentTransaction fragmentTransaction7 = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction7.replace(R.id.frameLayout_dashboard, fragmentlistdonor).commit();
                                } else {
                                    action_title.setText(R.string.receiverList);
                                    Fragment fragmentlisttRecever = new ReceiverRequestlistFragment();
                                    FragmentTransaction fragmentTransaction8 = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction8.replace(R.id.frameLayout_dashboard, fragmentlisttRecever).commit();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        break;

                    case 5:
                        action_title.setText(R.string.notification1);
                        Fragment fragmentnotification = new NotificationFragment();
                        FragmentTransaction fragmentTransaction6 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction6.replace(R.id.frameLayout_dashboard, fragmentnotification).commit();
                        break;
                }

            }
        });
        binding.mewbottomNavigation.setCount(5, "10");
        binding.mewbottomNavigation.show(1, true);
        binding.mewbottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        binding.mewbottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(MainActivity.this, "you reselected", Toast.LENGTH_SHORT).show();
            }
        });
        profile_imagetoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,
                        ProfileActivity.class));
                finish();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this,
                binding.drawerLayout,
                toolbar, R.string.nav_draw_open,
                R.string.nav_draw_close);


        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                int id = item.getItemId();

                switch (id) {

                    case R.id.aplus:
                        Intent intent3 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                        intent3.putExtra("group", "A+");
                        startActivity(intent3);
                        break;
                    case R.id.aminus:
                        Intent intent4 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                        intent4.putExtra("group", "A-");
                        startActivity(intent4);
                        break;
                    case R.id.bplus:
                        Intent intent5 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                        intent5.putExtra("group", "B+");
                        startActivity(intent5);
                        break;
                    case R.id.bminus:
                        Intent intent6 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                        intent6.putExtra("group", "B-");
                        startActivity(intent6);
                        break;
                    case R.id.abplus:
                        Intent intent7 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                        intent7.putExtra("group", "AB+");
                        startActivity(intent7);
                        break;
                    case R.id.abminus:
                        Intent intent8 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                        intent8.putExtra("group", "AB-");
                        startActivity(intent8);
                        break;
                    case R.id.oplus:
                        Intent intent9 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                        intent9.putExtra("group", "O+");
                        startActivity(intent9);
                        break;
                    case R.id.ominus:
                        Intent intent10 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                        intent10.putExtra("group", "O-");
                        startActivity(intent10);
                        break;
                    case R.id.compatible:
                        Intent intent11 = new Intent(MainActivity.this, CategorySelectedActivity.class);
                        intent11.putExtra("group", "Compatible with me");
                        startActivity(intent11);
                        break;
                    case R.id.district_donor:
                        Intent intent12 = new Intent(MainActivity.this, DistrictDonorSelectedActivity.class);
                        intent12.putExtra("group", "My District Donors");
                        startActivity(intent12);
                        break;
                    case R.id.union_donor:

                        Intent intent13 = new Intent(MainActivity.this, UnionSelectedActivity.class);
                        intent13.putExtra("group", "My Union Donors");
                        startActivity(intent13);

                        break;
                    case R.id.upzilla_donor:
                        Intent intent14 = new Intent(MainActivity.this, UpzilaDonorSelectedActivity.class);
                        intent14.putExtra("group", "My Upzilla Donors");
                        startActivity(intent14);
                        break;
                    case R.id.favouriteMenu:
                        Intent intent15 = new Intent(MainActivity.this, FavouriteActivity.class);
                        intent15.putExtra("group", "My Favourite Donors");
                        startActivity(intent15);
                        break;


                    case R.id.policy_menu:
                        startActivity(new Intent(MainActivity.this, PolicyActivity.class));
                        finish();
                        break;
                    case R.id.about_menu:
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        finish();
                        break;
                    case R.id.allbloodgroups_menu:
                        action_title.setText(R.string.allbloodgroups);
                        Fragment fragmentnotification12 = new AllBloodGroupsFragment();
                        FragmentTransaction fragmentTransaction13 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction13.replace(R.id.frameLayout_dashboard, fragmentnotification12).commit();
                        break;
                    case R.id.setting_menu:
                        action_title.setText(R.string.setting);
                        Fragment settingfragmentnotification = new SettingFragment();
                        FragmentTransaction settingfragmentTransaction = getSupportFragmentManager().beginTransaction();
                        settingfragmentTransaction.replace(R.id.frameLayout_dashboard, settingfragmentnotification).commit();
                        break;
                    case R.id.rateus_menu:
                        String appname = getPackageName();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appname)));

                        break;
                    case R.id.share_menu:
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Share Rokto Bindo");
                            String shareMessage = "http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                            intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(intent, "Share by"));
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.updateActivityMenu:

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String type = snapshot.child("Usertype").getValue().toString();
                                if (type.equals("Receiver")) {
                                    recevercustDialog();
                                } else {
                                    action_title.setText(R.string.activity);
                                    Fragment fragmentnotification18 = new PostFragment();
                                    FragmentTransaction fragmentTransaction13 = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction13.replace(R.id.frameLayout_dashboard, fragmentnotification18).commit();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        break;

                    case R.id.more_apps_menu:
                        try {
                            Uri uri = Uri.parse("market://search?q=pub:Shaidul Soft. Studio");
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        } catch (ActivityNotFoundException e) {
                            Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=Shaidul Soft. Studio");
                            startActivity(new Intent(Intent.ACTION_VIEW, uri1));
                        }
                        break;
                    case R.id.update_menu:
                        String appsname = getPackageName();
                        Intent update = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appsname));
                        startActivity(update);
                        break;

                    case R.id.profilemenu:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                    case R.id.logoutmenu:
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;

                    case R.id.languagemenu:
                        startActivity(new Intent(MainActivity.this, LanguageActivity.class));
                        break;
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

//========================================= header view start===================

        nav_profile_image = binding.navView.getHeaderView(0).findViewById(R.id.nav_profile_image1);
        nav_fullname = binding.navView.getHeaderView(0).findViewById(R.id.nav_fullname);
        emailnavTv = binding.navView.getHeaderView(0).findViewById(R.id.emailnavTv);
        usertypeTv = binding.navView.getHeaderView(0).findViewById(R.id.usertypeTv);
        activeStatustv = binding.navView.getHeaderView(0).findViewById(R.id.nav_availableTv);
        nav_activeLightred = binding.navView.getHeaderView(0).findViewById(R.id.nav_activeLightred);
        nav_activeLightgreen = binding.navView.getHeaderView(0).findViewById(R.id.nav_activeLightgreen);
        nav_bloodGroupTv = binding.navView.getHeaderView(0).findViewById(R.id.nav_bloodGroupTv);
        nave_memberIDno = binding.navView.getHeaderView(0).findViewById(R.id.nave_memberIDno);

        userRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        nav_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String fullname = snapshot.child("fullname").getValue().toString();
                    nav_fullname.setText(fullname);

                    String memberId = snapshot.child("memberID").getValue().toString();
                    nave_memberIDno.setText(memberId);

                    String email = snapshot.child("email").getValue().toString();
                    emailnavTv.setText(email);

                    String type = snapshot.child("Usertype").getValue().toString();
                    usertypeTv.setText(type);

                     String activeStatusdb = snapshot.child("ActiveStatus").getValue().toString();

                    String bloodgroup = snapshot.child("bloodGroup").getValue().toString();
                    nav_bloodGroupTv.setText(bloodgroup);

                    profileimage1 = snapshot.child("Profilepic").getValue().toString();

                    Glide.with(MainActivity.this).load(profileimage1).placeholder(R.drawable.profile_image).into(nav_profile_image);

                    if (type.equals("Donor")) {
                        activeStatustv.setVisibility(View.VISIBLE);
                        //  activeStatustv.setTextColor(Color.CYAN);

                    } else {
                        Toast.makeText(MainActivity.this, "ok.", Toast.LENGTH_SHORT).show();
                    }


                    if (activeStatusdb.equals("Yes")) {
                        activeStatustv.setVisibility(View.VISIBLE);
                        activeStatustv.setText(R.string.notavailable);
                        activeStatustv.setTextColor(Color.RED);
                        nav_activeLightred.setVisibility(View.VISIBLE);
                        imgRed.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(MainActivity.this, "ok.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "no header", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    profileimage1 = snapshot.child("Profilepic").getValue().toString();
                    Glide.with(MainActivity.this).load(profileimage1).placeholder(R.drawable.profile_image).into(profile_imagetoolbar);

                } else {
                    Toast.makeText(MainActivity.this, "no header", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        checkPermission();

    }

    private void activateReviewInfo() {
        reviewManager = ReviewManagerFactory.create(MainActivity.this);
        Task<ReviewInfo> managerInfotask = reviewManager.requestReviewFlow();
        managerInfotask.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                    Task<Void> flow = reviewManager.launchReviewFlow(MainActivity.this, reviewInfo);
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
                    Toast.makeText(MainActivity.this, "review failed to start", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void recevercustDialog() {
        Dialog dialog1 = new Dialog(MainActivity.this);
        dialog1.setContentView(R.layout.custom_fail_dialog);
        dialog1.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
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
                startActivity(new Intent(MainActivity.this,
                        MainActivity.class));
                finish();
            }
        });
    }


    private void checkPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {

                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getBaseContext(), "Press again to exit app", Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();
        }

    }
}



