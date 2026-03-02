package dev.sabin.gwaves.model.misc;

import java.util.Collection;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

import dev.sabin.gwaves.model.collection.Album;
import dev.sabin.gwaves.service.util.Statistics;

@AllArgsConstructor
public final class TopAlbumsStats  implements Statistics {
    private Collection<Album> albums;

    /**
     * Computes top 5 albums by number of likes
     * @return list of albums name
     */
    public List<String> results() {
        return albums.stream()
                     .sorted(Comparator.comparing(Album::getNrOfLikes).reversed())
                     .limit(MAXNUMBER)
                     .map(Album::getName)
                     .collect(Collectors.toList());
    }
}
