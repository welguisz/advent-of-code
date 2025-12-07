package com.dwelguisz.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AoCClient {
    private static String RESOURCE_DIRECTORY = "/Users/davidwelguisz/coding/advent-of-code/src/resources/";

    int year;
    int day;
    ClassLoader classLoader;
    HttpClient client;
    String cookie;

    public AoCClient(int year, int day, ClassLoader classLoader) throws URISyntaxException {
        this.year = year;
        this.day = day;
        this.classLoader = classLoader;
        this.cookie = getCookie();
        this.client = createClient();
    }

    public void fetchData() throws IOException, InterruptedException {
        Instant allowedTime = allowedTime();
        Instant currentTime = Instant.now();
        if (allowedTime.isAfter(currentTime)) {
            throw new RuntimeException("Too early to access AoC. Please wait.");
        }
        String uri = "https://adventofcode.com/" + year + "/day/" + day + "/input";
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("User-Agent", "https://github.com/welguisz/advent-of-code/blob/main/src/com/dwelguisz/base/AoCClient.java by welguisz@gmail.com")
                .GET().build();

        String body = (client.send(req, HttpResponse.BodyHandlers.ofString()).body());

        Instant nextAllowedTime = Instant.now().plus(15, TimeUnit.MINUTES.toChronoUnit());

        writeToCurrentResourceDirectory(body);
        writeToLongLivedResourceDirectory(body);
        writeNewAllowedTimeToCurrentResourceDirectory(nextAllowedTime);
        writeNewAllowedTimeToLongLivedResourceDirectory(nextAllowedTime);
    }

    private HttpClient createClient() throws URISyntaxException {
        CookieHandler.setDefault(new CookieManager());

        HttpCookie sessionCookie = new HttpCookie("session", cookie);
        sessionCookie.setPath("/");
        sessionCookie.setVersion(0);

        ((CookieManager) CookieHandler.getDefault()).getCookieStore().add(new URI("https://adventofcode.com"),
                sessionCookie);

        return HttpClient.newBuilder()
                .cookieHandler(CookieHandler.getDefault())
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    private String getCookie() {
        InputStream inputStream = classLoader.getResourceAsStream("cookie_session_id.txt");
        if (inputStream == null) {
            throw new RuntimeException("Couldn't find cookie_session_id.txt");
        }
        List<String> lines = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Unable to read file" + e);
        }
        return lines.get(0);
    }

    private Instant allowedTime() {
        InputStream inputStream = classLoader.getResourceAsStream("next_allowed_time_to_fetch_from_advent_of_code.txt");
        Instant allowedTime = Instant.now().minus(1, TimeUnit.SECONDS.toChronoUnit());
        if (inputStream == null) {

        } else {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                System.out.println("Unable to read file" + e);
            }
            allowedTime = Instant.ofEpochMilli(Long.parseLong(lines.get(0)));
        }
        return allowedTime;
    }

    private void writeToCurrentResourceDirectory(String body) throws IOException {
        String resourcePath = classLoader.getResource("").getPath();
        String separator = FileSystems.getDefault().getSeparator();
        String directories[] = new String[]{resourcePath, String.format("year%d",year),String.format("day%2d",day).replace(" ","0")};
        Files.createDirectories(Path.of(String.join(separator, directories)));
        File newFile = new File(String.join(separator, directories) + "/input.txt");
        try (FileWriter writer = new FileWriter(newFile)){
            writer.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToLongLivedResourceDirectory(String body) throws IOException {
        String separator = FileSystems.getDefault().getSeparator();
        String directories[] = new String[]{RESOURCE_DIRECTORY, String.format("year%d",year),String.format("day%2d",day).replace(" ","0")};
        Files.createDirectories(Path.of(String.join(separator, directories)));
        File newFile = new File(String.join(separator, directories) + "/input.txt");
        try (FileWriter writer = new FileWriter(newFile)){
            writer.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeNewAllowedTimeToCurrentResourceDirectory(Instant allowedTime) {
        String resourcePath = classLoader.getResource("").getPath();
        String separator = FileSystems.getDefault().getSeparator();
        try (FileWriter writer = new FileWriter(resourcePath+separator+ "next_allowed_time_to_fetch_from_advent_of_code.txt")) {
            writer.write(""+allowedTime.toEpochMilli());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNewAllowedTimeToLongLivedResourceDirectory(Instant allowedTime) {
        String separator = FileSystems.getDefault().getSeparator();
        try (FileWriter writer = new FileWriter(RESOURCE_DIRECTORY+separator+ "next_allowed_time_to_fetch_from_advent_of_code.txt")) {
            writer.write(""+allowedTime.toEpochMilli());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
