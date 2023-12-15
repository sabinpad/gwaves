package gwaves.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fileio.input.EpisodeInput;
import fileio.output.PodcastOutput;

import gwaves.collection.Podcast;

public class Host extends User {
    private LinkedHashMap<String, HostAnnouncement> announcements;

    private LinkedHashMap<String, Podcast> podcasts;

    /**
     * Create new Host object
     *
     * @param username
     * @param age
     * @param city
     */
    public Host(String username, int age, String city) {
        super(username, age, city);

        this.announcements = new LinkedHashMap<>();
        this.podcasts = new LinkedHashMap<>();
    }

    public void doAddPodcast(final String name, final String owner, final ArrayList<EpisodeInput> episodesInput) {
        if (this.podcasts.containsKey(name)) {
            this.commandMessage = this.getUserName() + " has another podcast with the same name.";
            return;
        }

        // TODO sa verific daca podcastul are 2 episoade la fel
        // this.commandMessage = this.getUserName() + " has the same episode in this podcast.";

        this.podcasts.put(name, new Podcast(name, owner, episodesInput));

        this.commandMessage = this.getUserName() + " has added new podcast successfully.";
    }

    public void doRmvPodcast(String name) {
        if (!this.podcasts.containsKey(name)) {
            this.commandMessage = this.getUserName() + " doesn't have a podcast with the given name.";
            return;
        }

        // TODO sa verific daca cineva asculta podcastul
        // this.commandMessage = this.getUserName() + " can't delete this podcast.";

        this.podcasts.remove(name);

        this.commandMessage = this.getUserName() + " deleted the podcast successfully.";
    }

    public void doAddAnnouncement(String name, String description) {
        if (this.announcements.containsKey(name)) {
            this.commandMessage = this.getUserName() + " has already added an announcement with this name.";
            return;
        }

        this.announcements.put(name, new HostAnnouncement(name, description));

        this.commandMessage = this.getUserName() + " has successfully added new announcement.";
    }

    public void doRmvAnnouncement(String name) {
        if (!this.announcements.containsKey(name)) {
            this.commandMessage = this.getUserName() + " has no announcement with the given name.";
            return;
        }

        this.announcements.remove(name);

        this.commandMessage = this.getUserName() + " has successfully deleted the announcement.";
    }

    public ArrayList<PodcastOutput> doShowPodcasts() {
        PodcastOutput podOutput;
        ArrayList<PodcastOutput> result = new ArrayList<>();

        for (var entry : this.podcasts.entrySet()) {
            podOutput = new PodcastOutput();

            podOutput.setName(entry.getValue().getName());
            podOutput.setEpisodes(entry.getValue().getEpisodesNameList());

            result.add(podOutput);
        }

        this.commandMessage = null;

        return result;
    }
}

class HostAnnouncement {
    private String name;
    private String description;

    public HostAnnouncement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
