package dev.ng5m.spotify;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sun.net.httpserver.HttpServer;
import dev.ng5m.AccessToken;
import dev.ng5m.RequestBuilder;
import dev.ng5m.ResponseHandler;
import dev.ng5m.Util;
import dev.ng5m.spotify.datatypes.*;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Spotify {
    private static final String CLIENT_ID = System.getenv("CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("CLIENT_SECRET");
    private static final int PORT = 9827;
    private static final String REDIRECT_URI = "http://127.0.0.1:" + PORT;

    public static AccessToken requestUserAuthorization(String... scopes) {
        try {
            AtomicReference<String> code = new AtomicReference<>();

            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 1);
            server.createContext("/", exchange -> {
                String path = exchange.getRequestURI().toString().substring(2);
                Map<String, String> query = Util.decodeQueryString(path);

                if (!query.containsKey("code") && !query.containsKey("error")) {
                    exchange.sendResponseHeaders(403, 0);
                } else {
                    int rCode;
                    byte[] bytes;

                    if (query.containsKey("error")) {
                        rCode = 401;
                        String res = query.get("error");
                        bytes = res.getBytes(StandardCharsets.UTF_8);

                    } else {
                        code.set(query.get("code"));

                        rCode = 200;
                        bytes = "authenticated".getBytes(StandardCharsets.UTF_8);
                    }

                    exchange.sendResponseHeaders(rCode, bytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(bytes);
                    os.close();
                }

                server.stop(0);
            });

            server.setExecutor(null);
            server.start();

            Desktop.getDesktop().browse(URI.create("https://accounts.spotify.com/authorize?response_type=code&client_id=%s&scope=%s&redirect_uri=%s"
                    .formatted(
                            CLIENT_ID, URLEncoder.encode(String.join(" ", scopes), StandardCharsets.UTF_8), REDIRECT_URI
                    )));

            Util.waitFor(() -> code.get() != null);

            return Util.HTTPRequest(
                    "https://accounts.spotify.com/api/token", "POST",
                    RequestBuilder.newBuilder()
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .header("Authorization", "Basic " +
                                    Base64.getEncoder().encodeToString(
                                            (CLIENT_ID + ":" + CLIENT_SECRET).getBytes(StandardCharsets.UTF_8)
                                    )
                            )

                            .param("code", code.get())
                            .param("redirect_uri", REDIRECT_URI)
                            .param("grant_type", "authorization_code"),
                    (rCode, text) -> Util.GSON.fromJson(text, AccessToken.class)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T makeAPIRequest(String method, String endpoint, RequestBuilder requestBuilder, ResponseHandler<T> responseHandler,
                                       @Nullable AccessToken accessToken) {
        return makeAuthorizedRequest(
                method, "https://api.spotify.com/v1" + endpoint,
                requestBuilder, responseHandler, accessToken
        );
    }

    public static <T> T makeAuthorizedRequest(String method, String url, RequestBuilder requestBuilder, ResponseHandler<T> responseHandler,
                                       @Nullable AccessToken accessToken) {
        if (accessToken != null)
            requestBuilder.header("Authorization", accessToken.toTokenString());

        return Util.HTTPRequest(
                url,
                method,
                requestBuilder,
                responseHandler
        );
    }

    public static <T extends Page> List<T> getAllPages(AccessToken accessToken, T page) {
        final List<T> list = new ArrayList<>();
        @SuppressWarnings("unchecked")
        final ResponseHandler<T> responseHandler = ResponseHandler.gsonConverterHandler((Class<T>) page.getClass());
        list.add(page);

        T current = page;
        // backtrack
        while (current.previous != null) {
            current = makeAuthorizedRequest(
                    "GET", current.previous, RequestBuilder.newBuilder(),
                    responseHandler, accessToken
            );
            list.add(current);
        }

        current = page;
        while (current.next != null) {
            current = makeAuthorizedRequest(
                    "GET", current.next, RequestBuilder.newBuilder(),
                    responseHandler, accessToken
            );
            list.add(current);
        }

        return list;
    }

    public static SavedTracks getSavedTracks(AccessToken accessToken) {
        return makeAPIRequest(
                "GET", "/me/tracks",
                RequestBuilder.newBuilder(),
                ResponseHandler.gsonConverterHandler(SavedTracks.class),
                accessToken
        );
    }

    public static User getCurrentUserProfile(AccessToken accessToken) {
        return makeAPIRequest(
                "GET", "/me",
                RequestBuilder.newBuilder(),
                ResponseHandler.gsonConverterHandler(User.class),
                accessToken
        );
    }

    public static PlaylistIC createPlaylist(AccessToken accessToken, User user, PlaylistOG playlist) {
        return makeAPIRequest(
                "POST", "/users/%s/playlists".formatted(user.id),
                RequestBuilder.newBuilder()
                        .header("Content-Type", "application/json")

                        .body(Util.GSON.toJson(playlist).getBytes(StandardCharsets.UTF_8)),
                ResponseHandler.gsonConverterHandler(PlaylistIC.class),
                accessToken
        );
    }

    public static String addItemsToPlaylist(AccessToken accessToken, Playlist playlist,
                                          int position, Resource... resources) {
        JsonObject object = new JsonObject();
        JsonArray uris = new JsonArray();
        for (Resource res : resources) {
            uris.add(new JsonPrimitive(res.uri));
        }

        object.add("uris", uris);

        if (position != -1)
            object.add("position", new JsonPrimitive(position));

        return makeAPIRequest(
                "POST", "/playlists/%s/tracks".formatted(playlist.id),
                RequestBuilder.newBuilder()
                        .header("Content-Type", "application/json")

                        .body(object.toString().getBytes(StandardCharsets.UTF_8)),
                (code, text) -> JsonParser.parseString(text)
                        .getAsJsonObject()
                        .get("snapshot_id")
                        .getAsString(),
                accessToken
        );
    }

}
