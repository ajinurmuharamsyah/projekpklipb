package com.example.emowwrevisi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emowwrevisi.Model.ModelPeternak;
import com.example.emowwrevisi.PeternakJoinActivity;
import com.example.emowwrevisi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterAproveKop extends RecyclerView.Adapter<AdapterAproveKop.HolderAproveKop> {
    private Context context;
    public ArrayList<ModelPeternak> peternak;

    public AdapterAproveKop(Context context, ArrayList<ModelPeternak> peternak) {
        this.context = context;
        this.peternak = peternak;
    }

    @NonNull
    @Override
    public HolderAproveKop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_anggota_koperasi,parent,false);
        return new HolderAproveKop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAproveKop holder, int position) {
        //get data
        final ModelPeternak modelPeternak = peternak.get(position);
        String accountType = modelPeternak.getAccountType();
        String username = modelPeternak.getUsername();
        String email = modelPeternak.getEmail();
        String name = modelPeternak.getName();
        String tempatlahir = modelPeternak.getTempatlahir();
        String tanggallahir = modelPeternak.getTanggallahir();
        String pendidikan = modelPeternak.getPendidikan();
        String online = modelPeternak.getOnline();
        final String uid = modelPeternak.getUid();
        String timestamp = modelPeternak.getTimestamp();
        String profil_image = modelPeternak.getProfil_image();
        final String anggotakoperasi = modelPeternak.getAnggotakoperasi();
        final String anggotabaru = modelPeternak.getAnggotabaru();
        final String validasi = modelPeternak.getValidasi();
        String negara = modelPeternak.getNegara();
        String provinsi = modelPeternak.getProvinsi();
        String kota = modelPeternak.getKota();
        String umur = modelPeternak.getUmur();

        //set data
        holder.namapet.setText(name);
        holder.emailtv.setText(email);
        holder.pendidikantv.setText(pendidikan);
        //check if online
        if (online.equals("true")){
            //peternak is online
            holder.onlineTv.setVisibility(View.VISIBLE);
        }else {
            //peternak is offline
            holder.onlineTv.setVisibility(View.GONE);
        }
        if (validasi != null && validasi.equals("pending")){
            holder.validasikop.setVisibility(View.VISIBLE);
        }else if (validasi != null && validasi.equals("terima")){
            holder.validasikop.setVisibility(View.INVISIBLE);
        }else {
            holder.validasikop.setVisibility(View.INVISIBLE);
        }

        try {
            Picasso.get().load(profil_image).placeholder(R.drawable.ic_account_circle_black_24dp).into(holder.profilIv);
        } catch (Exception e){
            holder.profilIv.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PeternakJoinActivity.class);
                intent.putExtra("peternakUid",uid);
                intent.putExtra("anggotabaru",anggotabaru);
                intent.putExtra("koperasiname",anggotakoperasi);
                intent.putExtra("validasi",validasi);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return peternak.size();
    }

    public class HolderAproveKop extends RecyclerView.ViewHolder {
        private ImageView profilIv, onlineTv,newanggota1;
        private TextView namapet,emailtv,pendidikantv,validasikop;
        public HolderAproveKop(@NonNull View itemView) {
            super(itemView);
            profilIv = itemView.findViewById(R.id.profilIv);
            onlineTv = itemView.findViewById(R.id.onlineTv);
            namapet = itemView.findViewById(R.id.namapet);
            emailtv = itemView.findViewById(R.id.email);
            pendidikantv = itemView.findViewById(R.id.pendidikan);
            validasikop = itemView.findViewById(R.id.validasitv);
        }
    }
}
