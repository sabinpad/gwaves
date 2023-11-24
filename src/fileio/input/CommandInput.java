package fileio.input;

public final class CommandInput {
    // Standard argument fields

    private String command;
    private String username;
    private Integer timestamp;

    // Additional argument fields based of command

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

    public void setCommand(final String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTimestamp() {
        return this.timestamp;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setFilters(final FilterInput filters) {
        this.filters = filters;
    }

    public FilterInput getFilters() {
        return this.filters;
    }

    public void setItemNumber(final Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getItemNumber() {
        return this.itemNumber;
    }

    public void setSeed(final Integer seed) {
        this.seed = seed;
    }

    public Integer getSeed() {
        return this.seed;
    }

    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistName() {
        return this.playlistName;
    }

    public void setPlaylistId(final Integer playlistId) {
        this.playlistId = playlistId;
    }

    public Integer getPlaylistId() {
        return this.playlistId;
    }
}
