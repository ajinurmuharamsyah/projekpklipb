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

import com.example.emowwrevisi.Model.ModelKoperasi;
import com.example.emowwrevisi.Model.ModelPeternak;
import com.example.emowwrevisi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterJoinPeternak extends RecyclerView.Adapter<AdapterJoinPeternak.HolderJoin> {
    private Context context;
    public ArrayList<ModelPeternak> joinkop;

    public AdapterJoinPeternak(Context context, ArrayList<ModelPeternak> joinkop) {
        this.context = context;
        this.joinkop = joinkop;
    }

    @NonNull
    @Override
    public HolderJoin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_validasi,parent,false);
        return new HolderJoin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderJoin holder, int position) {
        final ModelPeternak modelPeternak = joinkop.get(position);
        String validasi1 = modelPeternak.getValidasi();
        String anggotaKoperasi = modelPeternak.getAnggotakoperasi();
        String name = modelPeternak.getName();

        if (validasi1.equals("terima")){
            holder.validasicek.setText(name + " telah bergabung di " + anggotaKoperasi);
        } else if (validasi1.equals("pending")){
            holder.validasicek.setText(name + " menunggu konfirmasi dari koperasi");
        } else if (validasi1.equals("tolak")){
            holder.validasicek.setText(name + " telah ditolak dari " + anggotaKoperasi);
        }else {
            holder.validasicek.setText("Anda belum mendaftar di koperasi manapun");
        }
    }

    @Override
    public int getItemCount() {
        return joinkop.size();
    }

    public class HolderJoin extends RecyclerView.ViewHolder {
        private TextView validasicek;
        public HolderJoin(@NonNull View itemView) {
            super(itemView);
            validasicek = itemView.findViewById(R.id.catatan);
        }
    }
}
