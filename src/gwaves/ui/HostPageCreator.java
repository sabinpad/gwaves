package gwaves.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import gwaves.sample.Episode;
import gwaves.collection.Podcast;
import gwaves.misc.HostAnnouncement;

public class HostPageCreator implements PageCreator {
    private LinkedHashMap<String, HostAnnouncement> announcements;
    private LinkedHashMap<String, Podcast> podcasts;

    /**
     *
     * @param announcements
     * @param podcasts
     */
    public HostPageCreator(final LinkedHashMap<String, HostAnnouncement> announcements,
                           final LinkedHashMap<String, Podcast> podcasts) {
        this.announcements = announcements;
        this.podcasts = podcasts;
    }

    /**
     *
     */
    public String createPage() {
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
