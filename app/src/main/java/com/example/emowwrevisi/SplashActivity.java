package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user==null){
                    // user not logged in start login activity
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }else {
                    checkUserType();
                }
            }
        }, 1000);
    }

    private void checkUserType() {
        //if user is koperasi, start koperasi main screen
        //if user is peternak, start peternak main screen

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String accountType = ""+dataSnapshot.child("accountType").getValue();
                if (accountType.equals("Koperasi")){
                    //user is koperasi
                    startActivity(new Intent(SplashActivity.this,DashboardKoperasiActivity.class));
                    finish();
                } else {
                    //user is peternak
                    startActivity(new Intent(SplashActivity.this,DasboardPeternakActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SplashActivity.this, ""+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
