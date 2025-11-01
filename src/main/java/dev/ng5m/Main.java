package dev.ng5m;

import dev.ng5m.spotify.Spotify;
import dev.ng5m.spotify.datatypes.*;

import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AccessToken accessToken = Spotify.requestUserAuthorization(
                "user-library-read",
                "user-read-private", "user-read-email",
                "playlist-modify-public", "playlist-modify-private"
        );
        User profile = Spotify.getCurrentUserProfile(accessToken);

        SavedTracks savedTracks = Spotify.getSavedTracks(accessToken);
        shuffleSavedTracks(accessToken, profile, savedTracks);
    }

    public static void shuffleSavedTracks(AccessToken accessToken, User user, SavedTracks savedTracks) {
        List<SavedTracks> allPages = Spotify.getAllPages(accessToken, savedTracks);

        PlaylistIC created = Spotify.createPlaylist(accessToken, user,
                PlaylistOG.builder()
                        .public_(false)
                        .collaborative(false)
                        .name("test")
                        .description("test desc")
                        .build());

        Collections.shuffle(allPages);

        int ix = 0;
        for (SavedTracks page : allPages) {
            Collections.shuffle(page.items);
            Spotify.addItemsToPlaylist(accessToken, created, ix, page.items
                    .stream()
                    .map(o -> o.track)
                    .toArray(SavedTrackObject.Track[]::new));

            ix += page.items.size();
        }

    }
}