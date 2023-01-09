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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.emowwrevisi.Adapter.AdapterInvoiceKop;
import com.example.emowwrevisi.Adapter.AdapterSetoranKop;
import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.example.emowwrevisi.Model.ModelInvoiceKop;
import com.example.emowwrevisi.Model.ModelInvoicePet;
import com.example.emowwrevisi.Model.ModelSetoranKop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvoiceActivity extends AppCompatActivity {

    private EditText searchEd;
    private ImageButton btnback, btntambah;
    private RecyclerView viewbayar;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    private ArrayList<ModelInvoiceKop> modelInvoiceKops;
    private AdapterInvoiceKop adapterInvoiceKop;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        searchEd = findViewById(R.id.search_barw);
        btnback = findViewById(R.id.btnback);
        viewbayar = findViewById(R.id.recle);
        btntambah = findViewById(R.id.tambahinvoice);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();

        loadAllInvoice();

        searchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterInvoiceKop.getFilter().filter(s);
                } catch (Exception e) {
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

        btntambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvoiceActivity.this,TambahInvoiceActivity.class));
                finish();
            }
        });
    }

    private void loadAllInvoice() {
        modelInvoiceKops = new ArrayList<>();

        //get all pembayaran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Invoices");
        reference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //before getting  reset list
                modelInvoiceKops.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelInvoiceKop modelInvoice = ds.getValue(ModelInvoiceKop.class);
                    modelInvoiceKops.add(modelInvoice);
                }
                //setup adapter
                adapterInvoiceKop = new AdapterInvoiceKop(InvoiceActivity.this, modelInvoiceKops);
                //set adapter
                viewbayar.setAdapter(adapterInvoiceKop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InvoiceActivity.this, DashboardKoperasiActivity.class);
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
