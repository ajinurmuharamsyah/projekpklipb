package com.example.emowwrevisi.Model;

public class ModelInvoicePet {
    private String invoiceId,totalkualitas,namakoperasi,namapeternak,tanggal,totalpembelian,totalsetoran,uid,totalpendapatan;

    public ModelInvoicePet(String invoiceId, String totalkualitas, String namakoperasi, String namapeternak, String tanggal, String totalpembelian, String totalsetoran, String uid, String totalpendapatan) {
        this.invoiceId = invoiceId;
        this.totalkualitas = totalkualitas;
        this.namakoperasi = namakoperasi;
        this.namapeternak = namapeternak;
        this.tanggal = tanggal;
        this.totalpembelian = totalpembelian;
        this.totalsetoran = totalsetoran;
        this.uid = uid;
        this.totalpendapatan = totalpendapatan;
    }

    public ModelInvoicePet(){

    }

    public String getTotalpendapatan() {
        return totalpendapatan;
    }

    public void setTotalpendapatan(String totalpendapatan) {
        this.totalpendapatan = totalpendapatan;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getTotalkualitas() {
        return totalkualitas;
    }

    public void setTotalkualitas(String totalkualitas) {
        this.totalkualitas = totalkualitas;
    }

    public String getNamakoperasi() {
        return namakoperasi;
    }

    public void setNamakoperasi(String namakoperasi) {
        this.namakoperasi = namakoperasi;
    }

    public String getNamapeternak() {
        return namapeternak;
    }

    public void setNamapeternak(String namapeternak) {
        this.namapeternak = namapeternak;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotalpembelian() {
        return totalpembelian;
    }

    public void setTotalpembelian(String totalpembelian) {
        this.totalpembelian = totalpembelian;
    }

    public String getTotalsetoran() {
        return totalsetoran;
    }

    public void setTotalsetoran(String totalsetoran) {
        this.totalsetoran = totalsetoran;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
