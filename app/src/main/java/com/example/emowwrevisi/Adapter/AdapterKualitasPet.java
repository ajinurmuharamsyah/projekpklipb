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
import com.example.emowwrevisi.Filter.FilterKualitasKop;
import com.example.emowwrevisi.Filter.FilterKualitasPet;
import com.example.emowwrevisi.Model.ModelKualitasKop;
import com.example.emowwrevisi.Model.ModelKualitasPet;
import com.example.emowwrevisi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterKualitasPet extends RecyclerView.Adapter<AdapterKualitasPet.HolderKualitasPet> implements Filterable {
    private Context context;
    public ArrayList<ModelKualitasPet> kualitasPets,filterList;
    private FilterKualitasPet filter;

    public AdapterKualitasPet(Context context, ArrayList<ModelKualitasPet> kualitasPets) {
        this.context = context;
        this.kualitasPets = kualitasPets;
        this.filterList = kualitasPets;
    }

    @NonNull
    @Override
    public HolderKualitasPet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_kualitas_susu_koperasi,parent,false);
        return new AdapterKualitasPet.HolderKualitasPet(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderKualitasPet holder, int position) {
        //get Data
        final ModelKualitasPet modelKualitasPet = kualitasPets.get(position);
        String id = modelKualitasPet.getKualitasId();
        String uid = modelKualitasPet.getUid();
        String tanggal = modelKualitasPet.getTanggal();
        String peternak = modelKualitasPet.getNamapeternak();
        String koperasi =  modelKualitasPet.getNamakoperasi();
        String hasil = modelKualitasPet.getHasil();
        String bonus = modelKualitasPet.getBonus();
        String parameterkualitas = modelKualitasPet.getParameterkualitas();
        String satuan = modelKualitasPet.getSatuankualitas();
        String jumlah = modelKualitasPet.getJumlahkualitas();

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
                detailsBottomSheet(modelKualitasPet);;//here modelPembayaran contains details of clicked
            }
        });
    }

    private void detailsBottomSheet(ModelKualitasPet modelKualitasPet) {
        //bottom sheet
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_kualitas_details2, null);
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
        final String id = modelKualitasPet.getKualitasId();
        String uid = modelKualitasPet.getUid();
        String koperasi = modelKualitasPet.getNamakoperasi();
        String tanggal = modelKualitasPet.getTanggal();
        String peternak = modelKualitasPet.getNamapeternak();
        String hasil = modelKualitasPet.getHasil();
        String bonus = modelKualitasPet.getBonus();
        String parameterkualitas = modelKualitasPet.getParameterkualitas();
        String satuan = modelKualitasPet.getSatuankualitas();
        String jumlah = modelKualitasPet.getJumlahkualitas();

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

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kualitasPets.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new FilterKualitasPet(this,filterList);
        }
        return filter;
    }

    public class HolderKualitasPet extends RecyclerView.ViewHolder {
        private TextView namapet,namakop,tanggal1,hasil1,bonus1,parameterkul,jumlahkul;

        public HolderKualitasPet(@NonNull View itemView) {
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
