package fileio.input;

import fileio.CommandIO;

public final class CommandInput extends CommandIO {
    // Standard argument fields
    private String username;

    // Additional argument fields based on command

    // search
    private String type;
    private FilterInput filters;

    // select
    private Integer itemNumber;

    // shuffle
    private Integer seed;

    // createPlaylist
    private String playlistName;

    // addRemoveInPlaylist, switchVisibility
    private Integer playlistId;

    public CommandInput() {
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
}
