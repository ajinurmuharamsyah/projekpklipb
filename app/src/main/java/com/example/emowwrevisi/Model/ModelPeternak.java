package com.example.emowwrevisi.Model;

public class ModelPeternak {
    private String uid,username,email,name,tempatlahir,tanggallahir,pendidikan,timestamp,accountType,online,profil_image,anggotakoperasi,anggotabaru,validasi,negara,provinsi,kota,umur;

    public ModelPeternak(String uid, String username, String email, String name, String tempatlahir, String tanggallahir, String pendidikan, String timestamp, String accountType, String online, String profil_image, String anggotakoperasi, String anggotabaru, String validasi, String negara, String provinsi, String kota, String umur) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.name = name;
        this.tempatlahir = tempatlahir;
        this.tanggallahir = tanggallahir;
        this.pendidikan = pendidikan;
        this.timestamp = timestamp;
        this.accountType = accountType;
        this.online = online;
        this.profil_image = profil_image;
        this.anggotakoperasi = anggotakoperasi;
        this.anggotabaru = anggotabaru;
        this.validasi = validasi;
        this.negara = negara;
        this.provinsi = provinsi;
        this.kota = kota;
        this.umur = umur;
    }

    public ModelPeternak(){

    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTempatlahir() {
        return tempatlahir;
    }

    public void setTempatlahir(String tempatlahir) {
        this.tempatlahir = tempatlahir;
    }

    public String getTanggallahir() {
        return tanggallahir;
    }

    public void setTanggallahir(String tanggallahir) {
        this.tanggallahir = tanggallahir;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getProfil_image() {
        return profil_image;
    }

    public void setProfil_image(String profil_image) {
        this.profil_image = profil_image;
    }

    public String getAnggotakoperasi() {
        return anggotakoperasi;
    }

    public void setAnggotakoperasi(String anggotakoperasi) {
        this.anggotakoperasi = anggotakoperasi;
    }

    public String getAnggotabaru() {
        return anggotabaru;
    }

    public void setAnggotabaru(String anggotabaru) {
        this.anggotabaru = anggotabaru;
    }

    public String getValidasi() {
        return validasi;
    }

    public void setValidasi(String validasi) {
        this.validasi = validasi;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }
}
