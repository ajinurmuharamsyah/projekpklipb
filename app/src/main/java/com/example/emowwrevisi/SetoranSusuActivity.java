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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.emowwrevisi.Adapter.AdapterSetoranKop;
import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.example.emowwrevisi.Model.ModelSetoranKop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SetoranSusuActivity extends AppCompatActivity {

    private EditText searchEd;
    private ImageButton btnback,tambahsetor;
    private RecyclerView viewbayar;

    private ProgressDialog progressDialog;

    private ArrayList<ModelSetoranKop> modelSetorans1;
    private AdapterSetoranKop adapterSetoran;

    private FirebaseAuth mAuth;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setoran_susu);
        tambahsetor = findViewById(R.id.tambahsetoran);
        searchEd = findViewById(R.id.search_barw);
        btnback = findViewById(R.id.btnback);
        viewbayar = findViewById(R.id.recle);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();

        loadallsetoran();

        searchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterSetoran.getFilter().filter(s);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tambahsetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetoranSusuActivity.this,TambahSetoranSusuActivity.class));
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

    private void loadallsetoran() {
        modelSetorans1 = new ArrayList<>();

        //get all setoran
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Setorans");
        reference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelSetorans1.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    //get data
                    ModelSetoranKop modelSetoranKop = ds.getValue(ModelSetoranKop.class);

                    //add to arraylist
                    modelSetorans1.add(modelSetoranKop);
                }
                //setup adapter
                adapterSetoran = new AdapterSetoranKop(SetoranSusuActivity.this, modelSetorans1);
                //set adapter
                viewbayar.setAdapter(adapterSetoran);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (SetoranSusuActivity.this,DashboardKoperasiActivity.class);
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
