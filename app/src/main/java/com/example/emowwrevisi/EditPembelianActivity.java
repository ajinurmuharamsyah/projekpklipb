package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
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

public class EditPembelianActivity extends AppCompatActivity {

    private ImageButton btnback;
    private Button btnupdate;
    private TextView datetv,hasil,namapet,namakop;
    private EditText parameter,jumlah1,harga1;
    private AutoCompleteTextView satuan1;

    private DatePickerDialog.OnDateSetListener setListener;

    private String pembelianId, pembelianUid;

    private ProgressDialog progressDialog;
    //    private BroadcastReceiver broadcastReceiver;
    long now = System.currentTimeMillis() - 1000;
    private FirebaseAuth mAuth;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ArrayList<String> modeluidpet,modelnamepet,modeluidkop,modelnamekop;
    private String[] satuanb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pembelian);
        btnback = findViewById(R.id.btnback);
        btnupdate = findViewById(R.id.updatepembayaran);
        datetv = findViewById(R.id.datetv);
        namapet = findViewById(R.id.namapet);
        parameter = findViewById(R.id.parameterbar);
        jumlah1 = findViewById(R.id.jumlahba);
        satuan1 =findViewById(R.id.satuanbar);
        harga1 = findViewById(R.id.satuanhar);
        namakop = findViewById(R.id.namkop);
        hasil = findViewById(R.id.hasil1);

        mAuth = FirebaseAuth.getInstance();

        satuanb = getResources().getStringArray(R.array.satuanb);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, satuanb);
        satuan1.setAdapter(adapter1);

        Intent intent = getIntent();
        pembelianId = intent.getStringExtra("pembelianId");
        pembelianUid = intent.getStringExtra("pembelianuid");

        loadPembayaranDetail();

        //Setup ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        loadnamekop();
        loadnamekop2();

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
                startActivity(new Intent(EditPembelianActivity.this,PembelianPeternakActivity.class));
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
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data
                //validate data
                //update data to db
                inputData();
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
        builder.setTitle("Pilih Nama Koperasi")
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

    private void loadPembayaranDetail() {
        //get id of the product from intent
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pembelian");
        reference.child(pembelianId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get data
                String pembayaranId = ""+dataSnapshot.child("pembayaranId").getValue();
                String tanggal= ""+dataSnapshot.child("tanggal").getValue();
                String namapeternak = ""+dataSnapshot.child("namapeternak").getValue();
                String namakoperasi= ""+dataSnapshot.child("namakoperasi").getValue();
                String parameterBarang = ""+dataSnapshot.child("parameterBarang").getValue();
                String jumlah = ""+dataSnapshot.child("jumlah").getValue();
                String satuan = ""+dataSnapshot.child("satuan").getValue();
                String harga = ""+dataSnapshot.child("harga").getValue();
                String uid = ""+dataSnapshot.child("uid").getValue();

                datetv.setText(tanggal);
                namapet.setText(namapeternak);
                namakop.setText(namakoperasi);
                parameter.setText(parameterBarang);
                jumlah1.setText(jumlah);
                satuan1.setText(satuan);
                harga1.setText(harga);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String Peternak,Parameter,Jumlah,Tanggal,Satuan,Harga,Koperasi,Hasil;

    private void inputData() {
        Peternak = namapet.getText().toString().trim();
        Parameter = parameter.getText().toString().trim();
        Jumlah = jumlah1.getText().toString().trim();
        Tanggal = datetv.getText().toString().trim();
        Satuan = satuan1.getText().toString().trim();
        Harga = harga1.getText().toString().trim();
        Koperasi = namakop.getText().toString().trim();
        Hasil = hasil.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(selectedPet)){
            Toast.makeText(this, "Nama Peternak harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(selectedKop)){
            Toast.makeText(this, "Nama Koperasi harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Parameter)){
            Toast.makeText(this, "Parameter Baran harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Jumlah)){
            Toast.makeText(this, "Jumlah Barang harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Tanggal)){
            Toast.makeText(this, "Tanggal Pembelian harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Satuan)){
            Toast.makeText(this, "Satuan Barang harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Harga)){
            Toast.makeText(this, "Harga Barang harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else {
            int jum = Integer.parseInt(jumlah1.getText().toString());
            int har = Integer.parseInt(harga1.getText().toString());
            Hasil = String.valueOf(jum * har);

            updatePembayaran();
//            updatePembayaran2();
        }

    }

    private void updatePembayaran2() {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("totalpembelian",""+Hasil);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoice");
        databaseReference.child(mAuth.getUid()).updateChildren(hashMap);
    }

    private void updatePembayaran() {
        //show progress
        progressDialog.setMessage("Pembelian update...");
        progressDialog.show();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("tanggal","" +Tanggal);
        hashMap.put("namapeternak","" +selectedPet);
        hashMap.put("namakoperasi","" +selectedKop);
        hashMap.put("parameter","" +Tanggal);
        hashMap.put("tanggal","" +Tanggal);
        hashMap.put("parameterBarang",""+Parameter);
        hashMap.put("jumlah",""+Jumlah);
        hashMap.put("satuan",""+Satuan);
        hashMap.put("harga",""+Harga);
        hashMap.put("total",""+Hasil);

        //update to db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pembelian");
        reference.child(pembelianId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //update success
                progressDialog.dismiss();
                Toast.makeText(EditPembelianActivity.this, "Pembelian Update...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditPembelianActivity.this,PembelianPeternakActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //update failed
                progressDialog.dismiss();
                Toast.makeText(EditPembelianActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent (EditPembelianActivity.this,PembelianPeternakActivity.class);
        startActivity(intent);
        finish();
    }
}
