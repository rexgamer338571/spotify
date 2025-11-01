package dev.ng5m;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Util {
    public static final Gson GSON = new Gson();

    public static Map<String, String> decodeQueryString(String s) {
        Map<String, String> map = new HashMap<>();

        for (String entry : s.split("&")) {
            String[] spl = entry.split("=");
            map.put(spl[0], spl[1]);
        }

        return map;
    }

    public static <T> T HTTPRequest(String url, String method, RequestBuilder requestBuilder, ResponseHandler<T> responseHandler) {
        try {
            final String queryString = requestBuilder.params()
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));


            boolean isGET = "GET".equalsIgnoreCase(method);

            if (!queryString.isEmpty() && isGET) {
                url += "?" + queryString;
            }

            URL url_ = new URI(url).toURL();


            HttpURLConnection conn = (HttpURLConnection) url_.openConnection();



            conn.setRequestMethod(method);

            for (var entry : requestBuilder.headers().entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            conn.setDoOutput(!isGET);

            if (!isGET) {
                byte[] qsBytes = queryString.getBytes(StandardCharsets.UTF_8);
                conn.setFixedLengthStreamingMode(qsBytes.length + requestBuilder.body().length);
                conn.setRequestProperty("Content-Type",
                        requestBuilder.headers().getOrDefault("Content-Type", "application/x-www-form-urlencoded"));

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(qsBytes);
                    os.write(requestBuilder.body());
                }
            }

            int responseCode = conn.getResponseCode();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                content.append(inputLine);
            }

            reader.close();

            return responseHandler.handle(responseCode, content.toString());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void waitFor(BooleanSupplier getter) {
        while (!getter.get()) {
        }
    }

}
