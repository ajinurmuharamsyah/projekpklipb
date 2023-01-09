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

import com.example.emowwrevisi.EditSetoranSusuActivity;
import com.example.emowwrevisi.Filter.FilterSetorankop;
import com.example.emowwrevisi.Filter.FilterSetoranpet;
import com.example.emowwrevisi.Model.ModelSetoranKop;
import com.example.emowwrevisi.Model.ModelSetoranPet;
import com.example.emowwrevisi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterSetoranPet extends RecyclerView.Adapter<AdapterSetoranPet.HolderSetoranPet> implements Filterable {
    private Context context;
    public ArrayList<ModelSetoranPet> setoranPets,filterList1;
    private FilterSetoranpet filter1;

    public AdapterSetoranPet(Context context, ArrayList<ModelSetoranPet> setoranPets) {
        this.context = context;
        this.setoranPets = setoranPets;
        this.filterList1 = setoranPets;
    }

    @NonNull
    @Override
    public HolderSetoranPet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_setoran_susu_peternak,parent,false);
        return new AdapterSetoranPet.HolderSetoranPet(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSetoranPet holder, int position) {
        //get Data
        final ModelSetoranPet modelSetoran1 = setoranPets.get(position);
        final String id = modelSetoran1.getSetoranId();
        final String uid = modelSetoran1.getUid();
        String tanggal = modelSetoran1.getTanggal();
        String peternak = modelSetoran1.getNamapeternak();
        String koperasi =  modelSetoran1.getNamakoperasi();
        String jamsetoran = modelSetoran1.getJamsetoran();
        String jumlahsusu = modelSetoran1.getJumlahSusu();
        String harga = modelSetoran1.getHargaSusu();
        String total = modelSetoran1.getTotal();

        //set data
        holder.tanggal1.setText(tanggal);
        holder.namapet.setText(peternak);
        holder.namakop.setText(koperasi);
        holder.jamsetoran1.setText(jamsetoran);
        holder.jumlah1.setText(jumlahsusu+" Liter");
        holder.harga1.setText("Rp. "+harga);
        holder.total1.setText("Rp."+total);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item click, show item details (in bottom sheet)
                detailsBottomSheet(modelSetoran1);;//here modelPembayaran contains details of clicked
            }
        });
    }

    private void detailsBottomSheet(ModelSetoranPet modelSetoran) {
        //bottom sheet
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_setoran_detail2, null);
        // set view to bottomsheet
        bottomSheetDialog.setContentView(view);
        //init view of bottomsheet
        ImageButton btnback = view.findViewById(R.id.btnback);
        final ImageButton btndelete = view.findViewById(R.id.btndelete);
        ImageButton btnedit = view.findViewById(R.id.btnedit);
        TextView tanggal1 = view.findViewById(R.id.tv_date);
        TextView peternak1 = view.findViewById(R.id.tv_namapet);
        TextView koperasi1 = view.findViewById(R.id.tv_namakop);
        TextView jamsetoran1 = view.findViewById(R.id.tv_pagi);
        TextView jumlahsusu1 = view.findViewById(R.id.tv_susu);
        TextView hargasusu1 = view.findViewById(R.id.tv_harga);
        TextView total1 = view.findViewById(R.id.tv_total);

        //get data
        final String id = modelSetoran.getSetoranId();
        final String uid = modelSetoran.getUid();
        String koperasi = modelSetoran.getNamakoperasi();
        String tanggal = modelSetoran.getTanggal();
        String peternak = modelSetoran.getNamapeternak();
        String jamsetoran = modelSetoran.getJamsetoran();
        String hargasusu = modelSetoran.getHargaSusu();
        String jumlahsusu = modelSetoran.getJumlahSusu();
        String total = modelSetoran.getTotal();

        //set data
        tanggal1.setText(tanggal);
        peternak1.setText(peternak);
        koperasi1.setText(koperasi);
        jamsetoran1.setText(jamsetoran);
        hargasusu1.setText("Rp "+hargasusu);
        jumlahsusu1.setText(jumlahsusu+" Liter");
        total1.setText("Rp "+total);
        //show dialog
        bottomSheetDialog.show();
        //back click
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
    }


    @Override
    public int getItemCount() {
        return setoranPets.size();
    }

    @Override
    public Filter getFilter() {
        if (filter1==null){
            filter1 = new FilterSetoranpet( this,filterList1);
        }
        return filter1;
    }

    public class HolderSetoranPet extends RecyclerView.ViewHolder {
        private TextView namapet,namakop,tanggal1,jamsetoran1,jumlah1,harga1,total1;

        public HolderSetoranPet(@NonNull View itemView) {
            super(itemView);
            namapet = itemView.findViewById(R.id.namapet);
            namakop = itemView.findViewById(R.id.namakop);
            tanggal1 = itemView.findViewById(R.id.tanggal_tv);
            jamsetoran1 = itemView.findViewById(R.id.pagia);
            jumlah1 = itemView.findViewById(R.id.jumlahsetsus);
            harga1 = itemView.findViewById(R.id.hargasatuan1);
            total1 = itemView.findViewById(R.id.total1);
        }
    }
}
