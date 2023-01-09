package com.example.emowwrevisi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emowwrevisi.Filter.FilterInvoiceKop;
import com.example.emowwrevisi.Filter.FilterInvoicePet;
import com.example.emowwrevisi.Model.ModelInvoiceKop;
import com.example.emowwrevisi.Model.ModelInvoicePet;
import com.example.emowwrevisi.Model.ModelPembelianPet;
import com.example.emowwrevisi.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class AdapterInvoicePet extends RecyclerView.Adapter<AdapterInvoicePet.HolderInvoicePet> implements Filterable {
    private Context context;
    public ArrayList<ModelInvoicePet> invoicePets,filterList;
    private FilterInvoicePet filter;

    public AdapterInvoicePet(Context context, ArrayList<ModelInvoicePet> invoicePets) {
        this.context = context;
        this.invoicePets = invoicePets;
        this.filterList = invoicePets;
    }

    @NonNull
    @Override
    public HolderInvoicePet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_invoice_koperasi,parent,false);
        return new AdapterInvoicePet.HolderInvoicePet(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderInvoicePet holder, int position) {
        final ModelInvoicePet modelInvoice = invoicePets.get(position);
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

    private void detailsBottomSheet(ModelInvoicePet modelInvoice) {
        //bottom sheet
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_invoice_peternak, null);
        // set view to bottomsheet
        bottomSheetDialog.setContentView(view);
        //init view of bottomsheet
        ImageButton btnback = view.findViewById(R.id.btnback);
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
        return invoicePets.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new FilterInvoicePet(this,filterList);
        }
        return filter;
    }

    public class HolderInvoicePet extends RecyclerView.ViewHolder {
        private TextView namapet,namakop,tanggal1,totalk,totalp,totals,totalpem;
        public HolderInvoicePet(@NonNull View itemView) {
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
