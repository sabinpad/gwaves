package gwaves.tools;

import java.util.ArrayList;

import fileio.input.CommandInput;
import fileio.output.CommandOutput;

import gwaves.context.User;
import gwaves.context.NormalUser;
import gwaves.context.Artist;
import gwaves.context.Host;
import gwaves.storage.DataBase;

public final class CommandExecuter {
    private CommandExecuter() {

    }

    /**
     *
     * @param commandInput
     * @return
     */
    public static CommandOutput run(final CommandInput commandInput) {
        CommandOutput commandOutput = new CommandOutput();

        if (commandInput.getCommand() == null) {
            CommandExecuter.execEndProgram(commandInput, commandOutput);
            return commandOutput;
        }

        commandOutput.setCommand(commandInput.getCommand());
        commandOutput.setUser(commandInput.getUsername());
        commandOutput.setTimestamp(commandInput.getTimestamp());

        switch (commandInput.getCommand()) {
            case "changePage":
                CommandExecuter.execChangePage(commandInput, commandOutput);
                break;
            case "printCurrentPage":
                CommandExecuter.execPrintPage(commandInput, commandOutput);
                break;
            case "addUser":
                CommandExecuter.execAddUser(commandInput, commandOutput);
                break;
            case "deleteUser":
                CommandExecuter.execDeleteUser(commandInput, commandOutput);
                break;
            case "addAlbum":
                CommandExecuter.execAddAlbum(commandInput, commandOutput);
                break;
            case "removeAlbum":
                CommandExecuter.execRemoveAlbum(commandInput, commandOutput);
                break;
            case "addEvent":
                CommandExecuter.execAddEvent(commandInput, commandOutput);
                break;
            case "removeEvent":
                CommandExecuter.execRemoveEvent(commandInput, commandOutput);
                break;
            case "addMerch":
                CommandExecuter.execAddMerch(commandInput, commandOutput);
                break;
            case "addPodcast":
                CommandExecuter.execAddPodcast(commandInput, commandOutput);
                break;
            case "removePodcast":
                CommandExecuter.execRemovePodcast(commandInput, commandOutput);
                break;
            case "addAnnouncement":
                CommandExecuter.execAddAnnouncement(commandInput, commandOutput);
                break;
            case "removeAnnouncement":
                CommandExecuter.execRemoveAnnouncement(commandInput, commandOutput);
                break;
            case "search":
                CommandExecuter.execSearch(commandInput, commandOutput);
                break;
            case "select":
                CommandExecuter.execSelect(commandInput, commandOutput);
                break;
            case "load":
                CommandExecuter.execLoad(commandInput, commandOutput);
                break;
            case "playPause":
                CommandExecuter.execPlayPause(commandInput, commandOutput);
                break;
            case "repeat":
                CommandExecuter.execRepeat(commandInput, commandOutput);
                break;
            case "shuffle":
                CommandExecuter.execShuffle(commandInput, commandOutput);
                break;
            case "forward":
                CommandExecuter.execForward(commandInput, commandOutput);
                break;
            case "backward":
                CommandExecuter.execBackward(commandInput, commandOutput);
                break;
            case "like":
                CommandExecuter.execLike(commandInput, commandOutput);
                break;
            case "next":
                CommandExecuter.execNext(commandInput, commandOutput);
                break;
            case "prev":
                CommandExecuter.execPrev(commandInput, commandOutput);
                break;
            case "createPlaylist":
                CommandExecuter.execCreatePlaylist(commandInput, commandOutput);
                break;
            case "addRemoveInPlaylist":
                CommandExecuter.execAddRemoveInPlaylist(commandInput, commandOutput);
                break;
            case "switchVisibility":
                CommandExecuter.execSwitchVisibility(commandInput, commandOutput);
                break;
            case "showPlaylists":
                CommandExecuter.execShowPlaylists(commandInput, commandOutput);
                break;
            case "follow":
                CommandExecuter.execFollow(commandInput, commandOutput);
                break;
            case "status":
                CommandExecuter.execStatus(commandInput, commandOutput);
                break;
            case "showPreferredSongs":
                CommandExecuter.execShowPreferredSongs(commandInput, commandOutput);
                break;
            case "showAlbums":
                CommandExecuter.execShowAlbums(commandInput, commandOutput);
                break;
            case "showPodcasts":
                CommandExecuter.execShowPodcasts(commandInput, commandOutput);
                break;
            case "switchConnectionStatus":
                CommandExecuter.execSwitchConnectionStatus(commandInput, commandOutput);
                break;
            case "getTop5Songs":
                CommandExecuter.execGetTop5Songs(commandInput, commandOutput);
                break;
            case "getTop5Playlists":
                CommandExecuter.execGetTop5Playlists(commandInput, commandOutput);
                break;
            case "getTop5Albums":
                CommandExecuter.execGetTop5AlbumsName(commandInput, commandOutput);
                break;
            case "getTop5Artists":
                CommandExecuter.execGetTop5ArtistsName(commandInput, commandOutput);
                break;
            case "getAllUsers":
                CommandExecuter.execGetAllUsersName(commandInput, commandOutput);
                break;
            case "getOnlineUsers":
                CommandExecuter.execGetOnlineUsersName(commandInput, commandOutput);
                break;
            case "wrapped":
                CommandExecuter.execWrapped(commandInput, commandOutput);
                break;
            case "buyMerch":
                CommandExecuter.execBuyMerch(commandInput, commandOutput);
                break;
            case "seeMerch":
                CommandExecuter.execSeeMerch(commandInput, commandOutput);
                break;
            case "buyPremium":
                CommandExecuter.execBuyPremium(commandInput, commandOutput);
                break;
            case "cancelPremium":
                CommandExecuter.execCancelPremium(commandInput, commandOutput);
                break;
            case "adBreak":
                CommandExecuter.execAdBreak(commandInput, commandOutput);
                break;
            case "subscribe":
                CommandExecuter.execSubscribe(commandInput, commandOutput);
                break;
            case "getNotifications":
                CommandExecuter.execGetNotifications(commandInput, commandOutput);
                break;
            case "updateRecommendations":
                CommandExecuter.execUpdateRecommendations(commandInput, commandOutput);
                break;
            case "loadRecommendations":
                CommandExecuter.execLoadRecommendations(commandInput, commandOutput);
                break;
            case "previousPage":
                CommandExecuter.execPreviousPage(commandInput, commandOutput);
                break;
            case "nextPage":
                CommandExecuter.execNextPage(commandInput, commandOutput);
                break;
            // case "endProgram":
            //     CommandExecuter.execEndProgram(commandInput, commandOutput);
            //     break;
            default:
                break;
        }

        return commandOutput;
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execSearch(final CommandInput commandInput,
                                   final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            commandOutput.setResults(new ArrayList<>());
            return;
        }

        commandOutput.setResults(normaluser.doSearch(commandInput.getType(),
                                 commandInput.getFilters()));
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execSelect(final CommandInput commandInput,
                                   final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doSelect(commandInput.getItemNumber());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execLoad(final CommandInput commandInput,
                                 final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doLoad();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execPlayPause(final CommandInput commandInput,
                                      final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doPlayPause();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execRepeat(final CommandInput commandInput,
                                   final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doRepeat();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execShuffle(final CommandInput commandInput,
                                    final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        if (commandInput.getSeed() == null) {
            normaluser.doShuffle(0);
        } else {
            normaluser.doShuffle(commandInput.getSeed());
        }

        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execForward(final CommandInput commandInput,
                                    final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doForward();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execBackward(final CommandInput commandInput,
                                     final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doBackward();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execLike(final CommandInput commandInput,
                                 final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doLike();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execNext(final CommandInput commandInput,
                                 final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doNext();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execPrev(final CommandInput commandInput,
                                 final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doPrev();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execAddRemoveInPlaylist(final CommandInput commandInput,
                                                final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doAddRemoveInPlaylist(commandInput.getPlaylistId());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execStatus(final CommandInput commandInput,
                                   final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        commandOutput.setStats(normaluser.doMPlayerStatus());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execCreatePlaylist(final CommandInput commandInput,
                                           final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doCreatePlaylist(commandInput.getPlaylistName());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execSwitchVisibility(final CommandInput commandInput,
                                             final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doSwitchVisibility(commandInput.getPlaylistId());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execFollow(final CommandInput commandInput,
                                   final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doFollowPlaylist();
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execShowPlaylists(final CommandInput commandInput,
                                          final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        commandOutput.setResult(normaluser.doShowPlaylists());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execShowPreferredSongs(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        commandOutput.setResult(normaluser.doShowPreferredSongs());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execGetTop5Songs(final CommandInput commandInput,
                                         final CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5SongsName());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execGetTop5Playlists(final CommandInput commandInput,
                                             final CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5PlaylistsName());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execChangePage(final CommandInput commandInput,
                                       final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normaluser.doChangePage(commandInput.getNextPage());
        commandOutput.setMessage(normaluser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execPrintPage(final CommandInput commandInput,
                                      final CommandOutput commandOutput) {
        NormalUser normaluser = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (!normaluser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        commandOutput.setMessage(normaluser.doGetPage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execSwitchConnectionStatus(final CommandInput commandInput,
                                                   final CommandOutput commandOutput) {
        NormalUser user = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        user = DataBase.getInstance().queryNormalUser(commandInput.getUsername());

        if (user == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a normal user.");
            return;
        }

        user.doSwitchStatus();

        commandOutput.setMessage(user.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execAddUser(final CommandInput commandInput,
                                    final CommandOutput commandOutput) {
        NormalUser normaluser;
        DataBase database = DataBase.getInstance();

        if (database.queryUser(commandInput.getUsername()) != null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " is already taken.");
            return;
        }

        switch (commandInput.getType()) {
            case "user":
                normaluser = new NormalUser(commandInput.getUsername(),
                                            commandInput.getAge(),
                                            commandInput.getCity());
                database.addNormalUser(normaluser);
                UserManager.getInstance().addUser(normaluser);
                break;
            case "artist":
                database.addArtist(
                        new Artist(commandInput.getUsername(),
                                   commandInput.getAge(),
                                   commandInput.getCity()));
                break;
            case "host":
                database.addHost(new Host(commandInput.getUsername(),
                                          commandInput.getAge(),
                                          commandInput.getCity()));
                break;
            default:
                return;
        }

        commandOutput.setMessage("The username " + commandInput.getUsername()
                                 + " has been added successfully.");
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execDeleteUser(final CommandInput commandInput,
                                       final CommandOutput commandOutput) {
        User user = null;
        DataBase database = DataBase.getInstance();
        UserManager userManager = UserManager.getInstance();

        user = database.queryUser(commandInput.getUsername());

        if (user == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        if (database.queryNormalUser(commandInput.getUsername()) != null) {
            NormalUser normaluser = database.queryNormalUser(commandInput.getUsername());

            if (!userManager.isSafeToRemove(normaluser)) {
                commandOutput.setMessage(commandInput.getUsername() + " can't be deleted.");
                return;
            }

            userManager.rmvUser(normaluser);
            userManager.toUnlink(normaluser);

            normaluser.clearAll();
            database.removeNormalUser(normaluser);
        } else if (database.queryArtist(commandInput.getUsername()) != null) {
            Artist artist = database.queryArtist(commandInput.getUsername());

            if (!userManager.isSafeToRemove(artist)) {
                commandOutput.setMessage(commandInput.getUsername() + " can't be deleted.");
                return;
            }

            userManager.toUnlink(artist);

            artist.clearAll();
            database.removeArtist(artist);
        } else if (database.queryHost(commandInput.getUsername()) != null) {
            Host host = database.queryHost(commandInput.getUsername());

            if (!userManager.isSafeToRemove(host)) {
                commandOutput.setMessage(commandInput.getUsername() + " can't be deleted.");
                return;
            }

            host.clearAll();
            database.removeHost(host);
        }

        commandOutput.setMessage(commandInput.getUsername() + " was successfully deleted.");
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execAddAlbum(final CommandInput commandInput,
                                     final CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doAddAlbum(commandInput.getName(),
                commandInput.getUsername(),
                commandInput.getReleaseYear(),
                commandInput.getDescription(),
                commandInput.getSongs());

        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execRemoveAlbum(final CommandInput commandInput,
                                        final CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doRmvAlbum(commandInput.getName());

        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execAddEvent(final CommandInput commandInput,
                                     final CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doAddEvent(commandInput.getName(),
                commandInput.getDescription(),
                commandInput.getDate());

        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execRemoveEvent(final CommandInput commandInput,
                                        final CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doRmvEvent(commandInput.getName());

        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execAddMerch(final CommandInput commandInput,
                                     final CommandOutput commandOutput) {
        Artist artist = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        artist = DataBase.getInstance().queryArtist(commandInput.getUsername());

        if (artist == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not an artist.");
            return;
        }

        artist.doAddMerch(commandInput.getName(),
                commandInput.getDescription(),
                commandInput.getPrice());

        commandOutput.setMessage(artist.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execShowAlbums(final CommandInput commandInput,
                                       final CommandOutput commandOutput) {
        Artist artist = DataBase.getInstance().queryArtist(commandInput.getUsername());
        commandOutput.setResult(artist.doShowAlbums());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execAddPodcast(final CommandInput commandInput,
                                       final CommandOutput commandOutput) {
        Host host = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        host = DataBase.getInstance().queryHost(commandInput.getUsername());

        if (host == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a host.");
            return;
        }

        host.doAddPodcast(commandInput.getName(),
                commandInput.getUsername(),
                commandInput.getEpisodes());

        commandOutput.setMessage(host.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execRemovePodcast(final CommandInput commandInput,
                                          final CommandOutput commandOutput) {
        Host host = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        host = DataBase.getInstance().queryHost(commandInput.getUsername());

        if (host == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a host.");
            return;
        }

        host.doRmvPodcast(commandInput.getName());

        commandOutput.setMessage(host.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execAddAnnouncement(final CommandInput commandInput,
                                            final CommandOutput commandOutput) {
        Host host = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        host = DataBase.getInstance().queryHost(commandInput.getUsername());

        if (host == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a host.");
            return;
        }

        host.doAddAnnouncement(commandInput.getName(), commandInput.getDescription());

        commandOutput.setMessage(host.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execRemoveAnnouncement(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        Host host = null;

        if (DataBase.getInstance().queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                     + " doesn't exist.");
            return;
        }

        host = DataBase.getInstance().queryHost(commandInput.getUsername());

        if (host == null) {
            commandOutput.setMessage(commandInput.getUsername() + " is not a host.");
            return;
        }

        host.doRmvAnnouncement(commandInput.getName());

        commandOutput.setMessage(host.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execShowPodcasts(final CommandInput commandInput,
                                         final CommandOutput commandOutput) {
        Host host = DataBase.getInstance().queryHost(commandInput.getUsername());
        commandOutput.setResult(host.doShowPodcasts());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execGetTop5AlbumsName(final CommandInput commandInput,
                                              final CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5AlbumsName());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execGetTop5ArtistsName(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getTop5ArtistsName());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execGetAllUsersName(final CommandInput commandInput,
                                            final CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getAllUsersName());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execGetOnlineUsersName(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        commandOutput.setResult(DataBase.getInstance().getOnlineUsersName());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execWrapped(final CommandInput commandInput, 
                                    final CommandOutput commandOutput) {
        DataBase database = DataBase.getInstance();

        if (database.queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("No data to show for user " + commandInput.getUsername()
                                        + ".");
            return;
        }

        if (database.queryNormalUser(commandInput.getUsername()) != null) {
            NormalUser normaluser = database.queryNormalUser(commandInput.getUsername());
            commandOutput.setResult(normaluser.doWrapped());

            if (commandOutput.getResult() == null) {
                commandOutput.setMessage(normaluser.getLastCommandMessage());
            }
        } else if (database.queryArtist(commandInput.getUsername()) != null) {
            Artist artist = database.queryArtist(commandInput.getUsername());
            commandOutput.setResult(artist.doWrapped());

            if (commandOutput.getResult() == null) {
                commandOutput.setMessage(artist.getLastCommandMessage());
            }
        } else if (database.queryHost(commandInput.getUsername()) != null) {
            Host host = database.queryHost(commandInput.getUsername());
            commandOutput.setResult(host.doWrapped());

            if (commandOutput.getResult() == null) {
                commandOutput.setMessage(host.getLastCommandMessage());
            }
        }
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execBuyMerch(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
                                                
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        normalUser.doBuyMerch(commandInput.getName());

        commandOutput.setMessage(normalUser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execSeeMerch(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        commandOutput.setResult(normalUser.doSeeMerch());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execBuyPremium(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        normalUser.doBuyPremium();

        commandOutput.setMessage(normalUser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execCancelPremium(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        normalUser.doCancelPremium();

        commandOutput.setMessage(normalUser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execAdBreak(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        normalUser.doAdBreak(commandInput.getPrice());

        commandOutput.setMessage(normalUser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execSubscribe(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        normalUser.doSubscribe();

        commandOutput.setMessage(normalUser.getLastCommandMessage());                                   
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execGetNotifications(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        commandOutput.setNotifications(normalUser.doGetNotifications());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execUpdateRecommendations(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        if (database.queryUser(commandInput.getUsername()) == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage(commandInput.getUsername()
                                        + " is not a normal user.");
            return;
        }

        normalUser.doUpdateRecommendations(commandInput.getRecommendationType());

        commandOutput.setMessage(normalUser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execLoadRecommendations(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (!normalUser.isActive()) {
            commandOutput.setMessage(commandInput.getUsername() + " is offline.");
            return;
        }

        normalUser.doLoadRecommendations();

        commandOutput.setMessage(normalUser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execPreviousPage(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        normalUser.doPreviousPage();

        commandOutput.setMessage(normalUser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execNextPage(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        NormalUser normalUser = null;
        DataBase database = DataBase.getInstance();

        normalUser = database.queryNormalUser(commandInput.getUsername());

        if (normalUser == null) {
            commandOutput.setMessage("The username " + commandInput.getUsername()
                                        + " doesn't exist.");
            return;
        }

        normalUser.doNextPage();

        commandOutput.setMessage(normalUser.getLastCommandMessage());
    }

    /**
     *
     * @param commandInput
     * @param commandOutput
     */
    private static void execEndProgram(final CommandInput commandInput,
                                               final CommandOutput commandOutput) {
        commandOutput.setCommand("endProgram");
        UserManager.getInstance().payRemainingAll();
        commandOutput.setResult(DataBase.getInstance().getArtistRanking());
    }
}
