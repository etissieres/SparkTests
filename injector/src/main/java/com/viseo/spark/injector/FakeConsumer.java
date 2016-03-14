package com.viseo.spark.injector;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class FakeConsumer {

    public static void main(String... args) throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(1337));

        InputStream is = socket.getInputStream();
        Scanner scanner = new Scanner(is);
        while (true) {
            System.out.println("Waiting...");
            String line = scanner.nextLine();
            System.out.println(line);
        }
    }
}
