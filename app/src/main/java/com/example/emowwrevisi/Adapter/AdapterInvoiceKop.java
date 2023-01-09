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

import com.example.emowwrevisi.EditInvoiceActivity;
import com.example.emowwrevisi.Filter.FilterInvoiceKop;
import com.example.emowwrevisi.Filter.FilterListKop;
import com.example.emowwrevisi.Model.ModelInvoiceKop;
import com.example.emowwrevisi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterInvoiceKop extends RecyclerView.Adapter<AdapterInvoiceKop.HolderInvoiceKop> implements Filterable {
    private Context context;
    public ArrayList<ModelInvoiceKop> invoiceKops,filterList;
    private FilterInvoiceKop filter;

    public AdapterInvoiceKop(Context context, ArrayList<ModelInvoiceKop> invoiceKops) {
        this.context = context;
        this.invoiceKops = invoiceKops;
        this.filterList = invoiceKops;
    }

    @NonNull
    @Override
    public HolderInvoiceKop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_invoice_koperasi,parent,false);
        return new AdapterInvoiceKop.HolderInvoiceKop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderInvoiceKop holder, int position) {
        final ModelInvoiceKop modelInvoice = invoiceKops.get(position);
        String invoiceId = modelInvoice.getInvoiceId();
        String uid = modelInvoice.getUid();
        String tanggal = modelInvoice.getTanggal();
        String peternak = modelInvoice.getNamapeternak();
        String koperasi = modelInvoice.getNamakoperasi();
        String totalpembelian = modelInvoice.getTotalpembelian();
        String totalsetoran = modelInvoice.getTotalsetoran();
        String totalkualitas = modelInvoice.getTotalkualitas();
        String totalpendapatan = modelInvoice.getTotalpendapatan();

        holder.tanggal1.setText(tanggal);
        holder.namapet.setText(peternak);
        holder.namakop.setText(koperasi);
        holder.totals.setText("Rp "+totalsetoran);
        holder.totalp.setText("Rp "+ totalpembelian);
        holder.totalk.setText("Rp "+totalkualitas);
        holder.totalpem.setText("Rp " + totalpendapatan);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item click, show item details (in bottom sheet)
                detailsBottomSheet(modelInvoice);//here modelPembayaran contains details of clicked

            }
        });

    }

    private void detailsBottomSheet(ModelInvoiceKop modelInvoice) {
        //bottom sheet
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_invoice_koperasi, null);
        // set view to bottomsheet
        bottomSheetDialog.setContentView(view);
        //init view of bottomsheet
        ImageButton btnback = view.findViewById(R.id.btnback);
        final ImageButton btndelete = view.findViewById(R.id.btndelete);
        ImageButton btnedit = view.findViewById(R.id.btnedit);
        TextView tanggal1 = view.findViewById(R.id.tv_date);
        TextView peternak1 = view.findViewById(R.id.tv_namapet);
        TextView koperasi1 = view.findViewById(R.id.tv_namakop);
        TextView pembelian = view.findViewById(R.id.tv_pembelian);
        TextView setoran = view.findViewById(R.id.tv_setoran);
        TextView kualitas = view.findViewById(R.id.tv_kualitas);
        TextView pendapatan = view.findViewById(R.id.tv_pendapatan);

        //get data
        final String invoiceId = modelInvoice.getInvoiceId();
        String uid = modelInvoice.getUid();
        String tanggal = modelInvoice.getTanggal();
        String peternak = modelInvoice.getNamapeternak();
        String koperasi = modelInvoice.getNamakoperasi();
        String totalpembelian = modelInvoice.getTotalpembelian();
        String totalsetoran = modelInvoice.getTotalsetoran();
        String totalkualitas = modelInvoice.getTotalkualitas();
        String totalpendapatan = modelInvoice.getTotalpendapatan();

        tanggal1.setText(tanggal);
        peternak1.setText(peternak);
        koperasi1.setText(koperasi);
        pembelian.setText("Rp "+totalpembelian);
        setoran.setText("Rp "+totalsetoran);
        kualitas.setText("Rp "+totalkualitas);
        pendapatan.setText("Rp "+totalpendapatan);

        //show dialog
        bottomSheetDialog.show();

        //edit click
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //open edit pembayaran, pass id of pembayaran
                Intent intent = new Intent(context, EditInvoiceActivity.class);
                intent.putExtra("invoiceId", invoiceId);
                context.startActivity(intent);

            }
        });
        //delete click
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Apakah kamu yakin menghapus data Invoice?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                deleteinvoice(invoiceId);//id is the pembayaran id
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

    private void deleteinvoice(String invoiceId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Invoices");
        reference.child(invoiceId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Invoice deleted...", Toast.LENGTH_SHORT).show();
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
        return invoiceKops.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new FilterInvoiceKop(this,filterList);
        }
        return filter;
    }

    public class HolderInvoiceKop extends RecyclerView.ViewHolder {
        private TextView namapet,namakop,tanggal1,totalk,totalp,totals,totalpem;
        public HolderInvoiceKop(@NonNull View itemView) {
            super(itemView);
            namapet = itemView.findViewById(R.id.namapet);
            namakop = itemView.findViewById(R.id.namakop);
            tanggal1 = itemView.findViewById(R.id.tanggal_tv);
            totalk = itemView.findViewById(R.id.totalkualitas);
            totalp = itemView.findViewById(R.id.totalpembelian);
            totalpem = itemView.findViewById(R.id.totalpendapat);
            totals = itemView.findViewById(R.id.totalsetoran);
        }
    }
}
