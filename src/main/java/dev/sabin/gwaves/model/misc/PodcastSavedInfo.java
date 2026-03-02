package dev.sabin.gwaves.model.misc;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter @Setter @AllArgsConstructor
public class PodcastSavedInfo {
    private int lastEpisodePlayedIndex;
    private int lastEpisodePlayedRemainingTime;
}
