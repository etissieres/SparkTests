package com.viseo.spark.processor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

public class Main {

    public static void main(String... args) {

        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("spark");
        JavaStreamingContext sc = new JavaStreamingContext(conf, Durations.seconds(1));

        JavaReceiverInputDStream<String> rawMeasures = sc.socketTextStream("localhost", 1337);

        JavaDStream<InputMeasure> inputMeasures = rawMeasures.map(rawMeasure -> {
            JsonParser parser = new JsonParser();
            JsonElement item = parser.parse(rawMeasure);
            JsonObject jsonMeasure = item.getAsJsonObject();
            double kelvins = jsonMeasure.get("temperature").getAsDouble();
            int bars = jsonMeasure.get("pressure").getAsInt();
            return new InputMeasure(kelvins, bars);
        });

        JavaDStream<Tuple2<Temperature, Pressure>> measures = inputMeasures.map(inputMeasure -> {
            Temperature temperature = new Temperature(inputMeasure.kelvins - 273.15);
            Pressure pressure = new Pressure(inputMeasure.bars);
            return new Tuple2<>(temperature, pressure);
        });

        measures.foreachRDD(rdd -> {
            rdd.foreach(pair -> {
                System.out.println("Température " + pair._1.degrees + " °C; Pression " + pair._2.bars + " bars");
            });
        });

        sc.start();
        sc.awaitTermination();
    }
}
