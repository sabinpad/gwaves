package fileio.output;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WrappedOutput {
    private Object topArtists;
    private Object topGenres;
    private Object topSongs;
    private Object topAlbums;
    private Object topEpisodes;
}
