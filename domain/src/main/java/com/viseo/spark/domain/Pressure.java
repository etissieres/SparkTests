package com.viseo.spark.domain;

public class Pressure {
    private final double bars;

    public Pressure(double bars) {
        this.bars = bars;
    }

    public double getBars() {
        return bars;
    }

    @Override
    public String toString() {
        return "Pressure(" + bars + " hPa)";
    }
}
