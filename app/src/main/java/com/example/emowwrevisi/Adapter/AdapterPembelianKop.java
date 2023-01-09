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
import com.example.emowwrevisi.EditPembelianActivity;
import com.example.emowwrevisi.Filter.FilterKualitasKop;
import com.example.emowwrevisi.Filter.FilterPembelianKop;
import com.example.emowwrevisi.Model.ModelKualitasKop;
import com.example.emowwrevisi.Model.ModelPembelianKop;
import com.example.emowwrevisi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterPembelianKop extends RecyclerView.Adapter<AdapterPembelianKop.HolderPembelianKop> implements Filterable {
    private Context context;
    public ArrayList<ModelPembelianKop> pembelianKops,filterList;
    private FilterPembelianKop filter;

    public AdapterPembelianKop(Context context, ArrayList<ModelPembelianKop> pembelianKops) {
        this.context = context;
        this.pembelianKops = pembelianKops;
        this.filterList = pembelianKops;
    }

    @NonNull
    @Override
    public HolderPembelianKop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_pembelian_koperasi,parent,false);
        return new AdapterPembelianKop.HolderPembelianKop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPembelianKop holder, int position) {
        //get Data
        final ModelPembelianKop modelPembelianKop = pembelianKops.get(position);
        String id = modelPembelianKop.getPembayaranId();
        String uid = modelPembelianKop.getUid();
        String tanggal = modelPembelianKop.getTanggal();
        String peternak = modelPembelianKop.getNamapeternak();
        String koperasi =  modelPembelianKop.getNamakoperasi();
        String parameterBarang = modelPembelianKop.getParameterBarang();
        String satuan = modelPembelianKop.getSatuan();
        String jumlah = modelPembelianKop.getJumlah();
        String harga = modelPembelianKop.getHarga();
        String total = modelPembelianKop.getTotal();

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
                detailsBottomSheet(modelPembelianKop);//here modelPembayaran contains details of clicked

            }
        });
    }

    private void detailsBottomSheet(ModelPembelianKop modelPembayaran) {
        //bottom sheet
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_pembayaran_details_koperasi, null);
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
        final String id = modelPembayaran.getPembayaranId();
        String uid = modelPembayaran.getUid();
        String koperasi = modelPembayaran.getNamakoperasi();
        String tanggal = modelPembayaran.getTanggal();
        String peternak = modelPembayaran.getNamapeternak();
        String parameterbarang = modelPembayaran.getParameterBarang();
        String jumlah = modelPembayaran.getJumlah();
        String harga = modelPembayaran.getHarga();
        String satuan = modelPembayaran.getSatuan();
        String total = modelPembayaran.getTotal();

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

        //edit click
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //open edit pembayaran, pass id of pembayaran
                Intent intent = new Intent(context, EditPembelianActivity.class);
                intent.putExtra("pembelianId", id);
                intent.putExtra("pembelianuid", id);
                context.startActivity(intent);

            }
        });
        //delete click
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Apakah kamu yakin menghapus data pembayaran ?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                deletePembayaran(id);//id is the pembayaran id
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

    private void deletePembayaran(String id) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pembelian");
        reference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Pembelian deleted...", Toast.LENGTH_SHORT).show();
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
        return pembelianKops.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new FilterPembelianKop(this,filterList);
        }
        return filter;
    }

    public class HolderPembelianKop extends RecyclerView.ViewHolder {
        private TextView namapet,namakop,tanggal1,parameterbar,jumlahba,harga1,total1;

        public HolderPembelianKop(@NonNull View itemView) {
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
