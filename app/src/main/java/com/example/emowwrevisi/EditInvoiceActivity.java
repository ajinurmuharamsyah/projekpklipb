package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EditInvoiceActivity extends AppCompatActivity {

    private ImageButton btnback;
    private Button btninvoice;
    private TextView datetv,totalpendapatan,namapet,namakop;
    private EditText totalpembelian, totalsetoran, totalkualitas;
    private String invoiceId;

    private DatePickerDialog.OnDateSetListener setListener;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;

    //    private BroadcastReceiver broadcastReceiver;
    long now = System.currentTimeMillis() - 1000;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private ArrayList<String> modeluidpet,modelnamepet,modeluidkop,modelnamekop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_invoice);
        btnback = findViewById(R.id.btnback);
        datetv = findViewById(R.id.datetv);
        namapet = findViewById(R.id.namapet);
        namakop = findViewById(R.id.namkop);
        totalpembelian = findViewById(R.id.totalpembelian);
        totalsetoran = findViewById(R.id.totalsetoran);
        totalkualitas = findViewById(R.id.totalkualitas);
        btninvoice = findViewById(R.id.btninvoice);
        totalpendapatan = findViewById(R.id.pendapatan);

        //Setup ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();

        invoiceId = getIntent().getStringExtra("invoiceId");
//        broadcastReceiver = new KoneksiInternet();
//        registoreNetworkBroadcast();

        loadInvoice();
        loadnamekop();
        loadnamekop2();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        datetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditInvoiceActivity.this
                        ,android.R.style.Theme_Holo_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMinDate(now);
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                datetv.setText(date);
            }
        };
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        namapet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picknampet();
            }
        });
        namakop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picknamkop();
            }
        });

        btninvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    private void loadnamekop2() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Koperasi");
        reference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String namekoperasi = ""+ ds.child("koperasiname").getValue();
                    loadnamepet(namekoperasi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String selectedKop,selectedUidKop,selectedPet,selectedUidPet;

    private void picknamkop() {
        //get string array of categories from arraylist
        String[] koperasiArray = new String[modelnamekop.size()];
        for (int i = 0; i< modelnamekop.size(); i++){
            koperasiArray[i] = modelnamekop.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Nama Koperasi")
                .setItems(koperasiArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item click
                        //get clicked item from list
                        selectedKop = modelnamekop.get(which);
                        selectedUidKop = modeluidkop.get(which);
                        //set to category textview
                        namakop.setText(selectedKop);
                    }
                })
                .show();
    }

    private void picknampet() {
        //get string array of categories from arraylist
        String[] peternakArray = new String[modelnamepet.size()];
        for (int i = 0; i< modelnamepet.size(); i++){
            peternakArray[i] = modelnamepet.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Nama Peternak")
                .setItems(peternakArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item click
                        //get clicked item from list
                        selectedPet = modelnamepet.get(which);
                        selectedUidPet = modeluidpet.get(which);
                        //set to category textview
                        namapet.setText(selectedPet);
                    }
                })
                .show();
    }

    private void loadnamepet(String namekoperasi) {
        modeluidpet = new ArrayList<>();
        modelnamepet = new ArrayList<>();

        //db reference to load categories... db > categories
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Peternak");
        reference.orderByChild("anggotakoperasi").equalTo(namekoperasi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modeluidpet.clear();//clear before adding data
                modelnamepet.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    //get id and title of category
                    String uidpet = ""+ds.child("uid").getValue();
                    String namapeternak = ""+ ds.child("name").getValue();

                    //add to respective arraylist
                    modeluidpet.add(uidpet);
                    modelnamepet.add(namapeternak);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadnamekop() {
        modeluidkop = new ArrayList<>();
        modelnamekop = new ArrayList<>();

        //db reference to load categories... db > categories
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Koperasi");
        reference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modeluidkop.clear();//clear before adding data
                modelnamekop.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    //get id and title of category
                    String uidkop = ""+ds.child("id").getValue();
                    String namekoperasi = ""+ ds.child("koperasiname").getValue();

                    //add to respective arraylist
                    modeluidkop.add(uidkop);
                    modelnamekop.add(namekoperasi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (EditInvoiceActivity.this,InvoiceActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadInvoice() {
        //get id of the product from intent
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Invoices");
        reference.child(invoiceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //get data
                String invoiceId = ""+dataSnapshot.child("invoiceId").getValue();
                String tanggal= ""+dataSnapshot.child("tanggal").getValue();
                String namapeternak = ""+dataSnapshot.child("namapeternak").getValue();
                String namakoperasi= ""+dataSnapshot.child("namakoperasi").getValue();
                String totalsetoran1 = ""+dataSnapshot.child("totalsetoran").getValue();
                String totalkualitas1 = ""+dataSnapshot.child("totalkualitas").getValue();
                String totalpembelian1 = ""+dataSnapshot.child("totalpembelian").getValue();
                String uid = ""+dataSnapshot.child("uid").getValue();

                datetv.setText(tanggal);
                namapet.setText(namapeternak);
                namakop.setText(namakoperasi);
                totalsetoran.setText(totalsetoran1);
                totalkualitas.setText(totalkualitas1);
                totalpembelian.setText(totalpembelian1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String Setoran, Kualitass, Pembelian, Peternak, Koperasi, Tanggal, Pendapatan;

    private void inputData() {
        Tanggal = datetv.getText().toString().trim();
        Setoran = totalsetoran.getText().toString().trim();
        Kualitass = totalkualitas.getText().toString().trim();
        Pembelian = totalpembelian.getText().toString().trim();
        Pendapatan = totalpendapatan.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(Tanggal)){
            Toast.makeText(this, "Tanggal harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(selectedPet)){
            Toast.makeText(this, "Nama Peternak harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(selectedKop)){
            Toast.makeText(this, "Nama Koperasi harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Setoran)){
            Toast.makeText(this, "Total Setoran harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Kualitass)){
            Toast.makeText(this, "Total Kualitas harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Pembelian)){
            Toast.makeText(this, "Total Pembelian harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else {
            int setoran = Integer.parseInt(totalsetoran.getText().toString());
            int kualitas = Integer.parseInt(totalkualitas.getText().toString());
            int pembelian = Integer.parseInt(totalpembelian.getText().toString());
            Pendapatan = String.valueOf((setoran+kualitas) - pembelian);

            tambahinvoice();
        }

    }

    private void tambahinvoice() {
        progressDialog.setMessage("Update Invoice...");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tanggal",""+Tanggal);
        hashMap.put("namapeternak",""+selectedPet);
        hashMap.put("namakoperasi",""+selectedKop);
        hashMap.put("totalpembelian",""+Pembelian);
        hashMap.put("totalkualitas",""+Kualitass);
        hashMap.put("totalsetoran",""+Setoran);
        hashMap.put("totalpendapatan",""+Pendapatan);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoices");
        databaseReference.child(invoiceId).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(EditInvoiceActivity.this, "Menyimpan data Invoice...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditInvoiceActivity.this,InvoiceActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(EditInvoiceActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

}
