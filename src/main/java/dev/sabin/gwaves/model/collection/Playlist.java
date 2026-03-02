package dev.sabin.gwaves.model.collection;

import dev.sabin.gwaves.model.io.input.FilterInput;

import dev.sabin.gwaves.model.sample.Song;
import dev.sabin.gwaves.service.util.Filterable;

public final class Playlist extends AudioCollection<Song> implements Filterable {
    private Integer followers;
    private Boolean visibility;

    /**
     * Creates empty playlist
     *
     * @param name  of Playlist
     * @param owner name of Playlist owner
     */
    public Playlist(final String name, final String owner) {
        super(name, owner);

        this.followers = 0;
        this.visibility = true;
    }

    /**
     * Adds a follow to the song
     */
    public void addFollower() {
        this.followers++;
    }

    /**
     * Removes a like from the song
     */
    public void removeFollower() {
        if (this.followers > 0) {
            this.followers--;
        }
    }

    /**
     * changes the visibility of the playlist
     */
    public void changeVisibility() {
        this.visibility = !this.visibility;
    }

    /**
     * @return the number of followers the playlist has
     */
    public int getNrOfFollowers() {
        return this.followers.intValue();
    }

    /**
     *
     * @return
     */
    public int getNrOfLikes() {
        int sum = 0;

        for (var rec : this.getAudRecs()) {
            sum += rec.getLikes();
        }

        return sum;
    }

    /**
     * @return true if the playlist is visible
     */
    public boolean isVisible() {
        return this.visibility;
    }

    /**
     * @param filter used to match
     * @return true if the playlist is matched by the filter
     */
    public boolean isMatchedByFilter(final FilterInput filter) {
        if (filter.getName() != null) {
            if (!this.getName().startsWith(filter.getName())) {
                return false;
            }
        }

        if (filter.getOwner() != null) {
            if (!this.getOwner().equals(filter.getOwner())) {
                return false;
            }
        }

        return true;
    }
}
