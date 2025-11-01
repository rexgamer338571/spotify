package dev.ng5m.spotify.datatypes;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ToString
public class SavedTrackObject {

    @SerializedName("added_at")
    public String addedAt;

    public Track track;

    @ToString
    public static class Track extends Resource {
        public Album album;
        public List<SimplifiedArtistObject> artists;
        @SerializedName("available_markets")
        public List<String> availableMarkets;
        @SerializedName("disc_number")
        public int discNumber;
        @SerializedName("duration_ms")
        public long durationMS;
        public boolean explicit;
        @SerializedName("external_ids")
        public ExternalIDs externalIDs;
        @SerializedName("external_urls")
        public ExternalURLs externalURLs;
        public String href;
        public String id;
        @SerializedName("is_playable")
        public boolean isPlayable;
        @SerializedName("linked_from")
        public Object linkedFrom;
        public Restrictions restrictions;
        public String name;
        public int popularity;
        @Deprecated
        @SerializedName("preview_url")
        public @Nullable String previewURL;
        @SerializedName("track_number")
        public int trackNumber;
        public String type;
        @SerializedName("is_local")
        public boolean isLocal;

        @ToString
        public static class Album {
            @SerializedName("album_type")
            public String albumType;
            @SerializedName("total_tracks")
            public int totalTracks;
            @SerializedName("available_markets")
            public List<String> availableMarkets;
            @SerializedName("external_urls")
            public ExternalURLs externalURLs;
            public String href;
            public String id;
            public List<ImageObject> images;
            public String name;
            @SerializedName("release_date")
            public String releaseDate;
            @SerializedName("release_date_precision")
            public String releaseDatePrecision;
            public Restrictions restrictions;
            public String type;
            public String uri;
            public List<SimplifiedArtistObject> artists;


        }
    }

}
