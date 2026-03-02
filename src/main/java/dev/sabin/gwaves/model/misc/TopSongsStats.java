package dev.sabin.gwaves.model.misc;

import java.util.Collection;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

import dev.sabin.gwaves.model.sample.Song;
import dev.sabin.gwaves.service.util.Statistics;

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
