package com.example.emowwrevisi.Model;

public class ModelSetoranKop {
    private String setoranId,uid,tanggal,namapeternak,namakoperasi,jamsetoran,hargaSusu,jumlahSusu,total;

    public ModelSetoranKop(String setoranId, String uid, String tanggal, String namapeternak, String namakoperasi, String jamsetoran, String hargaSusu, String jumlahSusu, String total) {
        this.setoranId = setoranId;
        this.uid = uid;
        this.tanggal = tanggal;
        this.namapeternak = namapeternak;
        this.namakoperasi = namakoperasi;
        this.jamsetoran = jamsetoran;
        this.hargaSusu = hargaSusu;
        this.jumlahSusu = jumlahSusu;
        this.total = total;
    }

    public ModelSetoranKop(){

    }

    public String getSetoranId() {
        return setoranId;
    }

    public void setSetoranId(String setoranId) {
        this.setoranId = setoranId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getJamsetoran() {
        return jamsetoran;
    }

    public void setJamsetoran(String jamsetoran) {
        this.jamsetoran = jamsetoran;
    }

    public String getHargaSusu() {
        return hargaSusu;
    }

    public void setHargaSusu(String hargaSusu) {
        this.hargaSusu = hargaSusu;
    }

    public String getJumlahSusu() {
        return jumlahSusu;
    }

    public void setJumlahSusu(String jumlahSusu) {
        this.jumlahSusu = jumlahSusu;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
