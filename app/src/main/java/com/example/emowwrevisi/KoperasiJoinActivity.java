package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class KoperasiJoinActivity extends AppCompatActivity {
    private ImageButton btnback;
    private TextView namakop,ownertv,berdiritv,emailtv,sekoperasi,lokoperasi;

    private ImageView profilTv;
    private Button gabung,batalgabung;

    private LayoutInflater inflater;
    private AlertDialog.Builder reset_alert;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    
    private FirebaseAuth mAuth;
    
    private String koperasiUid, koperasiname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koperasi_join);
        namakop = findViewById(R.id.namakop);
        ownertv = findViewById(R.id.ownertv);
        berdiritv = findViewById(R.id.berdiritv);
        emailtv = findViewById(R.id.emailtv);
        sekoperasi = findViewById(R.id.sekoperasitv);
        lokoperasi = findViewById(R.id.lokoperasitv);
        btnback = findViewById(R.id.btnback);
        profilTv = findViewById(R.id.profilIv);
        gabung = findViewById(R.id.gabung);
        btnback = findViewById(R.id.btnback);
        batalgabung = findViewById(R.id.batalgabung);
        
        mAuth = FirebaseAuth.getInstance();

        //get data from intent
        Intent intent = getIntent();
        koperasiUid = intent.getStringExtra("Uid");
        koperasiname = intent.getStringExtra("koperasiname");
        
        loadmykoperasi();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        gabung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnjoin();
            }
        });
    }

    private void loadmykoperasi() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Koperasi");
        reference.child(koperasiUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = ""+snapshot.child("owner").getValue();
                String namakoperasi = ""+snapshot.child("koperasiname").getValue();
                String berdiri = ""+snapshot.child("berdiri").getValue();
                String email = ""+snapshot.child("email").getValue();
                String profil_image = ""+snapshot.child("profil_image").getValue();
                String sejarah = ""+snapshot.child("sejarah").getValue();
                String lokasi1 = ""+snapshot.child("negara").getValue();
                String lokasi2 = ""+snapshot.child("provinsi").getValue();
                String lokasi3 = ""+snapshot.child("kota").getValue();

                namakop.setText(namakoperasi);
                ownertv.setText(name);
                berdiritv.setText(berdiri);
                emailtv.setText(email);
                sekoperasi.setText(sejarah);
                lokoperasi.setText(lokasi1+","+lokasi2+","+lokasi3);

                try {
                    Picasso.get().load(profil_image).placeholder(R.drawable.ic_account_circle_black_24dp).into(profilTv);
                } catch (Exception e){
                    profilTv.setImageResource(R.drawable.ic_account_circle_black_24dp);
                }
                tess(namakoperasi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void tess(final String namakoperasi) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Peternak");
        reference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String validasi = ""+ snapshot.child("validasi").getValue();
                String Anggota = ""+snapshot.child("AnggotaKoperasi").getValue();
                if (validasi != null &&validasi.equals("")){
                    gabung.setVisibility(View.VISIBLE);
                    batalgabung.setVisibility(View.INVISIBLE);
                    btnjoin();
                }else if (validasi != null &&validasi.equals("pending")){
                    gabung.setVisibility(View.INVISIBLE);
                    batalgabung.setVisibility(View.VISIBLE);
                    btnbataljoin();
                }else if (validasi != null &&validasi.equals("tolak")){
                    gabung.setVisibility(View.VISIBLE);
                    batalgabung.setVisibility(View.INVISIBLE);
                    btnjoin();
                }else if (validasi != null && validasi.equals("terima")){
                    gabung.setVisibility(View.INVISIBLE);
                    batalgabung.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void btnjoin() {
        gabung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_alert.setTitle("Yakin join ke koperasi ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inputjoin();
                            }
                        }).setNegativeButton("Tidak", null)
                        .create().show();
            }
        });

    }

    private void inputjoin() {
        // save info account without image
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("anggotakoperasi",""+koperasiname);
        hashMap.put("anggotabaru","true");
        hashMap.put("validasi", "pending");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Peternak");
        reference.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(KoperasiJoinActivity.this, "Tunggu Konfirmasi Koperasi", Toast.LENGTH_SHORT).show();
                batalgabung.setVisibility(View.VISIBLE);
                gabung.setVisibility(View.INVISIBLE);
                btnbataljoin();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(KoperasiJoinActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void btnbataljoin() {
        batalgabung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_alert.setTitle("Yakin batal join ke koperasi ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inputbataljoin();
                            }
                        }).setNegativeButton("Tidak", null)
                        .create().show();
            }
        });

    }

    private void inputbataljoin() {
        // save info account without image
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("anggotakoperasi","");
        hashMap.put("anggotabaru","false");
        hashMap.put("validasi", "");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Peternak");
        reference.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(KoperasiJoinActivity.this, "batal join koperasi", Toast.LENGTH_SHORT).show();
                gabung.setVisibility(View.VISIBLE);
                batalgabung.setVisibility(View.INVISIBLE);
                btnjoin();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(KoperasiJoinActivity.this,DasboardPeternakActivity.class));
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
