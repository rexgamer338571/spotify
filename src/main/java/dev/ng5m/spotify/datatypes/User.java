package dev.ng5m.spotify.datatypes;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

import java.util.List;

@ToString
public class User {
    public String country;
    @SerializedName("display_name")
    public String displayName;
    public String email;
    @SerializedName("explicit_content")
    public ExplicitContentSettings explicitContentSettings;
    @SerializedName("external_urls")
    public ExternalURLs externalURLs;
    public Followers followers;
    public String href;
    public String id;
    public List<ImageObject> images;
    public String product;
    public String type;
    public String uri;
}
