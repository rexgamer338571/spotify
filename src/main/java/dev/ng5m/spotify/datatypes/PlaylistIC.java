package dev.ng5m.spotify.datatypes;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ToString
public class PlaylistIC extends Playlist {
    public boolean collaborative;
    public @Nullable String description;
    @SerializedName("external_urls")
    public ExternalURLs externalURLs;
    public String href;
    public List<ImageObject> images;
    public String name;
    public Owner owner;
    @SerializedName("public")
    public boolean public_;
    @SerializedName("snapshot_id")
    public String snapshotId;
    // TODO finish

    @ToString
    public static class Owner {
        @SerializedName("external_urls")
        public ExternalURLs externalURLs;
        public String href;
        public String id;
        public String type;
        public String uri;
        @SerializedName("display_name")
        public @Nullable String displayName;
    }
}
