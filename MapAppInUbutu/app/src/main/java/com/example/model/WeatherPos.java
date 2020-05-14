package com.example.model;

public class WeatherPos {
    private String tenDiaDiem;
    private double kinhDo;
    private double viDo;
    private float nhietDo;
    private float doAm;
    private float chiSoBui;

    public WeatherPos() {
    }

    public WeatherPos(String tenDiaDiem, double kinhDo, double viDo, float nhietDo, float doAm, float chiSoBui) {
        this.tenDiaDiem = tenDiaDiem;
        this.kinhDo = kinhDo;
        this.viDo = viDo;
        this.nhietDo = nhietDo;
        this.doAm = doAm;
        this.chiSoBui = chiSoBui;
    }

    public double getKinhDo() {
        return kinhDo;
    }

    public void setKinhDo(double kinhDo) {
        this.kinhDo = kinhDo;
    }

    public double getViDo() {
        return viDo;
    }

    public void setViDo(double viDo) {
        this.viDo = viDo;
    }

    public String getTenDiaDiem() {
        return tenDiaDiem;
    }

    public void setTenDiaDiem(String tenDiaDiem) {
        this.tenDiaDiem = tenDiaDiem;
    }

    public float getNhietDo() {
        return nhietDo;
    }

    public void setNhietDo(float nhietDo) {
        this.nhietDo = nhietDo;
    }

    public float getDoAm() {
        return doAm;
    }

    public void setDoAm(float doAm) {
        this.doAm = doAm;
    }

    public float getChiSoBui() {
        return chiSoBui;
    }

    public void setChiSoBui(float chiSoBui) {
        this.chiSoBui = chiSoBui;
    }

    @Override
    public String toString() {
        return "WeatherPos{" +
                "tenDiaDiem='" + tenDiaDiem + '\'' +
                ", nhietDo=" + nhietDo +
                ", doAm=" + doAm +
                ", chiSoBui=" + chiSoBui +
                '}';
    }
}
