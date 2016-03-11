package com.viseo.spark.provider;

import com.google.gson.JsonObject;

import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Main {
    private static final Random rand = new Random();

    public static void main(String... args) throws Exception {
        ServerSocket server = new ServerSocket(1337);
        Socket client = server.accept();
        OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());

        while (true) {
            String data = gen();
            System.out.println("Sending > " + data);
            out.append(data).append("\n");
            out.flush();
            Thread.sleep(randSleep());
        }
    }

    private static String gen() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("temperature", randTemperature());
        jsonObject.addProperty("pressure", randPressure());
        return jsonObject.toString();
    }

    private static double randTemperature() {
        return rand.nextDouble() * (300 - 260 + 1) + 260;
    }

    private static int randPressure() {
        return rand.nextInt(15 - 2 + 1) + 2;
    }

    private static int randSleep() {
        return rand.nextInt(2000 - 100 + 1) + 100;
    }
}
