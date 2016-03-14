package com.viseo.spark.domain;

public class Temperature {
    private final double celciusDegrees;

    public Temperature(double celciusDegrees) {
        this.celciusDegrees = celciusDegrees;
    }

    public double getCelciusDegrees() {
        return celciusDegrees;
    }

    @Override
    public String toString() {
        return "Temperature(" + celciusDegrees + " °C)";
    }
}
