package com.viseo.spark.processor;

public class InputMeasure {
    private final double kelvinDegrees;
    private final double bars;

    public InputMeasure(double kelvinDegrees, double bars) {
        this.kelvinDegrees = kelvinDegrees;
        this.bars = bars;
    }

    public double getKelvinDegrees() {
        return kelvinDegrees;
    }

    public double getBars() {
        return bars;
    }

    @Override
    public String toString() {
        return "InputMeasure(" + kelvinDegrees + " K; " + bars + " hPa)";
    }
}
