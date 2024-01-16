package gwaves.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import gwaves.sample.Episode;
import gwaves.collection.Podcast;
import gwaves.context.User;
import gwaves.context.Host;
import gwaves.misc.HostAnnouncement;

public final class HostPage implements Page {
    private Host owner;
    private LinkedHashMap<String, HostAnnouncement> announcements;
    private LinkedHashMap<String, Podcast> podcasts;

    /**
     *
     * @param announcements
     * @param podcasts
     */
    public HostPage(final Host owner, final LinkedHashMap<String, HostAnnouncement> announcements,
                           final LinkedHashMap<String, Podcast> podcasts) {
        this.owner = owner;
        this.announcements = announcements;
        this.podcasts = podcasts;
    }

    /**
     * @return type of page (useful for casting owner)
     */
    public Type type() {
        return Page.Type.OFHOST;
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
        int i = 0, j = 0;
        String page = "Podcasts:\n\t[";
        ArrayList<HostAnnouncement> announs = new ArrayList<>(this.announcements.values());
        ArrayList<Podcast> modifpodcasts = new ArrayList<>(this.podcasts.values());
        ArrayList<Episode> episodes;

        for (var podcast : modifpodcasts) {
            page += (podcast.getName() + ":\n\t[");

            episodes = podcast.getAudRecs();

            i = 0;
            for (var episode : episodes) {
                page += (episode.getName() + " - " + episode.getDescription());

                i++;
                if (i != episodes.size()) {
                    page += ", ";
                }
            }

            page += "]\n";

            j++;
            if (j != podcasts.size()) {
                page += ", ";
            }
        }

        page += "]\n\nAnnouncements:\n\t[";

        i = 0;
        for (var announ : announs) {
            page += (announ.getName() + ":\n\t");
            page += (announ.getDescription() + "\n");

            i++;
            if (i != announs.size()) {
                page += ", ";
            }
        }

        page += "]";

        return page;
    }
}
