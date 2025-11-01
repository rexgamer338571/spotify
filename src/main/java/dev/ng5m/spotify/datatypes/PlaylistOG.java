package dev.ng5m.spotify.datatypes;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class PlaylistOG {
    public String name;
    @Builder.Default
    @SerializedName("public")
    public boolean public_ = true;
    @Builder.Default
    public boolean collaborative = false;
    public String description;
}
