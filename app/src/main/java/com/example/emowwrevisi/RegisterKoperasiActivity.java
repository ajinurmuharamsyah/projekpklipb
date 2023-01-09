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
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterKoperasiActivity extends AppCompatActivity {
    private EditText namleng, useremail, userpassword, konfirmasi, namakop, sejarahkop;
    private Button btnregister;
    private ImageButton btnback;
    private TextView regpeternak, berdiri1;
    private ImageView gamprofil;
    private AutoCompleteTextView lokasi1, lokasi2, lokasi3;
    private String[] negara, provinsi, kota;

    //permission constants
    private static final int STORAGE_REQUEST_CODE = 100;

    //images pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 200;

    //permission arrays
    private String[] storagePermission;

    //images picked uri
    private Uri image_uri;

    //init Database
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private DatePickerDialog.OnDateSetListener setListener;
    //    private BroadcastReceiver broadcastReceiver;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_koperasi);
        gamprofil = findViewById(R.id.image4);
        namleng = findViewById(R.id.registerNama);
        useremail = findViewById(R.id.registerEmail);
        userpassword = findViewById(R.id.registerPassword);
        konfirmasi = findViewById(R.id.registerKonfirmasi);
        namakop = findViewById(R.id.registerName);
        berdiri1 = findViewById(R.id.registerTanggal);
        sejarahkop = findViewById(R.id.registerSejarah);
        lokasi1 = findViewById(R.id.registerNegara);
        lokasi2 = findViewById(R.id.registerprovinsi);
        lokasi3 = findViewById(R.id.registerkota);
        btnregister = findViewById(R.id.registerKop);
        regpeternak = findViewById(R.id.tv7);
        btnback = findViewById(R.id.btnback);

        negara = getResources().getStringArray(R.array.negara);
        kota = getResources().getStringArray(R.array.kota);
        provinsi = getResources().getStringArray(R.array.provinsi);

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

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, provinsi);
        lokasi2.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kota);
        lokasi3.setAdapter(adapter3);

        berdiri1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterKoperasiActivity.this
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
                berdiri1.setText(date);
            }
        };

        gamprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
                showImagePickDialog();
            }
        });

        regpeternak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterKoperasiActivity.this, RegisterPeternakActivity.class));
                finish();
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterKoperasiActivity.this,LoginActivity.class));
        finish();
    }

    private String Fullname, Email, Password, Konfirmasi, Koperasi, Berdiri, Sejarah, Negara, Provinsi, Kota;

    private void inputData() {
        //input data
        Fullname = namleng.getText().toString().trim();
        Email = useremail.getText().toString().trim();
        Password = userpassword.getText().toString().trim();
        Konfirmasi = konfirmasi.getText().toString().trim();
        Koperasi = namakop.getText().toString().trim();
        Berdiri = berdiri1.getText().toString().trim();
        Sejarah = sejarahkop.getText().toString().trim();
        Negara = lokasi1.getText().toString().trim();
        Provinsi = lokasi2.getText().toString().trim();
        Kota = lokasi3.getText().toString().trim();
        //validate data
        if (TextUtils.isEmpty(Fullname)) {
            Toast.makeText(this, "Nama Lengkap kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            Toast.makeText(this, "Email Kosong!.", Toast.LENGTH_SHORT).show();
            return;
        } else if (Password.length() < 5) {
            Toast.makeText(this, "Password minimal 5 Karakter", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Password.equals(Konfirmasi)) {
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Koperasi)) {
            Toast.makeText(this, "Nama Koperasi kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Berdiri)) {
            Toast.makeText(this, "Tanggal Berdiri Koperasi kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Sejarah)) {
            Toast.makeText(this, "Sejarah Koperasi kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Negara)) {
            Toast.makeText(this, "Lokasi Negara Koperasi kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Provinsi)) {
            Toast.makeText(this, "Lokasi Negara Koperasi kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Kota)) {
            Toast.makeText(this, "Lokasi Negara Koperasi kosong!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            createAccount();
        }
    }

    private void createAccount() {
        progressDialog.setMessage("Membuat Akun...");
        progressDialog.show();

        //createAccount
        mAuth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Create Account
                saverFirebaseData();
                tambahdata();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed create account
                progressDialog.dismiss();
                Toast.makeText(RegisterKoperasiActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void tambahdata() {

        final String timestamp = "" + System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", "" + mAuth.getUid());
        hashMap.put("timestamp", "" + timestamp);
        hashMap.put("accountType", "Koperasi");

        // save to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(mAuth.getUid()).setValue(hashMap);
    }

    private void saverFirebaseData() {
        progressDialog.setMessage("Menyimpan info akun...");

        final String timestamp = "" + System.currentTimeMillis();

        if (image_uri == null) {
            // save info account without image
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid", "" + mAuth.getUid());
            hashMap.put("email", "" + Email);
            hashMap.put("owner", "" + Fullname);
            hashMap.put("koperasiname", "" + Koperasi);
            hashMap.put("berdiri", "" + Berdiri);
            hashMap.put("sejarah", "" + Sejarah);
            hashMap.put("negara", "" + Negara);
            hashMap.put("provinsi", "" + Provinsi);
            hashMap.put("kota", "" + Kota);
            hashMap.put("timestamp", "" + timestamp);
            hashMap.put("accountType", "Koperasi");
            hashMap.put("online", "true");
            hashMap.put("profil_image", "");

            // save to db
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Koperasi");
            ref.child(mAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // dp update
                    progressDialog.dismiss();
                    startActivity(new Intent(RegisterKoperasiActivity.this, DashboardKoperasiActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // failed Updating db
                    progressDialog.dismiss();
                    Toast.makeText(RegisterKoperasiActivity.this, "Gagal buat akun...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // save info account with image

            //name and path of image
            String filePathAndName = "profil_image/" + "" + mAuth.getUid();

            //upload image
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //get url of upload image
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloadImageUri = uriTask.getResult();

                    if (uriTask.isSuccessful()) {
                        // save info account without image
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("uid", "" + mAuth.getUid());
                        hashMap.put("email", "" + Email);
                        hashMap.put("owner", "" + Fullname);
                        hashMap.put("koperasiname", "" + Koperasi);
                        hashMap.put("berdiri", "" + Berdiri);
                        hashMap.put("sejarah", "" + Sejarah);
                        hashMap.put("negara", "" + Negara);
                        hashMap.put("provinsi", "" + Provinsi);
                        hashMap.put("kota", "" + Kota);
                        hashMap.put("timestamp", "" + timestamp);
                        hashMap.put("accountType", "Koperasi");
                        hashMap.put("online", "true");
                        hashMap.put("profil_image", "" + downloadImageUri);//url of upload image

                        // save to db
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Koperasi");
                        ref.child(mAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // dp update
                                progressDialog.dismiss();
                                startActivity(new Intent(RegisterKoperasiActivity.this, DashboardKoperasiActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // failed Updating db
                                progressDialog.dismiss();
                                Toast.makeText(RegisterKoperasiActivity.this, "Gagal buat akun...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterKoperasiActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE && data != null && data.getData() != null) {
                //get picked Image
                image_uri = data.getData();

                //set on ImageView
                gamprofil.setImageURI(image_uri);

            }
        }
    }
}
