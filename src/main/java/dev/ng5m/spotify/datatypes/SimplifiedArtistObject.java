package dev.ng5m.spotify.datatypes;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

@ToString
public class SimplifiedArtistObject {
    @SerializedName("external_urls")
    public ExternalURLs externalURLs;
    public String href;
    public String id;
    public String name;
    public String type;
    public String uri;
}
