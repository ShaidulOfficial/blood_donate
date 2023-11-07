package com.shaidulsoftstudio.roktobindu.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shaidulsoftstudio.roktobindu.normalClass.AppCompat;
import com.shaidulsoftstudio.roktobindu.R;
import com.shaidulsoftstudio.roktobindu.databinding.ActivityLoginBinding;
import com.shaidulsoftstudio.roktobindu.model.Users;


public class LoginActivity extends AppCompat {
    ActivityLoginBinding binding;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private static final int TIME_INTERVAL = 2000;
    private static final String File_Email = "rememberMe";
    private long backPressed;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo wificonn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if ((wificonn == null && wificonn.isConnected()) || networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.no_internet);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loging...");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences(File_Email, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String email = sharedPreferences.getString("svEmail", "");
        String password = sharedPreferences.getString("svPass", "");
        if (sharedPreferences.contains("checked") && sharedPreferences.getBoolean("checked", false) == true) {

            binding.checkRemember.setChecked(true);
        } else {
            binding.checkRemember.setChecked(false);
        }

        binding.loginemailEdt.setText(email);
        binding.loginpassEdt.setText(password);

        binding.createNewAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, DonorRegistrationActivity.class));
            }
        });


        binding.imgregistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,
                        DonorRegistrationActivity.class));
            }
        });

        binding.signupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, DonorRegistrationActivity.class));
                Animatoo.animateSlideRight(LoginActivity.this);
            }
        });
        //================******************************=========================================================================
//ekbar login korle r korte hobe na
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "no member", Toast.LENGTH_SHORT).show();
        }


        //handle click login

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.loginemailEdt.getText().toString();
                String password = binding.loginpassEdt.getText().toString();
                // String checkpass="^(?=\\S+$).(6,20)$";

                if (binding.checkRemember.isChecked()) {
                    editor.putBoolean("checked", true);
                    editor.apply();
                    storeDatausingSharedPref(email, password);
                    if (email.isEmpty()) {
                        showAlert("Email field can no be empty");

                    } else if (password.isEmpty()) {
                        showAlert("Password field is can no be empty");
                    } else if (password.length() < 6) {
                        showAlert("Password atleast 6 char");
                    } else {
                        loginUser(email, password);
                        Toast.makeText(getApplicationContext(), "loged in", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        showAlert("Email field can no be empty");

                    } else if (password.isEmpty()) {
                        showAlert("Password field is can no be empty");
                    } else if (password.length() < 6) {
                        showAlert("Password atleast 6 char");
                    } else {
                        getSharedPreferences(File_Email, MODE_PRIVATE).edit().clear().commit();
                        loginUser(email, password);
                        Toast.makeText(getApplicationContext(), "loged in", Toast.LENGTH_SHORT).show();
                    }

                }


            }

            private void loginUser(String email, String password) {
                progressDialog.show();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                showAlert(e.getMessage());


                            }
                        });

            }

            private void showAlert(String msg) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
        });

        binding.forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,
                        ForgetPasswordActivity.class));
            }
        });
    }

    private void storeDatausingSharedPref(String email, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(File_Email, MODE_PRIVATE).edit();
        editor.putString("svEmail", email);
        editor.putString("svPass", password);
        editor.apply();
    }


    @Override
    public void onBackPressed() {


        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {

            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press again to exit app", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }
}