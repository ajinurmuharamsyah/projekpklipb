package com.example.emowwrevisi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emowwrevisi.Internat.NetworkChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class ProfilPeternakActivity extends AppCompatActivity {
    private EditText namleng, tempatlahir, umur;
    private TextView tanggallahir;
    private Button btnupdate;
    private ImageView gamprofil;
    private ImageButton btnback;
    private AutoCompleteTextView lokasi1, lokasi2, lokasi3, pendidikan1;
    private String[] negara;
    private String[] kota;
    private String[] provinsi;
    private String[] kota1;
    private String[] pendidikan;

    //permission constants
    private static final int STORAGE_REQUEST_CODE = 100;

    //images pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 200;

    private String[] storagePermission;

    //images picked uri
    private Uri image_uri;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    private DatePickerDialog.OnDateSetListener setListener;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_peternak);
        gamprofil = findViewById(R.id.image4);
        namleng = findViewById(R.id.profilnama);
        tempatlahir = findViewById(R.id.profiltempat);
        tanggallahir = findViewById(R.id.profiltanggal);
        pendidikan1 = findViewById(R.id.profilpendidikan);
        btnupdate = findViewById(R.id.updateprofil);
        btnback = findViewById(R.id.btnback);
        lokasi1 = findViewById(R.id.profilNegara);
        lokasi2 = findViewById(R.id.profilprovinsi);
        lokasi3 = findViewById(R.id.profilkota);
        umur = findViewById(R.id.registerumur);

        negara = getResources().getStringArray(R.array.negara);
        kota = getResources().getStringArray(R.array.kota);
        provinsi = getResources().getStringArray(R.array.provinsi);
        kota1 = getResources().getStringArray(R.array.kota);
        pendidikan = getResources().getStringArray(R.array.pendidikan);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Di tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();

        //init permissions array
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, negara);
        lokasi1.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, provinsi);
        lokasi2.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kota);
        lokasi3.setAdapter(adapter3);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pendidikan);
        pendidikan1.setAdapter(adapter5);

        tanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfilPeternakActivity.this
                        , android.R.style.Theme_Holo_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                tanggallahir.setText(date);
            }
        };

        gamprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        loadMyInfo();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();

            }
        });
    }

    private void loadMyInfo() {
        //load user info, and set to views
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Peternak");
        ref.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String accountType = "" + ds.child("accountType").getValue();
                    String pendidikan = "" + ds.child("pendidikan").getValue();
                    String tanggal = "" + ds.child("tanggallahir").getValue();
                    String tempat = "" + ds.child("tempatlahir").getValue();
                    String name = "" + ds.child("name").getValue();
                    String timestamp = "" + ds.child("timestamp").getValue();
                    String profil_image = "" + ds.child("profil_image").getValue();
                    String negara = "" + ds.child("negara").getValue();
                    String provinsi = "" + ds.child("provinsi").getValue();
                    String kota = "" + ds.child("kota").getValue();
                    String umur1 = "" + ds.child("umur").getValue();

                    namleng.setText(name);
                    tempatlahir.setText(tempat);
                    tanggallahir.setText(tanggal);
                    pendidikan1.setText(pendidikan);
                    lokasi1.setText(negara);
                    lokasi2.setText(provinsi);
                    lokasi3.setText(kota);
                    umur.setText(umur1);
                    try {
                        Picasso.get().load(profil_image).placeholder(R.drawable.userphoto).into(gamprofil);

                    } catch (Exception e) {
                        gamprofil.setImageResource(R.drawable.userphoto);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String Fullname, Tempat, Tanggal, Pendidikan, Negara, Provinsi, Kota, Umur;

    private void inputData() {
        //input data
        Fullname = namleng.getText().toString().trim();
        Tempat = tempatlahir.getText().toString().trim();
        Tanggal = tanggallahir.getText().toString().trim();
        Pendidikan = pendidikan1.getText().toString().trim();
        Negara = lokasi1.getText().toString().trim();
        Provinsi = lokasi2.getText().toString().trim();
        Kota = lokasi3.getText().toString().trim();
        Umur = umur.getText().toString().trim();

        updateProfil();
    }

    private void updateProfil() {
        progressDialog.setMessage("update profil akun...");
        progressDialog.show();

        if (image_uri == null) {
            // save info account without image
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", "" + Fullname);
            hashMap.put("tempatlahir", "" + Tempat);
            hashMap.put("tanggallahir", "" + Tanggal);
            hashMap.put("pendidikan", "" + Pendidikan);
            hashMap.put("umur", "" + Umur);
            hashMap.put("negara", "" + Negara);
            hashMap.put("provinsi", "" + Provinsi);
            hashMap.put("kota", "" + Kota);

            // save to db
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Peternak");
            ref.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // update
//                    upProfil();
                    progressDialog.dismiss();
                    Toast.makeText(ProfilPeternakActivity.this, "Profil Updated...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfilPeternakActivity.this, SettingPeternakActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // failed to update
                    progressDialog.dismiss();
                    Toast.makeText(ProfilPeternakActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //update with image
            /*--------Upload image first--------*/
            String filePathAndName = "profile_image/" + "" + mAuth.getUid();
            //get Storage reference
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloadImageUri = uriTask.getResult();
                    if (uriTask.isSuccessful()) {
                        //image url received,now update to database
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("name", "" + Fullname);
                        hashMap.put("tempatlahir", "" + Tempat);
                        hashMap.put("tanggallahir", "" + Tanggal);
                        hashMap.put("pendidikan", "" + Pendidikan);
                        hashMap.put("negara", "" + Negara);
                        hashMap.put("provinsi", "" + Provinsi);
                        hashMap.put("kota", "" + Kota);
                        hashMap.put("profil_image", "" + downloadImageUri);//url of upload image


                        // save to db
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Peternak");
                        ref.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // update
                                progressDialog.dismiss();
//                                upProfil();
                                Toast.makeText(ProfilPeternakActivity.this, "Profil Updated...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProfilPeternakActivity.this, SettingPeternakActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // failed to update
                                progressDialog.dismiss();
                                Toast.makeText(ProfilPeternakActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        }
    }

    private void showImagePickDialog() {
        //options to display in dialog
        String[] options = {"Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ambil Gambar")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle click
                        if (which == 0) {
                            //gallery clicked
                            if (checkStoragePermission()) {
                                //storage permissions allowed
                                pickFromGallery();

                            } else {
                                //not allowed, request
                                requestStoragePermission();
                            }
                        }

                    }
                })
                .show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //permission allowed
                        pickFromGallery();
                    }
                } else {
                    Toast.makeText(this, "Storage Permission are necessary", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE && data != null && data.getData() != null) {
                //get picked Image
                image_uri = data.getData();
                //set on ImageView
                gamprofil.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        Intent intent = new Intent(ProfilPeternakActivity.this, SettingPeternakActivity.class);
        startActivity(intent);
        finish();
    }
}
