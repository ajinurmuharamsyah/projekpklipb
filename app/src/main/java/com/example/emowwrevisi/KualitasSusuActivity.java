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

import com.example.emowwrevisi.Adapter.AdapterKualitasKop;
import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.example.emowwrevisi.Model.ModelKualitasKop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KualitasSusuActivity extends AppCompatActivity {
    private EditText searchEd;
    private ImageButton btnback,btnkualitas;
    private RecyclerView viewbayar;

    private ProgressDialog progressDialog;

    private ArrayList<ModelKualitasKop> modelKualitasKops;
    private AdapterKualitasKop adapterKualitasKop;

    private FirebaseAuth mAuth;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kualitas_susu);
        btnback = findViewById(R.id.btnback);
        btnkualitas = findViewById(R.id.tambahkualitas);
        searchEd = findViewById(R.id.search_barw);
        viewbayar = findViewById(R.id.recle);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();
        
        loadallkualitas();

        searchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterKualitasKop.getFilter().filter(s);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnkualitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KualitasSusuActivity.this,TambahKualitasSusuActivity.class));
                finish();
            }
        });
    }

    private void loadallkualitas() {
        modelKualitasKops = new ArrayList<>();

        //get all pembayaran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kualitas");
        reference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //before getting  reset list
                modelKualitasKops.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelKualitasKop modelKualitas1 = ds.getValue(ModelKualitasKop.class);
                    modelKualitasKops.add(modelKualitas1);
                }
                //setup adapter
                adapterKualitasKop = new AdapterKualitasKop(KualitasSusuActivity.this, modelKualitasKops);
                //set adapter
                viewbayar.setAdapter(adapterKualitasKop);
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
        Intent intent = new Intent (KualitasSusuActivity.this,DashboardKoperasiActivity.class);
        startActivity(intent);
        finish();
    }

}
