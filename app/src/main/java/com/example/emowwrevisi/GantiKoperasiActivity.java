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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GantiKoperasiActivity extends AppCompatActivity {

    private EditText email1,password1;
    private ImageButton btnback;
    private Button ChangeButtton;
    private ProgressDialog progressDialog;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private String[] susu = {"Pagi","Sore","Pagi dan Sore"};
    long now = System.currentTimeMillis() - 1000;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_koperasi);

        email1 = findViewById(R.id.gantiemail);
        ChangeButtton = findViewById(R.id.change);
        btnback = findViewById(R.id.btnback);
        password1 = findViewById(R.id.konfirmasi);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        ChangeButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEmail();
            }
        });

    }

    private String getEmail,password;

    private void newEmail() {
        getEmail = email1.getText().toString().trim();
        password = password1.getText().toString().trim();
        if(TextUtils.isEmpty(getEmail)){
            Toast.makeText(this, "Email baru harap diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password harap diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        saverFirebase();
    }

    private void reauth(final String getEmail) {

        final FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),password);
        mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                user.updateEmail(getEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(GantiKoperasiActivity.this, "Email updated...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent (GantiKoperasiActivity.this,SettingKoperasiActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(GantiKoperasiActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //authenticated failed, show reason
                progressDialog.dismiss();
                Toast.makeText(GantiKoperasiActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });;
    }

    private void saverFirebase() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email",""+getEmail);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Koperasi");
        ref.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reauth(getEmail);
//                upemail();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GantiKoperasiActivity.this, "Gagal Update...", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent (GantiKoperasiActivity.this,SettingKoperasiActivity.class);
        startActivity(intent);
        finish();
    }
}
