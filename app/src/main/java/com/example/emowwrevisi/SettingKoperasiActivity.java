package com.example.emowwrevisi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.emowwrevisi.Internat.NetworkChangeListener;

public class SettingKoperasiActivity extends AppCompatActivity {
    private TextView profil,password,kredit,email;
    private ImageButton btnback;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_koperasi);
        kredit = findViewById(R.id.kredit);
        profil = findViewById(R.id.profilakun);
        password = findViewById(R.id.keamananakun);
        btnback = findViewById(R.id.btnback);
        email = findViewById(R.id.ganemail);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingKoperasiActivity.this,GantiKoperasiActivity.class));
                finish();
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingKoperasiActivity.this,ProfilKoperasiActivity.class));
                finish();
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingKoperasiActivity.this,KeamananKoperasiActivity.class));
                finish();
            }
        });
        kredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingKoperasiActivity.this,KreditKoperasiActivity.class));
                finish();
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent (SettingKoperasiActivity.this,DashboardKoperasiActivity.class);
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
