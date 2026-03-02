package dev.sabin.gwaves.model.misc;

import java.util.Collection;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

import dev.sabin.gwaves.model.context.Artist;
import dev.sabin.gwaves.service.util.Statistics;

@AllArgsConstructor
public final class TopArtistsStats  implements Statistics {
    private Collection<Artist> artists;

    /**
     * Computes top 5 artists by number of likes
     * @return list of artists name
     */
    public List<String> results() {
        return artists.stream()
                      .sorted(Comparator.comparing(Artist::getNrOfLikes).reversed())
                      .limit(MAXNUMBER)
                      .map(Artist::getUsername)
                      .collect(Collectors.toList());
    }
}
