package dev.sabin.gwaves.model.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import dev.sabin.gwaves.model.collection.Album;
import dev.sabin.gwaves.model.context.User;
import dev.sabin.gwaves.model.context.Artist;
import dev.sabin.gwaves.model.misc.ArtistEvent;
import dev.sabin.gwaves.model.misc.ArtistMerch;

public final class ArtistPage implements Page {
    private Artist owner;
    private LinkedHashMap<String, ArtistEvent> events;
    private LinkedHashMap<String, ArtistMerch> merches;
    private LinkedHashMap<String, Album> albums;

    /**
     *
     * @param events
     * @param merches
     * @param albums
     */
    public ArtistPage(final Artist owner, final LinkedHashMap<String, ArtistEvent> events,
                             final LinkedHashMap<String, ArtistMerch> merches,
                             final LinkedHashMap<String, Album> albums) {
        this.owner = owner;
        this.events = events;
        this.merches = merches;
        this.albums = albums;
    }

    /**
     * @return type of page (useful for casting owner)
     */
    public Type type() {
        return Page.Type.OFARTIST;
    }

    /**
     * @return owner user of the page
     */
    public User owner() {
        return this.owner;
    }

    /**
     * @return the contens of the page in String format
     */
    public String strigify() {
        int i = 0;
        String page = "Albums:\n\t[";
        ArrayList<ArtistEvent> modifevents = new ArrayList<>(this.events.values());
        ArrayList<ArtistMerch> modifmerches = new ArrayList<>(this.merches.values());
        ArrayList<Album> modifalbums = new ArrayList<>(this.albums.values());

        for (var album : modifalbums) {
            page += album.getName();

            i++;
            if (i != albums.size()) {
                page += ", ";
            }
        }

        page += "]\n\nMerch:\n\t[";

        i = 0;
        for (var merch : modifmerches) {
            page += (merch.getName() + " - " + merch.getPrice());
            page += (":\n\t" + merch.getDescription());

            i++;
            if (i != merches.size()) {
                page += ", ";
            }
        }

        page += "]\n\nEvents:\n\t[";

        i = 0;
        for (var event : modifevents) {
            page += (event.getName() + " - " + event.getDate());
            page += (":\n\t" + event.getDescription());

            i++;
            if (i != events.size()) {
                page += ", ";
            }
        }

        page += "]";

        return page;
    }
}
