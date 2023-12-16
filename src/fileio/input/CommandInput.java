package fileio.input;

import java.util.ArrayList;

import fileio.CommandIO;

public final class CommandInput extends CommandIO {
    // Argument fields based on command

    private String username;
    private Integer timestamp;
    private String type;
    private FilterInput filters;
    private Integer itemNumber;
    private Integer seed;
    private String playlistName;
    private Integer playlistId;
    private String nextPage;
    private Integer age;
    private String city;
    private String name;
    private Integer releaseYear;
    private String description;
    private ArrayList<SongInput> songs;
    private ArrayList<EpisodeInput> episodes;
    private String date;
    private Integer price;


    public CommandInput() {
    }

    /**
     * @param timestamp
     */
    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return
     */
    public Integer getTimestamp() {
        return this.timestamp;
    }

    /**
     * @param username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param filters
     */
    public void setFilters(final FilterInput filters) {
        this.filters = filters;
    }

    /**
     * @return
     */
    public FilterInput getFilters() {
        return this.filters;
    }

    /**
     * @param itemNumber
     */
    public void setItemNumber(final Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    /**
     * @return
     */
    public Integer getItemNumber() {
        return this.itemNumber;
    }

    /**
     * @param seed
     */
    public void setSeed(final Integer seed) {
        this.seed = seed;
    }

    /**
     * @return
     */
    public Integer getSeed() {
        return this.seed;
    }

    /**
     * @param playlistName
     */
    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    /**
     * @return
     */
    public String getPlaylistName() {
        return this.playlistName;
    }

    /**
     * @param playlistId
     */
    public void setPlaylistId(final Integer playlistId) {
        this.playlistId = playlistId;
    }

    /**
     * @return
     */
    public Integer getPlaylistId() {
        return this.playlistId;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setNextPage(final String nextPage) {
        this.nextPage = nextPage;
    }

    public String getNextPage() {
        return this.nextPage;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setReleaseYear(final Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getReleaseYear() {
        return this.releaseYear;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setSongs(final ArrayList<SongInput> songs) {
        this.songs = songs;
    }

    public ArrayList<SongInput> getSongs() {
        return this.songs;
    }

    public void setEpisodes(final ArrayList<EpisodeInput> episodes) {
        this.episodes = episodes;
    }

    public ArrayList<EpisodeInput> getEpisodes() {
        return this.episodes;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return this.price;
    }
}
