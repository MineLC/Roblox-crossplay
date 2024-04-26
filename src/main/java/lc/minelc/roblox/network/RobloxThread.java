package lc.minelc.roblox.network;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.tinylog.Logger;

import lc.lcspigot.roblox.RobloxData;
import lc.minelc.roblox.network.deserializer.JsonDeserializer;
import lc.minelc.roblox.network.serializer.JsonSerializer;
import lc.minelc.roblox.tasks.TaskManager;

public final class RobloxThread extends Thread {
    private static RobloxThread thread;

    private final HttpClient client = HttpClient.newHttpClient();
    private final String outUri, inUri;
    private final TaskManager taskManager = new TaskManager();
    private boolean run = true;

    private RobloxThread(String outUri, String inUri) {
        this.outUri = outUri;
        this.inUri = inUri;
    }

    @Override
    public void run() {
        while (run) {
            try {
                Thread.sleep(1000);
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void execute() {
        try {
            sendInRequest();
            sendOutRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendInRequest() throws IOException, InterruptedException {
        final HttpRequest inRequest = HttpRequest.newBuilder()
            .uri(URI.create(inUri))
            .GET()
            .build();

        final HttpResponse<String> inResponse = client.send(inRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("IN : " + inResponse.body());
        taskManager.execute(new JsonDeserializer(inResponse.body()));
    }

    private void sendOutRequest() throws IOException, InterruptedException {
        final String post = new JsonSerializer().createFormat();
        if (post == null) {
            return;
        }
        RobloxData.getInstance().clear();   

        final HttpRequest outRequest = HttpRequest.newBuilder()
            .uri(URI.create(outUri))
            .PUT(HttpRequest.BodyPublishers.ofString(post))
            .build();

        final HttpResponse<String> outResponse = client.send(outRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("OUT : " + outResponse.statusCode());
    }

    public static void iniciate(String outUri, String inUri) {
        if (outUri == null || inUri == null) {
            Logger.warn("Roblox uri can't be null");
            return;
        }
        RobloxData.start();
        thread = new RobloxThread(outUri, inUri);
        thread.start();
    }

    public static void stopThread() {
        if (thread != null) {
            thread.run = false;
        }
    }
}