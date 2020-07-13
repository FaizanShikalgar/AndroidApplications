package com.example.fdcalculator;

public class FDstore {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String key;
    public int principalAmount;
    public double rate;

    public double getSimpleIntrest() {
        return simpleIntrest;
    }

    public void setSimpleIntrest(double simpleIntrest) {
        this.simpleIntrest = simpleIntrest;
    }

    public double simpleIntrest;
    public int getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(int principalAmount) {
        this.principalAmount = principalAmount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSartDate() {
        return sartDate;
    }

    public void setSartDate(String sartDate) {
        this.sartDate = sartDate;
    }

    public int duration;
    public String sartDate;

    public FDstore() {

    }

    public FDstore(int principalAmount, double rate, int duration, String sartDate,double simpleIntrest,String key) {
        this.principalAmount = principalAmount;
        this.rate = rate;
        this.duration = duration;
        this.sartDate = sartDate;
        this.simpleIntrest = simpleIntrest;
        this.key = key;
    }
}
