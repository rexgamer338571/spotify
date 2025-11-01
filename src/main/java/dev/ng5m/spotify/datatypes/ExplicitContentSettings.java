package dev.ng5m.spotify.datatypes;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

@ToString
public class ExplicitContentSettings {
    @SerializedName("filter_enabled")
    public boolean filterEnabled;
    @SerializedName("filter_locked")
    public boolean filterLocked;
}
