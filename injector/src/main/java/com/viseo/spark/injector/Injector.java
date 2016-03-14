package com.viseo.spark.injector;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Injector {
    private static final Random rand = new Random();

    private static final double MIN_PRESSURE = 0.96;
    private static final double MAX_PRESSURE = 1.04;
    private static final double MIN_TEMPERATURE = 274;
    private static final double MAX_TEMPERATURE = 280;

    public static void main(String... args) throws Exception {
        ServerSocket server = new ServerSocket(1337);

        while (true) {
            final Socket client = server.accept();
            System.out.println("New client : " + client.getInetAddress());

            (new Thread(() -> {
                try {
                    OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());

                    while (true) {
                        String payload = generatePayload();
                        System.out.println("[" + Thread.currentThread().getName() + "] Sending : " + payload);
                        out.append(payload).append("\n");
                        out.flush();
                        Thread.sleep(rand(100, 1500));
                    }
                } catch (IOException | InterruptedException e) {
                    System.err.println("Error in thread " + Thread.currentThread().getName());
                    e.printStackTrace();
                }
            })).start();
        }
    }

    private static String generatePayload() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("temperature", rand(MIN_TEMPERATURE, MAX_TEMPERATURE));
        jsonObject.addProperty("pressure", rand(MIN_PRESSURE, MAX_PRESSURE));
        return jsonObject.toString();
    }

    private static double rand(double min, double max) {
        return rand.nextDouble() * (max - min) + min;
    }

    private static int rand(int min, int max) {
        return rand.nextInt(1) * (max - min) + min;
    }
}
