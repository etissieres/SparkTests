package com.viseo.spark.processor;

public class TemperatureUtils {

    public static double kelvinToCelcius(double kelvinDegrees) {
        return kelvinDegrees - 273.15;
    }
}
