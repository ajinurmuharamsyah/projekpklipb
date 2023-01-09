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

public class EditSetoranSusuActivity extends AppCompatActivity {
    private ImageButton btnback;
    private Button btnsetoran;
    private TextView datetv,jumlahsusu1,totalsusu1, namapet,namakop;
    private EditText suspagi1,sussore1,harga1,jumlahsusu;
    private AutoCompleteTextView jamsetoran;
    private String item;

    private DatePickerDialog.OnDateSetListener setListener;

    private FirebaseAuth mAuth;
    private String setoranId, setoranUid;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private ProgressDialog progressDialog;
    private String[] susu = {"Pagi","Sore","Pagi dan Sore"};
    long now = System.currentTimeMillis() - 1000;

    private ArrayList<String> modeluidpet,modelnamepet,modeluidkop,modelnamekop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_setoran_susu);

        btnback = findViewById(R.id.btnback);
        datetv = findViewById(R.id.datetv);
        namapet = findViewById(R.id.namapet);
        namakop = findViewById(R.id.namkop);
        jamsetoran = findViewById(R.id.jumsupa);
        jumlahsusu = findViewById(R.id.jumsusore);
        harga1 = findViewById(R.id.hargasatuan1);
        totalsusu1 = findViewById(R.id.hasil2);
        btnsetoran = findViewById(R.id.btnsetoran);

        mAuth = FirebaseAuth.getInstance();

        //Setup ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.listsetoran, susu);
        jamsetoran.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        setoranId = intent.getStringExtra("setoranId");
        setoranUid = intent.getStringExtra("setoranuid");

        loadnamekop();
        loadnamekop2();
        loadSetoranSusu1();

        datetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditSetoranSusuActivity.this
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

        btnsetoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
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

    private String Peternak,Tanggal,Koperasi,Jumlah,Total,Pagi,Sore,Harga,JamSetoran;

    private void inputData() {
        Peternak = namapet.getText().toString().trim();
        Tanggal = datetv.getText().toString().trim();
        Koperasi = namakop.getText().toString().trim();
        Jumlah = jumlahsusu.getText().toString().trim();
        Total = totalsusu1.getText().toString().trim();
        JamSetoran = jamsetoran.getText().toString().trim();
        Harga = harga1.getText().toString().trim();


        //validate data
        if (TextUtils.isEmpty(selectedPet)){
            Toast.makeText(this, "Nama Peternak harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(selectedKop)){
            Toast.makeText(this, "Nama Koperasi harus diisi", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Jumlah)){
            Toast.makeText(this, "Jumlah Setoran Susu harus diisi", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Tanggal)){
            Toast.makeText(this, "Tanggal Setoran harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Harga)){
            Toast.makeText(this, "Harga Barang harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(JamSetoran)){
            Toast.makeText(this, "Jam Setoran harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }else {
            int jum = Integer.parseInt(Jumlah);
            int har = Integer.parseInt(Harga);
            Total = String.valueOf(jum * har);

            updatesetoran();
//            updatesetoran2();
        }
    }

    private void updatesetoran2() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tanggal",""+Tanggal);
        hashMap.put("namapeternak",""+selectedPet);
        hashMap.put("namakoperasi",""+selectedKop);
        hashMap.put("totalsetoran",""+Total);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Invoices");
        databaseReference.child(setoranId).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(EditSetoranSusuActivity.this, "Menyimpan data setoran susu...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditSetoranSusuActivity.this,SetoranSusuActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(EditSetoranSusuActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatesetoran() {
        progressDialog.setMessage("Update data setoran susu...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tanggal",""+Tanggal);
        hashMap.put("namapeternak",""+selectedPet);
        hashMap.put("namakoperasi",""+selectedKop);
        hashMap.put("jamsetoran",""+JamSetoran);
        hashMap.put("hargaSusu",""+Harga);
        hashMap.put("jumlahSusu",""+Jumlah);
        hashMap.put("total",""+Total);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Setorans");
        databaseReference.child(setoranId).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(EditSetoranSusuActivity.this, "Menyimpan data setoran susu...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditSetoranSusuActivity.this,SetoranSusuActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(EditSetoranSusuActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSetoranSusu1() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Setorans");
        reference.child(setoranId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get data
                String setoranIds = ""+dataSnapshot.child("setoranId").getValue();
                String tanggal= ""+dataSnapshot.child("tanggal").getValue();
                String namapeternak = ""+dataSnapshot.child("namapeternak").getValue();
                String namakoperasi= ""+dataSnapshot.child("namakoperasi").getValue();
                String jamsetoran1 = ""+dataSnapshot.child("jamsetoran").getValue();
                String hargaSusu = ""+dataSnapshot.child("hargaSusu").getValue();
                String jumlahSusu = ""+dataSnapshot.child("jumlahSusu").getValue();
                String total = ""+dataSnapshot.child("total").getValue();
                String uid = ""+dataSnapshot.child("uid").getValue();

                datetv.setText(tanggal);
                namakop.setText(namakoperasi);
                namapet.setText(namapeternak);
                jumlahsusu.setText(jumlahSusu);
                harga1.setText(hargaSusu);
                jamsetoran.setText(jamsetoran1);
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
        Intent intent = new Intent (EditSetoranSusuActivity.this,SetoranSusuActivity.class);
        startActivity(intent);
        finish();
    }
}
