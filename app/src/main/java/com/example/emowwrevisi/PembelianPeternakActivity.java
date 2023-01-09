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

import com.example.emowwrevisi.Adapter.AdapterPembelianKop;
import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.example.emowwrevisi.Model.ModelPembelianKop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PembelianPeternakActivity extends AppCompatActivity {
    private EditText searchEd;
    private ImageButton btnback, tambahpembayaran;
    private RecyclerView viewbayar;

    private ProgressDialog progressDialog;

    private ArrayList<ModelPembelianKop> modelPembelianKops;
    private AdapterPembelianKop adapterPembelianKop;

    private FirebaseAuth mAuth;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian_peternak);
        searchEd = findViewById(R.id.search_barw);
        btnback = findViewById(R.id.btnback);
        tambahpembayaran = findViewById(R.id.tambahpem);
        viewbayar = findViewById(R.id.recle);

        mAuth = FirebaseAuth.getInstance();
        loadPembelian();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tambahpembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PembelianPeternakActivity.this,TambahPembelianActivity.class));
                finish();
            }
        });

        searchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterPembelianKop.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadPembelian() {
        modelPembelianKops = new ArrayList<>();

        //get all pembayaran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pembelian");
        reference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //before getting  reset list
                modelPembelianKops.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPembelianKop modelPembelianKop = ds.getValue(ModelPembelianKop.class);
                    modelPembelianKops.add(modelPembelianKop);
                }
                //setup adapter
                adapterPembelianKop = new AdapterPembelianKop(PembelianPeternakActivity.this, modelPembelianKops);
                //set adapter
                viewbayar.setAdapter(adapterPembelianKop);
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
        Intent intent = new Intent(PembelianPeternakActivity.this, DashboardKoperasiActivity.class);
        startActivity(intent);
        finish();
    }
}
