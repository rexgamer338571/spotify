package dev.ng5m.spotify.datatypes;

import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@ToString
public class Followers {
    public @Nullable String href;
    public int total;

}
