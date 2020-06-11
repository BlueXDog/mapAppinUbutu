package com.example.model;

public class WeatherInfo {
    private float doam;
    private float dobui;
    private float nhietdo;



    public WeatherInfo() {
    }

    public WeatherInfo(float doam, float dobui, float nhietdo) {
        this.doam = doam;
        this.dobui = dobui;
        this.nhietdo = nhietdo;
    }

    public float getDoam() {
        return doam;
    }

    public void setDoam(float doam) {
        this.doam = doam;
    }

    public float getDobui() {
        return dobui;
    }

    public void setDobui(float dobui) {
        this.dobui = dobui;
    }

    public float getNhietdo() {
        return nhietdo;
    }

    public void setNhietdo(float nhietdo) {
        this.nhietdo = nhietdo;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "doam=" + doam +
                ", dobui=" + dobui +
                ", nhietdo=" + nhietdo +
                '}';
    }
}
