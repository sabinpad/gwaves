package dev.sabin.gwaves.model.misc;

import java.util.Collection;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

import dev.sabin.gwaves.model.collection.Playlist;
import dev.sabin.gwaves.service.util.Statistics;

@AllArgsConstructor
public final class TopPlaylistsStat  implements Statistics {
    private Collection<Playlist> playlists;

    /**
     * Computes top 5 playlists by number of followers
     * @return list of playlists name
     */
    public List<String> results() {
        return playlists.stream()
                        .sorted(Comparator.comparing(Playlist::getNrOfFollowers).reversed())
                        .limit(MAXNUMBER)
                        .map(Playlist::getName)
                        .collect(Collectors.toList());
    }
}
