package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class KeamananPeternakActivity extends AppCompatActivity {
    private EditText Passwordbaru, Passwordlama, KonfirmasiPass;
    private Button simpanpass;
    private ImageButton btnback;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keamanan_peternak);
        Passwordbaru = findViewById(R.id.passwordbar);
        Passwordlama = findViewById(R.id.passwordlam);
        KonfirmasiPass = findViewById(R.id.passwordbarkon);
        simpanpass = findViewById(R.id.simpanpassword1);
        btnback = findViewById(R.id.btnback);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        simpanpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPassword();
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private String oldPassword, newPassword, konfirmPassword;

    private void inputPassword() {
        oldPassword = Passwordlama.getText().toString().trim();
        newPassword = Passwordbaru.getText().toString().trim();
        konfirmPassword = KonfirmasiPass.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(KeamananPeternakActivity.this, "Masukan Password lama", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(KeamananPeternakActivity.this, "Masukan Password Baru", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(KeamananPeternakActivity.this, "Password Minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(konfirmPassword)) {
            Toast.makeText(KeamananPeternakActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            return;
        }

        updatePassword(oldPassword, newPassword);
    }

    private void updatePassword(String oldPassword, final String newPassword) {
        progressDialog.setMessage("update password akun...");
        progressDialog.show();

        //get current user
        final FirebaseUser user = mAuth.getCurrentUser();

        //before changing password re-authenticate the user
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //successfully authenticated, begin update
                user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        startActivity(new Intent(KeamananPeternakActivity.this,SettingPeternakActivity.class));
                        finish();
                        Toast.makeText(KeamananPeternakActivity.this, "Password Updated...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(KeamananPeternakActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //authenticated failed, show reason
                progressDialog.dismiss();
                Toast.makeText(KeamananPeternakActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(KeamananPeternakActivity.this, SettingPeternakActivity.class);
        startActivity(intent);
        finish();
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
}
