package com.example.emowwrevisi.Model;

public class ModelKualitasPet {
    private String kualitasId,tanggal,namapeternak,namakoperasi,parameterkualitas,satuankualitas,hasil,bonus,jumlahkualitas,uid;

    public ModelKualitasPet(String kualitasId, String tanggal, String namapeternak, String namakoperasi, String parameterkualitas, String satuankualitas, String hasil, String bonus, String jumlahkualitas, String uid) {
        this.kualitasId = kualitasId;
        this.tanggal = tanggal;
        this.namapeternak = namapeternak;
        this.namakoperasi = namakoperasi;
        this.parameterkualitas = parameterkualitas;
        this.satuankualitas = satuankualitas;
        this.hasil = hasil;
        this.bonus = bonus;
        this.jumlahkualitas = jumlahkualitas;
        this.uid = uid;
    }

    public ModelKualitasPet(){

    }

    public String getKualitasId() {
        return kualitasId;
    }

    public void setKualitasId(String kualitasId) {
        this.kualitasId = kualitasId;
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

    public String getParameterkualitas() {
        return parameterkualitas;
    }

    public void setParameterkualitas(String parameterkualitas) {
        this.parameterkualitas = parameterkualitas;
    }

    public String getSatuankualitas() {
        return satuankualitas;
    }

    public void setSatuankualitas(String satuankualitas) {
        this.satuankualitas = satuankualitas;
    }

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getJumlahkualitas() {
        return jumlahkualitas;
    }

    public void setJumlahkualitas(String jumlahkualitas) {
        this.jumlahkualitas = jumlahkualitas;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
