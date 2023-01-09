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
import com.example.emowwrevisi.Model.ModelSetoranKop;
import com.example.emowwrevisi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterSetoranKop extends RecyclerView.Adapter<AdapterSetoranKop.HolderSetoranKop> implements Filterable {
    private Context context;
    public ArrayList<ModelSetoranKop> setoranKops,filterList;
    private FilterSetorankop filter;

    public AdapterSetoranKop(Context context, ArrayList<ModelSetoranKop> setoranKops) {
        this.context = context;
        this.setoranKops = setoranKops;
        this.filterList = setoranKops;
    }

    @NonNull
    @Override
    public HolderSetoranKop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_setoran_susu_koperasi,parent,false);
        return new AdapterSetoranKop.HolderSetoranKop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSetoranKop holder, int position) {
        //get Data
        final ModelSetoranKop modelSetoran = setoranKops.get(position);
        final String id = modelSetoran.getSetoranId();
        final String uid = modelSetoran.getUid();
        String tanggal = modelSetoran.getTanggal();
        String peternak = modelSetoran.getNamapeternak();
        String koperasi =  modelSetoran.getNamakoperasi();
        String jamsetoran = modelSetoran.getJamsetoran();
        String jumlahsusu = modelSetoran.getJumlahSusu();
        String harga = modelSetoran.getHargaSusu();
        String total = modelSetoran.getTotal();

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
                detailsBottomSheet(modelSetoran);;//here modelPembayaran contains details of clicked
            }
        });
    }

    private void detailsBottomSheet(ModelSetoranKop modelSetoran) {
        //bottom sheet
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_setoran_detail, null);
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

        //edit click
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //open edit pembayaran, pass id of pembayaran
                Intent intent = new Intent(context, EditSetoranSusuActivity.class);
                intent.putExtra("setoranId", id);
                intent.putExtra("setoranuid", uid);
                context.startActivity(intent);

            }
        });
        //delete click
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Apakah kamu yakin menghapus data Setoran?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                deleteSetoran(id);//id is the pembayaran id
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

    private void deleteSetoran(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Setorans");
        reference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Setoran Hapus...", Toast.LENGTH_SHORT).show();
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
        return setoranKops.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new FilterSetorankop(this,filterList);
        }
        return filter;
    }

    public class HolderSetoranKop extends RecyclerView.ViewHolder {
        private TextView namapet,namakop,tanggal1,jamsetoran1,jumlah1,harga1,total1;

        public HolderSetoranKop(@NonNull View itemView) {
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
