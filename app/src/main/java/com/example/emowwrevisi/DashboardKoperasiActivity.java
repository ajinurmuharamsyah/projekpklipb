package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emowwrevisi.Adapter.AdapterAproveKop;
import com.example.emowwrevisi.Adapter.AdapterJoinPeternak;
import com.example.emowwrevisi.Internat.NetworkChangeListener;
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

public class DashboardKoperasiActivity extends AppCompatActivity {

    private CardView btnsetor,btnkualitas,btnpembayaran,btninvoice;
    private TextView nameTv,koptv,emailkoptv,tabhome,tabpeternak;
    private ImageButton logoutBtn,editprofil;
    private RelativeLayout peternakui;
    private LinearLayout homeui;
    private RecyclerView peternakRl;
    private ImageView profilkop,baruanggota;

    private ProgressDialog progressDialog;

    private long backPressedTime;
    private Toast backToast;

    private ArrayList<ModelPeternak> peternaks;
    private AdapterAproveKop adapterAproveKop;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_koperasi);
        profilkop = findViewById(R.id.profilIv);
        nameTv = findViewById(R.id.namatv);
        koptv = findViewById(R.id.kopetv);
        emailkoptv = findViewById(R.id.emailtv);
        logoutBtn = findViewById(R.id.btnLogout);
        editprofil = findViewById(R.id.btnaccount);
        tabhome = findViewById(R.id.tabhome);
        tabpeternak = findViewById(R.id.tabpeternak);
        homeui = findViewById(R.id.homeUi);
        peternakui = findViewById(R.id.peternakUi);
        peternakRl = findViewById(R.id.peternakRl);
        btnsetor = findViewById(R.id.ivsetor);
        btnkualitas = findViewById(R.id.ivkualitas);
        btnpembayaran = findViewById(R.id.ivbayar);
        btninvoice = findViewById(R.id.ivinvoice);
        baruanggota = findViewById(R.id.baruanggota1);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();

        //tampil awal
        homeuishow();

        btninvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardKoperasiActivity.this,InvoiceActivity.class));
                finish();
            }
        });

        btnsetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardKoperasiActivity.this,SetoranSusuActivity.class));
                finish();
            }
        });
        btnkualitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardKoperasiActivity.this,KualitasSusuActivity.class));
                finish();
            }
        });
        btnpembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardKoperasiActivity.this,PembelianPeternakActivity.class));
                finish();
            }
        });

        tabhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show koperasi ui
                homeuishow();
            }
        });
        tabpeternak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show peternak ui
                peternakshowui();
//                validasicek();

            }
        });

        editprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardKoperasiActivity.this,SettingKoperasiActivity.class));
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
    }

    private void makeMeOffline() {
        mAuth = FirebaseAuth.getInstance();
        //after logging in, make user online
        progressDialog.setMessage("Logging Out...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online","false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Koperasi");
        ref.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //update succesfully
                progressDialog.dismiss();
                mAuth.signOut();
                checkuser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed updating
                progressDialog.dismiss();
                Toast.makeText(DashboardKoperasiActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkuser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(DashboardKoperasiActivity.this,LoginActivity.class));
            finish();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Koperasi");
        ref.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //get data from db
                    String name =""+ds.child("owner").getValue();
                    String accountType =""+ds.child("accountType").getValue();
                    String koperasiname = ""+ds.child("koperasiname").getValue();
                    String email = ""+ds.child("email").getValue();
                    String profil_image =""+ds.child("profil_image").getValue();

                    //set data to ui
                    nameTv.setText(name);
                    koptv.setText(koperasiname);
                    emailkoptv.setText(email);

                    try {
                        Picasso.get().load(profil_image).placeholder(R.drawable.ic_account_circle_black_24dp).into(profilkop);

                    } catch (Exception e){
                        profilkop.setImageResource(R.drawable.ic_account_circle_black_24dp);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    
    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(), "Tekan Kembali untuk Keluar Aplikasi", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void peternakshowui() {
        homeui.setVisibility(View.INVISIBLE);
        peternakui.setVisibility(View.VISIBLE);

        tabpeternak.setTextColor(getResources().getColor(R.color.black));
        tabpeternak.setBackgroundResource(R.drawable.shape_tab);

        tabhome.setTextColor(getResources().getColor(R.color.white));
        tabhome.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        loadinfokop();
    }

    private void loadinfokop() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Koperasi");
        ref.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //get data from db
                    String koperasiname = ""+ds.child("koperasiname").getValue();
                    loadMyPeternak(koperasiname);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMyPeternak(String koperasiname) {
        peternaks = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Peternak");
        reference.orderByChild("anggotakoperasi").equalTo(koperasiname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //before getting  reset list
                peternaks.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelPeternak modelPeternak = ds.getValue(ModelPeternak.class);
                    String anggotakoperasi1 = "" + ds.child("AnggotaKoperasi").getValue();
                    String validasi = "" + ds.child("validasi").getValue();
                    peternaks.add(modelPeternak);
                    if (validasi.equals("pending")) {
                        baruanggota.setVisibility(View.VISIBLE);
                    } else if (validasi.equals("tolak") || validasi.equals("") || validasi.equals("terima")) {
                        baruanggota.setVisibility(View.INVISIBLE);
                    }

                }
                //setup adapter
                adapterAproveKop = new AdapterAproveKop(DashboardKoperasiActivity.this, peternaks);
                //set adapter
                peternakRl.setAdapter(adapterAproveKop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void homeuishow() {
        homeui.setVisibility(View.VISIBLE);
        peternakui.setVisibility(View.INVISIBLE);

        tabhome.setTextColor(getResources().getColor(R.color.black));
        tabhome.setBackgroundResource(R.drawable.shape_tab);

        tabpeternak.setTextColor(getResources().getColor(R.color.white));
        tabpeternak.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        loadMyInfo();
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
