package com.viseo.spark.processor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viseo.spark.domain.Pressure;
import com.viseo.spark.domain.Temperature;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

public class Adapter {

    public static void main(String... args) {

        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("spark");
        JavaStreamingContext sc = new JavaStreamingContext(conf, Durations.seconds(1));

        JavaReceiverInputDStream<String> rawMeasures = sc.socketTextStream("localhost", 1337);

        JavaDStream<InputMeasure> inputMeasures = rawMeasures.map(rawMeasure -> {
            JsonParser parser = new JsonParser();
            JsonElement item = parser.parse(rawMeasure);
            JsonObject jsonMeasure = item.getAsJsonObject();

            double kelvins = jsonMeasure.get("temperature").getAsDouble();
            double bars = jsonMeasure.get("pressure").getAsDouble();
            InputMeasure inputMeasure = new InputMeasure(kelvins, bars);

            System.out.println("Received : " + inputMeasure);
            return inputMeasure;
        });

        JavaDStream<Tuple2<Temperature, Pressure>> measures = inputMeasures.map(inputMeasure -> {
            double celciusDegrees = TemperatureUtils.kelvinToCelcius(inputMeasure.getKelvinDegrees());
            Temperature temperature = new Temperature(celciusDegrees);
            Pressure pressure = new Pressure(inputMeasure.getBars());

            System.out.println("Parsed : " + temperature + "; " + pressure);
            return new Tuple2<>(temperature, pressure);
        });

        JavaDStream<Double> temperatures = measures.map(tuple -> tuple._1.getCelciusDegrees());

        JavaDStream<Double> temperaturesWindows = temperatures.window(
            Durations.seconds(60),  // window of 60 seconds...
            Durations.seconds(3)  // ... computed every 3 seconds
        );

        temperaturesWindows.foreachRDD(rdd -> {
            System.out.println("Mean temperature (last minute) : " + rdd.mapToDouble(x -> x).mean());
        });

        sc.start();
        sc.awaitTermination();
    }
}
