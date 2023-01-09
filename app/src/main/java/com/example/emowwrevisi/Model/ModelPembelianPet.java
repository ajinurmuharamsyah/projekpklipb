package com.example.emowwrevisi.Model;

public class ModelPembelianPet {
    private String pembayaranId,tanggal,namapeternak,namakoperasi,parameterBarang,jumlah,satuan,harga,total,uid;

    public ModelPembelianPet(String pembayaranId, String tanggal, String namapeternak, String namakoperasi, String parameterBarang, String jumlah, String satuan, String harga, String total, String uid) {
        this.pembayaranId = pembayaranId;
        this.tanggal = tanggal;
        this.namapeternak = namapeternak;
        this.namakoperasi = namakoperasi;
        this.parameterBarang = parameterBarang;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.harga = harga;
        this.total = total;
        this.uid = uid;
    }

    public ModelPembelianPet(){

    }

    public String getPembayaranId() {
        return pembayaranId;
    }

    public void setPembayaranId(String pembayaranId) {
        this.pembayaranId = pembayaranId;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamapeternak() {
        return namapeternak;
    }

    public void setNamapeternak(String namapeternak) {
        this.namapeternak = namapeternak;
    }

    public String getNamakoperasi() {
        return namakoperasi;
    }

    public void setNamakoperasi(String namakoperasi) {
        this.namakoperasi = namakoperasi;
    }

    public String getParameterBarang() {
        return parameterBarang;
    }

    public void setParameterBarang(String parameterBarang) {
        this.parameterBarang = parameterBarang;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
