package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.emowwrevisi.Adapter.AdapterInvoicePet;
import com.example.emowwrevisi.Adapter.AdapterKualitasPet;
import com.example.emowwrevisi.Adapter.AdapterPembelianKop;
import com.example.emowwrevisi.Adapter.AdapterPembelianPet;
import com.example.emowwrevisi.Adapter.AdapterSetoranKop;
import com.example.emowwrevisi.Adapter.AdapterSetoranPet;
import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.example.emowwrevisi.Model.ModelInvoicePet;
import com.example.emowwrevisi.Model.ModelKualitasPet;
import com.example.emowwrevisi.Model.ModelPembelianKop;
import com.example.emowwrevisi.Model.ModelPembelianPet;
import com.example.emowwrevisi.Model.ModelSetoranKop;
import com.example.emowwrevisi.Model.ModelSetoranPet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KoperasiDetailsActivity extends AppCompatActivity {
    private ImageButton btnback,btnkeluar;
    private EditText seset, sepem, sekuali, seinvo;
    private TextView tabsetoran, tabkualitas, tabpembayaran, tabinvoice;
    private ImageView profilTv;

    private String koperasiUid,koperasiname;

    private ArrayList<ModelSetoranPet> modelSetorans1;
    private AdapterSetoranPet adapterSetoran;

    private ArrayList<ModelKualitasPet> modelKualitasPets;
    private AdapterKualitasPet adapterKualitasPet;

    private ArrayList<ModelPembelianPet> modelPembelianPets;
    private AdapterPembelianPet adapterPembelianPet;

    private ArrayList<ModelInvoicePet> modelInvoicePets;
    private AdapterInvoicePet adapterInvoicePet;

    private RelativeLayout setoranui, kualitasui, pembayaranui, invoiceui;
    private RecyclerView viewsetoran, viewkualitas, viewpembayaran, viewinvoice;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koperasi_details);
        btnback = findViewById(R.id.btnback);
        tabsetoran = findViewById(R.id.tabsetorans);
        tabkualitas = findViewById(R.id.tabkusus);
        tabpembayaran = findViewById(R.id.tabpembayarans);
        tabinvoice = findViewById(R.id.tabinvoice);
        setoranui = findViewById(R.id.setoranUi);
        kualitasui = findViewById(R.id.kualitasUi);
        pembayaranui = findViewById(R.id.pembayaranUi);
        invoiceui = findViewById(R.id.invoiceUi);
        viewsetoran = findViewById(R.id.setoranRl);
        viewkualitas = findViewById(R.id.kualitasRl);
        viewpembayaran = findViewById(R.id.pembayaranRl);
        viewinvoice = findViewById(R.id.invoiceRl);
        seset = findViewById(R.id.search_set);
        sepem = findViewById(R.id.search_pem);
        sekuali = findViewById(R.id.search_kual);
        seinvo = findViewById(R.id.search_invoi);

        koperasiUid = getIntent().getStringExtra("Uid");
        koperasiname = getIntent().getStringExtra("koperasiname");

        showSetoranUi();

        tabinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInvoiceUi();
            }
        });

        tabsetoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetoranUi();
            }
        });

        tabkualitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKualitasUi();
            }
        });

        tabpembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPembayaran();
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        seset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterSetoran.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sekuali.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterKualitasPet.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        sepem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterPembelianPet.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        seinvo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterInvoicePet.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

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
        Intent intent = new Intent(KoperasiDetailsActivity.this, DasboardPeternakActivity.class);
        startActivity(intent);
        finish();
    }

    private void showInvoiceUi() {
        invoiceui.setVisibility(View.VISIBLE);
        pembayaranui.setVisibility(View.GONE);
        setoranui.setVisibility(View.GONE);
        kualitasui.setVisibility(View.GONE);

        tabinvoice.setTextColor(getResources().getColor(R.color.black));
        tabinvoice.setBackgroundResource(R.drawable.shape_tab);

        tabpembayaran.setTextColor(getResources().getColor(R.color.white));
        tabpembayaran.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabsetoran.setTextColor(getResources().getColor(R.color.white));
        tabsetoran.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabkualitas.setTextColor(getResources().getColor(R.color.white));
        tabkualitas.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        loadInvoices();
    }

    private void loadInvoices() {
        modelInvoicePets = new ArrayList<>();

        //get all pembayaran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Invoices");
        reference.orderByChild("uid").equalTo(koperasiUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //before getting  reset list
                modelInvoicePets.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelInvoicePet modelInvoicePet = ds.getValue(ModelInvoicePet.class);
                    modelInvoicePets.add(modelInvoicePet);
                }
                //setup adapter
                adapterInvoicePet = new AdapterInvoicePet(KoperasiDetailsActivity.this, modelInvoicePets);
                //set adapter
                viewinvoice.setAdapter(adapterInvoicePet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showPembayaran() {
        pembayaranui.setVisibility(View.VISIBLE);
        setoranui.setVisibility(View.GONE);
        kualitasui.setVisibility(View.GONE);
        invoiceui.setVisibility(View.GONE);

        tabpembayaran.setTextColor(getResources().getColor(R.color.black));
        tabpembayaran.setBackgroundResource(R.drawable.shape_tab);

        tabsetoran.setTextColor(getResources().getColor(R.color.white));
        tabsetoran.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabkualitas.setTextColor(getResources().getColor(R.color.white));
        tabkualitas.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabinvoice.setTextColor(getResources().getColor(R.color.white));
        tabinvoice.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        loadPembelian();
    }

    private void loadPembelian() {
        modelPembelianPets = new ArrayList<>();

        //get all pembayaran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pembelian");
        reference.orderByChild("uid").equalTo(koperasiUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //before getting  reset list
                modelPembelianPets.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPembelianPet modelPembelianPet = ds.getValue(ModelPembelianPet.class);
                    modelPembelianPets.add(modelPembelianPet);
                }
                //setup adapter
                adapterPembelianPet = new AdapterPembelianPet(KoperasiDetailsActivity.this, modelPembelianPets);
                //set adapter
                viewpembayaran.setAdapter(adapterPembelianPet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showKualitasUi() {
        kualitasui.setVisibility(View.VISIBLE);
        setoranui.setVisibility(View.GONE);
        pembayaranui.setVisibility(View.GONE);
        invoiceui.setVisibility(View.GONE);

        tabkualitas.setTextColor(getResources().getColor(R.color.black));
        tabkualitas.setBackgroundResource(R.drawable.shape_tab);

        tabsetoran.setTextColor(getResources().getColor(R.color.white));
        tabsetoran.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabpembayaran.setTextColor(getResources().getColor(R.color.white));
        tabpembayaran.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabinvoice.setTextColor(getResources().getColor(R.color.white));
        tabinvoice.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        loadallkualitassusu();
    }

    private void loadallkualitassusu() {
        modelKualitasPets = new ArrayList<>();

        //get all setoran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kualitas");
        reference.orderByChild("uid").equalTo(koperasiUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelKualitasPets.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    //get data
                    ModelKualitasPet modelKualitasPet = ds.getValue(ModelKualitasPet.class);

                    //add to arraylist
                    modelKualitasPets.add(modelKualitasPet);
                }
                //setup adapter
                adapterKualitasPet = new AdapterKualitasPet(KoperasiDetailsActivity.this, modelKualitasPets);
                //set adapter
                viewkualitas.setAdapter(adapterKualitasPet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSetoranUi() {
        setoranui.setVisibility(View.VISIBLE);
        kualitasui.setVisibility(View.GONE);
        pembayaranui.setVisibility(View.GONE);
        invoiceui.setVisibility(View.GONE);

        tabsetoran.setTextColor(getResources().getColor(R.color.black));
        tabsetoran.setBackgroundResource(R.drawable.shape_tab);

        tabkualitas.setTextColor(getResources().getColor(R.color.white));
        tabkualitas.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabpembayaran.setTextColor(getResources().getColor(R.color.white));
        tabpembayaran.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabinvoice.setTextColor(getResources().getColor(R.color.white));
        tabinvoice.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        loadallsetoransusu();
    }

    private void loadallsetoransusu() {
        modelSetorans1 = new ArrayList<>();

        //get all setoran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Setorans");
        reference.orderByChild("uid").equalTo(koperasiUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelSetorans1.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    //get data
                    ModelSetoranPet modelSetoranPet = ds.getValue(ModelSetoranPet.class);

                    //add to arraylist
                    modelSetorans1.add(modelSetoranPet);
                }
                //setup adapter
                adapterSetoran = new AdapterSetoranPet(KoperasiDetailsActivity.this, modelSetorans1);
                //set adapter
                viewsetoran.setAdapter(adapterSetoran);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
