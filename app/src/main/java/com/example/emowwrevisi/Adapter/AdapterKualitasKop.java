package com.example.emowwrevisi.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emowwrevisi.EditKualitasSusuActivity;
import com.example.emowwrevisi.EditSetoranSusuActivity;
import com.example.emowwrevisi.Filter.FilterKualitasKop;
import com.example.emowwrevisi.Filter.FilterSetorankop;
import com.example.emowwrevisi.Model.ModelKualitasKop;
import com.example.emowwrevisi.Model.ModelSetoranKop;
import com.example.emowwrevisi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterKualitasKop extends RecyclerView.Adapter<AdapterKualitasKop.HolderKualitasKop> implements Filterable {
    private Context context;
    public ArrayList<ModelKualitasKop> kualitasKops,filterList;
    private FilterKualitasKop filter;

    public AdapterKualitasKop(Context context, ArrayList<ModelKualitasKop> kualitasKops) {
        this.context = context;
        this.kualitasKops = kualitasKops;
        this.filterList = kualitasKops;
    }

    @NonNull
    @Override
    public HolderKualitasKop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_kualitas_susu_koperasi,parent,false);
        return new AdapterKualitasKop.HolderKualitasKop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderKualitasKop holder, int position) {
        //get Data
        final ModelKualitasKop modelKualitasKop = kualitasKops.get(position);
        String id = modelKualitasKop.getKualitasId();
        String uid = modelKualitasKop.getUid();
        String tanggal = modelKualitasKop.getTanggal();
        String peternak = modelKualitasKop.getNamapeternak();
        String koperasi =  modelKualitasKop.getNamakoperasi();
        String hasil = modelKualitasKop.getHasil();
        String bonus = modelKualitasKop.getBonus();
        String parameterkualitas = modelKualitasKop.getParameterkualitas();
        String satuan = modelKualitasKop.getSatuankualitas();
        String jumlah = modelKualitasKop.getJumlahkualitas();

        //set data
        holder.tanggal1.setText(tanggal);
        holder.namapet.setText(peternak);
        holder.namakop.setText(koperasi);
        holder.parameterkul.setText(parameterkualitas+" "+satuan);
        holder.hasil1.setText(hasil+ " %");
        holder.bonus1.setText("Rp "+bonus);
        holder.jumlahkul.setText("Rp "+jumlah);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item click, show item details (in bottom sheet)
                detailsBottomSheet(modelKualitasKop);;//here modelPembayaran contains details of clicked
            }
        });
    }

    private void detailsBottomSheet(ModelKualitasKop modelKualitasKop) {
        //bottom sheet
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_kualitas_details, null);
        // set view to bottomsheet
        bottomSheetDialog.setContentView(view);
        //init view of bottomsheet
        ImageButton btnback = view.findViewById(R.id.btnback);
        final ImageButton btndelete = view.findViewById(R.id.btndelete);
        ImageButton btnedit = view.findViewById(R.id.btnedit);
        TextView tanggal1 = view.findViewById(R.id.tv_date);
        TextView peternak1 = view.findViewById(R.id.tv_namapet);
        TextView koperasi1 = view.findViewById(R.id.tv_namakop);
        TextView bonus1 = view.findViewById(R.id.tv_bonus);
        TextView hasil1 = view.findViewById(R.id.tv_hasil1);
        TextView parameterkul = view.findViewById(R.id.tv_parameterkul);
        TextView kualitas = view.findViewById(R.id.tv_jumlahkualitas);


        //get data
        final String id = modelKualitasKop.getKualitasId();
        String uid = modelKualitasKop.getUid();
        String koperasi = modelKualitasKop.getNamakoperasi();
        String tanggal = modelKualitasKop.getTanggal();
        String peternak = modelKualitasKop.getNamapeternak();
        String hasil = modelKualitasKop.getHasil();
        String bonus = modelKualitasKop.getBonus();
        String parameterkualitas = modelKualitasKop.getParameterkualitas();
        String satuan = modelKualitasKop.getSatuankualitas();
        String jumlah = modelKualitasKop.getJumlahkualitas();

        //set data
        tanggal1.setText(tanggal);
        peternak1.setText(peternak);
        koperasi1.setText(koperasi);
        parameterkul.setText(parameterkualitas+" "+ satuan);
        hasil1.setText(hasil + "%");
        bonus1.setText("Rp "+bonus);
        kualitas.setText("Rp "+jumlah);
        //show dialog
        bottomSheetDialog.show();

        //edit click
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //open edit pembayaran, pass id of pembayaran
                Intent intent = new Intent(context, EditKualitasSusuActivity.class);
                intent.putExtra("kualitasId", id);
                intent.putExtra("kualitasUid", uid);
                context.startActivity(intent);

            }
        });
        //delete click
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Apakah kamu yakin menghapus data Kualitas ?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                deleteKualitas(id);//id is the pembayaran id
                                dialog.dismiss();
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel, dismiss dialog
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        //back click
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void deleteKualitas(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kualitas");
        reference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Kualitas Hapus...", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kualitasKops.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new FilterKualitasKop(this,filterList);
        }
        return filter;
    }

    public class HolderKualitasKop extends RecyclerView.ViewHolder {
        private TextView namapet,namakop,tanggal1,hasil1,bonus1,parameterkul,jumlahkul;

        public HolderKualitasKop(@NonNull View itemView) {
            super(itemView);
            namapet = itemView.findViewById(R.id.namapet);
            namakop = itemView.findViewById(R.id.namakop);
            tanggal1 = itemView.findViewById(R.id.tanggal_tv);
            hasil1 = itemView.findViewById(R.id.hasil1);
            bonus1 = itemView.findViewById(R.id.bonus1);
            parameterkul = itemView.findViewById(R.id.parameterkul);
            jumlahkul = itemView.findViewById(R.id.jumlahkualitas1);
        }
    }
}
