package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emowwrevisi.Adapter.AdapterJoinPeternak;
import com.example.emowwrevisi.Adapter.AdapterListKoperasi;
import com.example.emowwrevisi.Adapter.AdapterMyKop;
import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.example.emowwrevisi.Model.ModelKoperasi;
import com.example.emowwrevisi.Model.ModelKoperasi2;
import com.example.emowwrevisi.Model.ModelPeternak;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class DasboardPeternakActivity extends AppCompatActivity {

    private TextView nameTv, emailpettv, tabhome, tabvalidasi, tabkoperasi;
    private ImageButton logoutBtn, settingbtn;
    private ImageView profilpet;
    private RelativeLayout homeui, validasiui, gabungui;
    private RecyclerView cekvalidasi, viewkoperasi, viewkoperasi1;
    private EditText searchkop;

    private ProgressDialog progressDialog;
    private long backPressedTime;
    private Toast backToast;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private FirebaseAuth mAuth;

    private AdapterListKoperasi adapterKoperasi;

    private ArrayList<ModelKoperasi2> koperasis2;
    private AdapterMyKop adapterMyKop;
    private ArrayList<ModelKoperasi> koperasis;

    private ArrayList<ModelPeternak> peternaks;
    private AdapterJoinPeternak adapterJoinKop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard_peternak);
        profilpet = findViewById(R.id.profilIv);
        emailpettv = findViewById(R.id.emailtv);
        nameTv = findViewById(R.id.namatv);
        logoutBtn = findViewById(R.id.btnLogout);
        tabhome = findViewById(R.id.tabhome);
        tabkoperasi = findViewById(R.id.tabgabung);
        tabvalidasi = findViewById(R.id.tabvalidasi);
        validasiui = findViewById(R.id.cekvalidasiUi);
        homeui = findViewById(R.id.homeUi);
        gabungui = findViewById(R.id.gabungui);
        viewkoperasi1 = findViewById(R.id.koperasi1Rl);
        settingbtn = findViewById(R.id.btnaccount);
        viewkoperasi = findViewById(R.id.koperasiRl);
        searchkop = findViewById(R.id.search_kop);
        cekvalidasi = findViewById(R.id.cekvalidasi);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();

        //tampilan awal
        datashowui();

        tabhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show koperasi ui
                datashowui();
            }
        });
        tabvalidasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show peternak ui
                koperasishowui();

            }
        });
        tabkoperasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gabungkopshowui();
            }
        });

        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DasboardPeternakActivity.this, SettingPeternakActivity.class));
                finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make offline
                //sign out
                //go to login activity
                makeMeOffline();

            }
        });

        searchkop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterKoperasi.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkuser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(DasboardPeternakActivity.this,LoginActivity.class));
            finish();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Peternak");
        ref.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data from db
                    String name = "" + ds.child("name").getValue();
                    String accountType = "" + ds.child("accountType").getValue();
                    String email = "" + ds.child("email").getValue();
                    String profil_image = "" + ds.child("profil_image").getValue();
                    String anggotakoperasi = "" + ds.child("anggotakoperasi").getValue();
                    String validasi = ""+ds.child("validasi").getValue();

                    //set data to ui
                    nameTv.setText(name);
                    emailpettv.setText(email);

                    try {
                        Picasso.get().load(profil_image).placeholder(R.drawable.ic_account_circle_black_24dp).into(profilpet);

                    } catch (Exception e) {
                        profilpet.setImageResource(R.drawable.ic_account_circle_black_24dp);

                    }
                    if (validasi.equals("")||validasi.equals("pending")||validasi.equals("tolak")){
                        viewkoperasi.setVisibility(View.INVISIBLE);
                    } else {
                        viewkoperasi.setVisibility(View.VISIBLE);
                        loadmykoperasi(anggotakoperasi);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadmykoperasi(final String anggotakoperasi) {
        koperasis2 = new ArrayList<>();

        //get all pembayaran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Koperasi");
        reference.orderByChild("koperasiname").equalTo(anggotakoperasi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //before getting  reset list
                koperasis2.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelKoperasi2 modelKoperasi = ds.getValue(ModelKoperasi2.class);
                    koperasis2.add(modelKoperasi);
                }
                //setup adapter
                adapterMyKop = new AdapterMyKop(DasboardPeternakActivity.this, koperasis2);
                //set adapter
                viewkoperasi.setAdapter(adapterMyKop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void makeMeOffline() {
        //after logging in, make user online
        progressDialog.setMessage("Logging Out...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Peternak");
        ref.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //update succesfully
                mAuth.signOut();
                progressDialog.dismiss();
                checkuser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed updating
                progressDialog.dismiss();
                Toast.makeText(DasboardPeternakActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void gabungkopshowui() {
        gabungui.setVisibility(View.VISIBLE);
        validasiui.setVisibility(View.INVISIBLE);
        homeui.setVisibility(View.INVISIBLE);

        tabkoperasi.setTextColor(getResources().getColor(R.color.black));
        tabkoperasi.setBackgroundResource(R.drawable.shape_tab);

        tabvalidasi.setTextColor(getResources().getColor(R.color.white));
        tabvalidasi.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tabhome.setTextColor(getResources().getColor(R.color.white));
        tabhome.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        loadinfokota();
    }

    private void loadinfokota() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Peternak");
        ref.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String kota = "" + ds.child("kota").getValue();
                    loadkoperasi(kota);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadkoperasi(final String kota) {
        koperasis = new ArrayList<>();

        //get all pembayaran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Koperasi");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //before getting  reset list
                koperasis.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelKoperasi modelKoperasi = ds.getValue(ModelKoperasi.class);
                    String kota1 = "" + ds.child("kota").getValue();
                    if (kota1.equals(kota)) {
                        koperasis.add(modelKoperasi);
                    }

                }
                //setup adapter
                adapterKoperasi = new AdapterListKoperasi(DasboardPeternakActivity.this, koperasis);
                //set adapter
                viewkoperasi1.setAdapter(adapterKoperasi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void datashowui() {
        validasiui.setVisibility(View.INVISIBLE);
        homeui.setVisibility(View.VISIBLE);
        gabungui.setVisibility(View.INVISIBLE);

        tabhome.setTextColor(getResources().getColor(R.color.black));
        tabhome.setBackgroundResource(R.drawable.shape_tab);

        tabvalidasi.setTextColor(getResources().getColor(R.color.white));
        tabvalidasi.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tabkoperasi.setTextColor(getResources().getColor(R.color.white));
        tabkoperasi.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        
        loadMyInfo();
    }

    private void koperasishowui() {
        homeui.setVisibility(View.INVISIBLE);
        gabungui.setVisibility(View.INVISIBLE);
        validasiui.setVisibility(View.VISIBLE);

        tabvalidasi.setTextColor(getResources().getColor(R.color.black));
        tabvalidasi.setBackgroundResource(R.drawable.shape_tab);

        tabhome.setTextColor(getResources().getColor(R.color.white));
        tabhome.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tabkoperasi.setTextColor(getResources().getColor(R.color.white));
        tabkoperasi.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        loadinfoname();
    }

    private void loadinfoname() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Peternak");
        ref.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name1 = "" + ds.child("name").getValue();
                    loadvalidasi(name1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadvalidasi(final String name1) {
        peternaks = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Peternak");
        reference.orderByChild("uid").equalTo(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = "" + ds.child("name").getValue();
                            ModelPeternak modelPeternak = ds.getValue(ModelPeternak.class);
                            if (name1.equals(name)) {
                                peternaks.add(modelPeternak);
                            }
                            adapterJoinKop = new AdapterJoinPeternak(DasboardPeternakActivity.this, peternaks);

                            cekvalidasi.setAdapter(adapterJoinKop);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
