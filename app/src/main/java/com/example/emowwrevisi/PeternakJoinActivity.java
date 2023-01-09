package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PeternakJoinActivity extends AppCompatActivity {
    private ImageButton btnback;
    private TextView namatv, tempattv, emailtv, pendidikantv, anggotakoperasitv, alamat,umur;
    private ImageView profilTv, keluar;
    private Button tolak, terima;

    private FirebaseAuth mAuth;

    private String peternakUid, validasi, anggotabaru, koperasiname;

    //    private BroadcastReceiver broadcastReceiver;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peternak_join);
        namatv = findViewById(R.id.namapet);
        tempattv = findViewById(R.id.tempattv);
        emailtv = findViewById(R.id.emailtv);
        pendidikantv = findViewById(R.id.pendidikantv);
        btnback = findViewById(R.id.btnback);
        profilTv = findViewById(R.id.profilIv);
        anggotakoperasitv = findViewById(R.id.anggotakoperasi);
        keluar = findViewById(R.id.keluar_koperasi);
        terima = findViewById(R.id.terimagabung);
        tolak = findViewById(R.id.tolakgabung);
        alamat = findViewById(R.id.alamat);
        umur = findViewById(R.id.umurtv);

        mAuth = FirebaseAuth.getInstance();

        peternakUid = getIntent().getStringExtra("peternakUid");
        anggotabaru = getIntent().getStringExtra("anggotabaru");
        validasi = getIntent().getStringExtra("validasi");
        koperasiname = getIntent().getStringExtra("koperasiname");

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (validasi != null && validasi.equals("pending")) {
            terima.setVisibility(View.VISIBLE);
            tolak.setVisibility(View.VISIBLE);
            keluar.setVisibility(View.INVISIBLE);
        } else if (validasi != null && validasi.equals("terima")) {
            terima.setVisibility(View.INVISIBLE);
            tolak.setVisibility(View.INVISIBLE);
            keluar.setVisibility(View.VISIBLE);
        } else {
            terima.setVisibility(View.INVISIBLE);
            tolak.setVisibility(View.INVISIBLE);
            keluar.setVisibility(View.INVISIBLE);
        }

        terima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terimajoin();
            }
        });
        tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tolakjoin();
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keluarkop();
            }
        });
        
        loadpeternakdetail();
    }

    private void keluarkop() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("anggotabaru", "");
        hashMap.put("anggotakoperasi", "");
        hashMap.put("validasi", "tolak");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Peternak");
        reference.child(peternakUid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PeternakJoinActivity.this, "Peternak di tolak", Toast.LENGTH_SHORT).show();
                terima.setVisibility(View.INVISIBLE);
                tolak.setVisibility(View.INVISIBLE);
                startActivity(new Intent(PeternakJoinActivity.this,DashboardKoperasiActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PeternakJoinActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void terimajoin() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("validasi", "terima");
        hashMap.put("anggotabaru","false");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Peternak");
        reference.child(peternakUid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PeternakJoinActivity.this, "Peternak di terima", Toast.LENGTH_SHORT).show();
                terima.setVisibility(View.INVISIBLE);
                tolak.setVisibility(View.INVISIBLE);
                startActivity(new Intent(PeternakJoinActivity.this,DashboardKoperasiActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PeternakJoinActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tolakjoin() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("anggotabaru", "false");
        hashMap.put("anggotakoperasi", "");
        hashMap.put("validasi", "tolak");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Peternak");
        reference.child(peternakUid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PeternakJoinActivity.this, "Peternak di tolak", Toast.LENGTH_SHORT).show();
                terima.setVisibility(View.INVISIBLE);
                tolak.setVisibility(View.INVISIBLE);
                startActivity(new Intent(PeternakJoinActivity.this,DashboardKoperasiActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PeternakJoinActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadpeternakdetail() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Peternak");
        ref.child(peternakUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get data from db
                String name = "" + dataSnapshot.child("name").getValue();
                String tempatlahir = "" + dataSnapshot.child("tempatlahir").getValue();
                String tanggallahir = "" + dataSnapshot.child("tanggallahir").getValue();
                String pendidikan = "" + dataSnapshot.child("pendidikan").getValue();
                String email = "" + dataSnapshot.child("email").getValue();
                String profilImage = "" + dataSnapshot.child("profil_image").getValue();
                String Umur = ""+dataSnapshot.child("umur").getValue();
                String AnggotaKoperasi = "" + dataSnapshot.child("anggotakoperasi").getValue();
                String lokasi1 = "" + dataSnapshot.child("negara").getValue();
                String lokasi2 = "" + dataSnapshot.child("provinsi").getValue();
                String lokasi3 = "" + dataSnapshot.child("kota").getValue();

                //set data to ui
                namatv.setText(name);
                tempattv.setText(tempatlahir + " , " + tanggallahir);
                emailtv.setText(email);
                pendidikantv.setText(pendidikan);
                umur.setText(Umur + " Tahun");
                anggotakoperasitv.setText(AnggotaKoperasi);
                alamat.setText(lokasi1 + "," + lokasi2 + "," + lokasi3);
                try {
                    Picasso.get().load(profilImage).placeholder(R.drawable.ic_account_circle_black_24dp).into(profilTv);
                } catch (Exception e) {
                    profilTv.setImageResource(R.drawable.ic_account_circle_black_24dp);
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
        Intent intent = new Intent(PeternakJoinActivity.this, DashboardKoperasiActivity.class);
        startActivity(intent);
        finish();
    }
}
