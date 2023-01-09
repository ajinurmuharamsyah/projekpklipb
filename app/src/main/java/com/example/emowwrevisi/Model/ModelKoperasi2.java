package com.example.emowwrevisi.Model;

public class ModelKoperasi2 {
    private String uid,username,email,owner,koperasiname,berdiri,sejarah,negara,provinsi,kota,timestamp,accountType,online,profil_image;

    public ModelKoperasi2(String uid, String username, String email, String owner, String koperasiname, String berdiri, String sejarah, String negara, String provinsi, String kota, String timestamp, String accountType, String online, String profil_image) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.owner = owner;
        this.koperasiname = koperasiname;
        this.berdiri = berdiri;
        this.sejarah = sejarah;
        this.negara = negara;
        this.provinsi = provinsi;
        this.kota = kota;
        this.timestamp = timestamp;
        this.accountType = accountType;
        this.online = online;
        this.profil_image = profil_image;
    }

    public ModelKoperasi2(){

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getKoperasiname() {
        return koperasiname;
    }

    public void setKoperasiname(String koperasiname) {
        this.koperasiname = koperasiname;
    }

    public String getBerdiri() {
        return berdiri;
    }

    public void setBerdiri(String berdiri) {
        this.berdiri = berdiri;
    }

    public String getSejarah() {
        return sejarah;
    }

    public void setSejarah(String sejarah) {
        this.sejarah = sejarah;
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
}
