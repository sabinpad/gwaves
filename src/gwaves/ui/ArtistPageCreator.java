package gwaves.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import gwaves.collection.Album;
import gwaves.misc.ArtistEvent;
import gwaves.misc.ArtistMerch;

public class ArtistPageCreator implements PageCreator {
    private LinkedHashMap<String, ArtistEvent> events;
    private LinkedHashMap<String, ArtistMerch> merches;
    private LinkedHashMap<String, Album> albums;

    /**
     *
     * @param events
     * @param merches
     * @param albums
     */
    public ArtistPageCreator(final LinkedHashMap<String, ArtistEvent> events,
                             final LinkedHashMap<String, ArtistMerch> merches,
                             final LinkedHashMap<String, Album> albums) {
        this.events = events;
        this.merches = merches;
        this.albums = albums;
    }

    /**
     *
     */
    public String createPage() {
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
