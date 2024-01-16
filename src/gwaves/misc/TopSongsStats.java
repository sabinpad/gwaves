package gwaves.misc;

import java.util.Collection;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

import gwaves.sample.Song;
import gwaves.util.Statistics;

@AllArgsConstructor
public final class TopSongsStats implements Statistics {
    private Collection<Song> songs;

    /**
     * Computes top 5 songs by number of likes
     * @return list of songs name
     */
    public List<String> results() {
        return songs.stream()
                    .sorted(Comparator.comparing(Song::getLikes).reversed())
                    .limit(MAXNUMBER)
                    .map(Song::getName)
                    .collect(Collectors.toList());
    }
}
