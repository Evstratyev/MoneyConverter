package com.example.orin1.moneyconverter;

import android.support.annotation.NonNull;

public class Currency implements Comparable<Currency> {
    private String name;
    private Double rate;

    public Currency(String name, Double rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public int compareTo(@NonNull Currency o) {
        return getName().compareTo(o.getName());
    }
}
