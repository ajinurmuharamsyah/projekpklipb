package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText usemail, uspassword;
    private TextView register, lupapassword;
    private Button btnlogin;

    private long backPressedTime;
    private Toast backToast;

    private ProgressDialog progressDialog;

    private LayoutInflater inflater;
    private AlertDialog.Builder reset_alert;

    private FirebaseAuth mAuth;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usemail = findViewById(R.id.userEmail);
        uspassword = findViewById(R.id.userPassword);
        register = findViewById(R.id.tv4);
        btnlogin = findViewById(R.id.loginKop);
        lupapassword = findViewById(R.id.lupapassword);

        mAuth = FirebaseAuth.getInstance();

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        lupapassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = inflater.inflate(R.layout.view_reset, null);

                reset_alert.setTitle("Apakah Kamu ingin mengubah passsword?")
                        .setMessage("Masukan alamat email kamu untuk mendapat link ubah password.")
                        .setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Mengirim Link");
                                progressDialog.show();
                                EditText email = view.findViewById(R.id.reset_email);
                                if (email.getText().toString().isEmpty()) {
                                    email.setError("Mohon diisi Alamat Email");
                                    return;
                                }

                                //send the reset link
                                mAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Link Ubah Password telah terkirim ke alamat email anda", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Mohon cek alamat email anda benar", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Batal", null)
                        .setView(view)
                        .create().show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterKoperasiActivity.class));
                finish();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private String Email, Password;

    private void loginUser() {
        Email = usemail.getText().toString().trim();
        Password = uspassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            Toast.makeText(this, "Email anda tidak terdaftar...", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Password)) {
            Toast.makeText(this, "Password Kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging In");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //logged in successfully
                makeMeOnline();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void makeMeOnline() {
        //after logging in, make user online
        progressDialog.setMessage("Checking User...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "true");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //update succesfully
                checkUserType();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed updating
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserType() {
        //if user is koperasi, start koperasi main screen
        //if user is peternak, start peternak main screen

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String accountType = "" + ds.child("accountType").getValue();
                    if (accountType.equals("Koperasi")) {
                        progressDialog.dismiss();
                        //user is koperasi
                        startActivity(new Intent(LoginActivity.this, DashboardKoperasiActivity.class));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        //user is peternak
                        startActivity(new Intent(LoginActivity.this, DasboardPeternakActivity.class));
                        finish();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Tekan Kembali untuk Keluar Aplikasi", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}
