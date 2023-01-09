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

import com.example.emowwrevisi.EditPembelianActivity;
import com.example.emowwrevisi.Filter.FilterPembelianKop;
import com.example.emowwrevisi.Filter.FilterPembelianPet;
import com.example.emowwrevisi.Model.ModelPembelianKop;
import com.example.emowwrevisi.Model.ModelPembelianPet;
import com.example.emowwrevisi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterPembelianPet extends RecyclerView.Adapter<AdapterPembelianPet.HolderPembelianPet> implements Filterable {
    private Context context;
    public ArrayList<ModelPembelianPet> pembelianPets,filterList;
    private FilterPembelianPet filter;

    public AdapterPembelianPet(Context context, ArrayList<ModelPembelianPet> pembelianPets) {
        this.context = context;
        this.pembelianPets = pembelianPets;
        this.filterList = pembelianPets;
    }

    @NonNull
    @Override
    public HolderPembelianPet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_pembelian_peternak,parent,false);
        return new AdapterPembelianPet.HolderPembelianPet(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPembelianPet holder, int position) {
        //get Data
        final ModelPembelianPet modelPembelianPet = pembelianPets.get(position);
        String id = modelPembelianPet.getPembayaranId();
        String uid = modelPembelianPet.getUid();
        String tanggal = modelPembelianPet.getTanggal();
        String peternak = modelPembelianPet.getNamapeternak();
        String koperasi =  modelPembelianPet.getNamakoperasi();
        String parameterBarang = modelPembelianPet.getParameterBarang();
        String satuan = modelPembelianPet.getSatuan();
        String jumlah = modelPembelianPet.getJumlah();
        String harga = modelPembelianPet.getHarga();
        String total = modelPembelianPet.getTotal();

        //set data
        holder.tanggal1.setText(tanggal);
        holder.namapet.setText(peternak);
        holder.namakop.setText(koperasi);
        holder.parameterbar.setText(parameterBarang);
        holder.jumlahba.setText(jumlah+" "+satuan);
        holder.harga1.setText("Rp. "+harga);
        holder.total1.setText("Rp."+total);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item click, show item details (in bottom sheet)
                detailsBottomSheet(modelPembelianPet);//here modelPembayaran contains details of clicked

            }
        });
    }

    private void detailsBottomSheet(ModelPembelianPet modelPembelianPet) {
        //bottom sheet
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_pembayaran_details_koperasi2, null);
        // set view to bottomsheet
        bottomSheetDialog.setContentView(view);
        //init view of bottomsheet
        ImageButton btnback = view.findViewById(R.id.btnback);
        final ImageButton btndelete = view.findViewById(R.id.btndelete);
        ImageButton btnedit = view.findViewById(R.id.btnedit);
        TextView tanggal1 = view.findViewById(R.id.tv_date);
        TextView peternak1 = view.findViewById(R.id.tv_namapet);
        TextView koperasi1 = view.findViewById(R.id.tv_namakop);
        TextView parameterbar = view.findViewById(R.id.tv_parameterbar);
        TextView jumlah1 = view.findViewById(R.id.tv_jumlah);
        TextView harga1 = view.findViewById(R.id.tv_harga);
        TextView total1 = view.findViewById(R.id.tv_total);

        //get data
        final String id = modelPembelianPet.getPembayaranId();
        String uid = modelPembelianPet.getUid();
        String koperasi = modelPembelianPet.getNamakoperasi();
        String tanggal = modelPembelianPet.getTanggal();
        String peternak = modelPembelianPet.getNamapeternak();
        String parameterbarang = modelPembelianPet.getParameterBarang();
        String jumlah = modelPembelianPet.getJumlah();
        String harga = modelPembelianPet.getHarga();
        String satuan = modelPembelianPet.getSatuan();
        String total = modelPembelianPet.getTotal();

        //set data
        tanggal1.setText(tanggal);
        peternak1.setText(peternak);
        koperasi1.setText(koperasi);
        parameterbar.setText(parameterbarang);
        jumlah1.setText(jumlah+" "+satuan);
        harga1.setText("Rp "+harga);
        total1.setText("Rp "+total);
        //show dialog
        bottomSheetDialog.show();

    }


    @Override
    public int getItemCount() {
        return pembelianPets.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new FilterPembelianPet(this,filterList);
        }
        return filter;
    }

    public class HolderPembelianPet extends RecyclerView.ViewHolder {
        private TextView namapet,namakop,tanggal1,parameterbar,jumlahba,harga1,total1;

        public HolderPembelianPet(@NonNull View itemView) {
            super(itemView);
            namapet = itemView.findViewById(R.id.namapet);
            namakop = itemView.findViewById(R.id.namakop);
            tanggal1 = itemView.findViewById(R.id.tanggal_tv);
            parameterbar = itemView.findViewById(R.id.parameterbar);
            jumlahba = itemView.findViewById(R.id.jumlahba);
            harga1 = itemView.findViewById(R.id.hargasatuan1);
            total1 = itemView.findViewById(R.id.hasil1);
        }
    }
}
