package dev.ng5m.spotify.datatypes;

import lombok.ToString;

import java.util.List;

@ToString
public class SavedTracks extends Page {

    public String href;
    public int limit;
    public int offset;
    public int total;
    public List<SavedTrackObject> items;

}
