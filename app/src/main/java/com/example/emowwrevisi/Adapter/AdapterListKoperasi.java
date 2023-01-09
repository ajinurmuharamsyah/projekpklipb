package com.example.emowwrevisi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emowwrevisi.Filter.FilterListKop;
import com.example.emowwrevisi.Filter.FilterSetorankop;
import com.example.emowwrevisi.KoperasiDetailsActivity;
import com.example.emowwrevisi.KoperasiJoinActivity;
import com.example.emowwrevisi.Model.ModelKoperasi;
import com.example.emowwrevisi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterListKoperasi extends RecyclerView.Adapter<AdapterListKoperasi.HolderListKoperasi> implements Filterable {
    private Context context;
    public ArrayList<ModelKoperasi> modelKoperasis,filterList;
    private FilterListKop filter;

    public AdapterListKoperasi(Context context, ArrayList<ModelKoperasi> modelKoperasis) {
        this.context = context;
        this.modelKoperasis = modelKoperasis;
        this.filterList = modelKoperasis;
    }

    @NonNull
    @Override
    public HolderListKoperasi onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_koperasi_join,parent,false);
        return new HolderListKoperasi(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderListKoperasi holder, int position) {
        //get data
        final ModelKoperasi modelKoperasi = modelKoperasis.get(position);
        final String uid = modelKoperasi.getUid();
        String accountType = modelKoperasi.getAccountType();
        String owner = modelKoperasi.getOwner();
        String email = modelKoperasi.getEmail();
        final String koperasiname = modelKoperasi.getKoperasiname();
        String berdiri = modelKoperasi.getBerdiri();
        String sejarah = modelKoperasi.getSejarah();
        String negara = modelKoperasi.getNegara();
        String provinsi = modelKoperasi.getProvinsi();
        String kota = modelKoperasi.getKota();
        String timestamp = modelKoperasi.getTimestamp();
        String profil_image = modelKoperasi.getProfil_image();

        //set data
        holder.namakop.setText(koperasiname);
        holder.emailtv.setText(email);
        holder.lokasitv.setText(negara + "," + provinsi + "," + kota);

        try {
            Picasso.get().load(profil_image).placeholder(R.drawable.ic_store_black_24dp).into(holder.profilIv);
        } catch (Exception e){
            holder.profilIv.setImageResource(R.drawable.ic_store_black_24dp);
        }

        //handle click listener, show shop details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KoperasiJoinActivity.class);
                intent.putExtra("Uid",uid);
                intent.putExtra("koperasiname",koperasiname);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelKoperasis.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new FilterListKop(this,filterList);
        }
        return filter;
    }

    public class HolderListKoperasi extends RecyclerView.ViewHolder {
        //ui view of row_koperasi.xml
        private ImageView profilIv;
        private TextView namakop,emailtv,lokasitv;
        public HolderListKoperasi(@NonNull View itemView) {
            super(itemView);
            profilIv = itemView.findViewById(R.id.profilIv);
            namakop = itemView.findViewById(R.id.namakop);
            emailtv = itemView.findViewById(R.id.email);
            lokasitv = itemView.findViewById(R.id.alamat);
        }
    }
}
