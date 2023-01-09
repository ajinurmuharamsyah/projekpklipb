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
import android.view.View;
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

public class TambahInvoiceActivity extends AppCompatActivity {

    private ImageButton btnback;
    private Button btninvoice;
    private TextView datetv,totalpendapatan,namapet,namakop;
    private EditText totalpembelian, totalsetoran, totalkualitas;

    private DatePickerDialog.OnDateSetListener setListener;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    //    private BroadcastReceiver broadcastReceiver;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    long now = System.currentTimeMillis() - 1000;

    private ArrayList<String> modeluidpet,modelnamepet,modeluidkop,modelnamekop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_invoice);
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

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        loadnamekop();
        loadnamekop2();

        datetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TambahInvoiceActivity.this
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
                startActivity(new Intent(TambahInvoiceActivity.this,InvoiceActivity.class));
                finish();
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
            Toast.makeText(this, "Jumlah Setoran harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Kualitass)){
            Toast.makeText(this, "Jumlah Kualitas harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Pembelian)){
            Toast.makeText(this, "Jumlah Pembelian harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        int setoran = Integer.parseInt(totalsetoran.getText().toString());
        int kualitas = Integer.parseInt(totalkualitas.getText().toString());
        int pembelian = Integer.parseInt(totalpembelian.getText().toString());
        Pendapatan = String.valueOf((setoran+kualitas) - pembelian);

        tambahinvoice();

    }

    private void tambahinvoice() {
        progressDialog.setMessage("Menambahkan data invoice...");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("invoiceId",""+timestamp);
        hashMap.put("tanggal",""+Tanggal);
        hashMap.put("namapeternak",""+selectedPet);
        hashMap.put("namakoperasi",""+selectedKop);
        hashMap.put("totalpembelian",""+Pembelian);
        hashMap.put("totalkualitas",""+Kualitass);
        hashMap.put("totalsetoran",""+Setoran);
        hashMap.put("totalpendapatan",""+Pendapatan);
        hashMap.put("uid",""+mAuth.getUid());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoices");
        databaseReference.child(""+timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        startActivity(new Intent(TambahInvoiceActivity.this,InvoiceActivity.class));
                        finish();
                        Toast.makeText(TambahInvoiceActivity.this, "Menyimpan data Invoice...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(TambahInvoiceActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent (TambahInvoiceActivity.this,InvoiceActivity.class);
        startActivity(intent);
        finish();
    }
}
