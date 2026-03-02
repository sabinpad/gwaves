package dev.sabin.gwaves.model.io.output;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WrappedOutput {
    private Object topArtists;
    private Object topGenres;
    private Object topSongs;
    private Object topAlbums;
    private Object topEpisodes;
    private Object topFans;
    private Integer listeners;
}
