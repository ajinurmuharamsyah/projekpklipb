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

public class EditKualitasSusuActivity extends AppCompatActivity {

    private ImageButton btnback;
    private Button btnkualitas;
    private TextView datetv,jumlahkualitas, namapet,namakop;
    private EditText hasil1,bonus1;
    private AutoCompleteTextView parameterkul,satuankul;

    private DatePickerDialog.OnDateSetListener setListener;

    private String kualitasId, kualitasUid;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    //    private BroadcastReceiver broadcastReceiver;
    long now = System.currentTimeMillis() - 1000;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ArrayList<String> modeluidpet,modelnamepet,modeluidkop,modelnamekop;
    private String[] parametersusu, parametersatuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kualitas_susu);
        btnback = findViewById(R.id.btnback);
        datetv = findViewById(R.id.datetv);
        namapet = findViewById(R.id.namapet);
        namakop = findViewById(R.id.namkop);
        parameterkul = findViewById(R.id.parameterkul);
        satuankul = findViewById(R.id.satuankul);
        hasil1 = findViewById(R.id.hasil1);
        bonus1 = findViewById(R.id.bonus1);
        jumlahkualitas = findViewById(R.id.jumlahkualitas1);
        btnkualitas = findViewById(R.id.btnkualitas);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        kualitasId = intent.getStringExtra("kualitasId");
        kualitasUid = intent.getStringExtra("kualitasUid");

        //Setup ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        parametersusu = getResources().getStringArray(R.array.parametersusu);
        parametersatuan = getResources().getStringArray(R.array.parametersatuan);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, parametersusu);
        parameterkul.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, parametersatuan);
        satuankul.setAdapter(adapter2);

        loadnamekop();
        loadnamekop2();
        loadKualitas();

        datetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditKualitasSusuActivity.this
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

        btnkualitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private String Peternak,Tanggal,Koperasi,Hasil,Bonus,Parameter,Satuan,Jumlah;

    private void inputData() {
        Tanggal = datetv.getText().toString().trim();
        Hasil = hasil1.getText().toString().trim();
        Bonus = bonus1.getText().toString().trim();
        Parameter = parameterkul.getText().toString().trim();
        Satuan = satuankul.getText().toString().trim();
        Jumlah = jumlahkualitas.getText().toString().trim();

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
        }else if (TextUtils.isEmpty(Parameter)){
            Toast.makeText(this, "Parameter harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Satuan)){
            Toast.makeText(this, "Satuan harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Hasil)){
            Toast.makeText(this, "Hasil harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Bonus)){
            Toast.makeText(this, "Bonus harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else {
            double jum = Double.parseDouble(hasil1.getText().toString());
            int har = Integer.parseInt(bonus1.getText().toString());
            Jumlah = String.valueOf(har * jum / 100);

            updatekualitas();
            updatekualitas2();
        }

    }

    private void updatekualitas2() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("jumlahkualitas",""+Bonus);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoice");
        databaseReference.child(kualitasId).updateChildren(hashMap);
    }

    private void updatekualitas() {
        progressDialog.setMessage("Mengupdate data kualitas susu...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tanggal",""+Tanggal);
        hashMap.put("namapeternak",""+selectedPet);
        hashMap.put("namakoperasi",""+selectedKop);
        hashMap.put("parameterkualitas",""+Parameter);
        hashMap.put("satuankualitas",""+Satuan);
        hashMap.put("hasil",""+Hasil);
        hashMap.put("bonus",""+Bonus);
        hashMap.put("jumlahkualitas",""+Bonus);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Kualitas");
        databaseReference.child(kualitasId).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(EditKualitasSusuActivity.this, "Menyimpan data kualitas susu...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditKualitasSusuActivity.this,KualitasSusuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(EditKualitasSusuActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadKualitas() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kualitas");
        reference.child(kualitasId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get data
                String kualitasId = ""+dataSnapshot.child("kualitasId").getValue();
                String tanggal= ""+dataSnapshot.child("tanggal").getValue();
                String namapeternak = ""+dataSnapshot.child("namapeternak").getValue();
                String namakoperasi= ""+dataSnapshot.child("namakoperasi").getValue();
                String parameter = ""+dataSnapshot.child("parameterkualitas").getValue();
                String satuan = ""+dataSnapshot.child("satuankualitas").getValue();
                String hasil = ""+dataSnapshot.child("hasil").getValue();
                String bonus = ""+dataSnapshot.child("bonus").getValue();
                String uid = ""+dataSnapshot.child("uid").getValue();

                datetv.setText(tanggal);
                namakop.setText(namakoperasi);
                namapet.setText(namapeternak);
                parameterkul.setText(parameter);
                satuankul.setText(satuan);
                hasil1.setText(hasil);
                bonus1.setText(bonus);
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
        Intent intent = new Intent (EditKualitasSusuActivity.this,KualitasSusuActivity.class);
        startActivity(intent);
        finish();
    }
}
