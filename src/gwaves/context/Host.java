package gwaves.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fileio.input.FilterInput;
import fileio.input.EpisodeInput;
import fileio.output.PodcastOutput;

import gwaves.collection.Podcast;
import gwaves.storage.DataBase;
import gwaves.tools.UserManager;
import gwaves.misc.HostAnnouncement;
import gwaves.util.Filterable;
import gwaves.ui.PageCreator;
import gwaves.ui.HostPageCreator;

public final class Host extends User implements Filterable {
    private LinkedHashMap<String, HostAnnouncement> announcements;

    private LinkedHashMap<String, Podcast> podcasts;

    private HostPageCreator pageCreator;

    private LinkedHashMap<NormalUser, Integer> listeners;

    private ArrayList<NormalUser> subscribers;

    /**
     * Create new Host object
     *
     * @param username
     * @param age
     * @param city
     */
    public Host(final String username, final int age, final String city) {
        super(username, age, city);

        this.announcements = new LinkedHashMap<>();
        this.podcasts = new LinkedHashMap<>();
        this.pageCreator = new HostPageCreator(announcements, podcasts);
        this.subscribers = new ArrayList<>();
    }

    /**
     *
     * @param name
     * @param owner
     * @param episodesInput
     */
    public void doAddPodcast(final String name, final String owner,
                             final ArrayList<EpisodeInput> episodesInput) {
        if (this.podcasts.containsKey(name)) {
            this.commandMessage = this.getUsername() + " has another podcast with the same name.";
            return;
        }

        for (var ep1 : episodesInput) {
            for (var ep2 : episodesInput) {
                if (ep1.getName().equals(ep2.getName()) && (ep1 != ep2)) {
                    this.commandMessage = this.getUsername()
                                          + " has the same episode in this podcast.";
                    return;
                }
            }
        }

        Podcast newPodcast = new Podcast(name, owner, episodesInput);
        DataBase database = DataBase.getInstance();

        this.podcasts.put(name, newPodcast);
        database.addPodcast(newPodcast);

        this.notifySubs("New Podcast", "New Podcast from " + this.getUsername());

        this.commandMessage = this.getUsername() + " has added new podcast successfully.";
    }

    /**
     *
     * @param name
     */
    public void doRmvPodcast(final String name) {
        if (!this.podcasts.containsKey(name)) {
            this.commandMessage = this.getUsername()
                                  + " doesn't have a podcast with the given name.";
            return;
        }

        if (!UserManager.getInstance().isSafeToRemove(this.podcasts.get(name))) {
            this.commandMessage = this.getUsername() + " can't delete this podcast.";
            return;
        }

        DataBase.getInstance().removePodcast(this.podcasts.get(name));
        this.podcasts.remove(name);

        this.commandMessage = this.getUsername() + " deleted the podcast successfully.";
    }

    /**
     *
     * @param name
     * @param description
     */
    public void doAddAnnouncement(final String name, final String description) {
        if (this.announcements.containsKey(name)) {
            this.commandMessage = this.getUsername()
                                  + " has already added an announcement with this name.";
            return;
        }

        this.announcements.put(name, new HostAnnouncement(name, description));

        this.notifySubs("New Announcement", "New Announcement from " + this.getUsername());

        this.commandMessage = this.getUsername()
                            + " has successfully added new announcement.";
    }

    /**
     *
     * @param name
     */
    public void doRmvAnnouncement(final String name) {
        if (!this.announcements.containsKey(name)) {
            this.commandMessage = this.getUsername()
                                  + " has no announcement with the given name.";
            return;
        }

        this.announcements.remove(name);

        this.commandMessage = this.getUsername()
                              + " has successfully deleted the announcement.";
    }

    /**
     *
     * @return
     */
    public ArrayList<PodcastOutput> doShowPodcasts() {
        PodcastOutput podOutput;
        ArrayList<PodcastOutput> result = new ArrayList<>();

        for (var entry : this.podcasts.entrySet()) {
            podOutput = new PodcastOutput();

            podOutput.setName(entry.getValue().getName());
            podOutput.setEpisodes(entry.getValue().getAudRecsName());

            result.add(podOutput);
        }

        this.commandMessage = null;

        return result;
    }

    /**
     *
     * @return
     */
    public String doGetPage() {
        return this.pageCreator.createPage();
    }

    public void addListen(NormalUser normalUser) {
        if (this.listeners.containsKey(normalUser)) {
            Integer val = this.listeners.get(normalUser);
            val++;
        } else {
            this.listeners.put(normalUser, 1);
        }
    }

    private void notifySubs(String name, String description) {
        for (var sub : this.subscribers) {
            sub.addNotification(name, description);
        }
    }

    /**
     *
     */
    public void clearAll() {
        DataBase database = DataBase.getInstance();

        for (var entry : this.podcasts.entrySet()) {
            database.removePodcast(entry.getValue());
        }

        this.podcasts.clear();
    }

    /**
     *
     * @return
     */
    public ArrayList<Podcast> getPodcasts() {
        return new ArrayList<>(this.podcasts.values());
    }

    /**
     *
     * @return
     */
    public PageCreator getPageCreator() {
        return this.pageCreator;
    }

    /**
     *
     */
    public boolean isMatchedByFilter(final FilterInput filter) {
        if (filter.getName() != null) {
            if (!this.getUsername().startsWith(filter.getName())) {
                return false;
            }
        }

        return true;
    }
}
