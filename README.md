Copyright (c) 2023 Sabin Padurariu

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
━┏━━━┓┏━━┓┏━━━┓━━━━┏┓━━┏┓┏━━━┓┏┓━┏┓━━━━┏━━━┓┏━━━┓┏━━━┓┏━━━┓━━━━┏━┓┏━┓┏━━━┓┏━━━┓━
━┗┓┏┓┃┗┫┣┛┗┓┏┓┃━━━━┃┗┓┏┛┃┃┏━┓┃┃┃━┃┃━━━━┃┏━┓┃┃┏━━┛┃┏━┓┃┗┓┏┓┃━━━━┃┃┗┛┃┃┃┏━━┛┃┏━┓┃━
━━┃┃┃┃━┃┃━━┃┃┃┃━━━━┗┓┗┛┏┛┃┃━┃┃┃┃━┃┃━━━━┃┗━┛┃┃┗━━┓┃┃━┃┃━┃┃┃┃━━━━┃┏┓┏┓┃┃┗━━┓┗┛┏┛┃━
━━┃┃┃┃━┃┃━━┃┃┃┃━━━━━┗┓┏┛━┃┃━┃┃┃┃━┃┃━━━━┃┏┓┏┛┃┏━━┛┃┗━┛┃━┃┃┃┃━━━━┃┃┃┃┃┃┃┏━━┛━━┃┏┛━
━┏┛┗┛┃┏┫┣┓┏┛┗┛┃━━━━━━┃┃━━┃┗━┛┃┃┗━┛┃━━━━┃┃┃┗┓┃┗━━┓┃┏━┓┃┏┛┗┛┃━━━━┃┃┃┃┃┃┃┗━━┓━━┏┓━━
━┗━━━┛┗━━┛┗━━━┛━━━━━━┗┛━━┗━━━┛┗━━━┛━━━━┗┛┗━┛┗━━━┛┗┛━┗┛┗━━━┛━━━━┗┛┗┛┗┛┗━━━┛━━┗┛━━
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

# **GLOBAL WAVES**

A web platform that simulates music streaming.

The application is powered using the Spring Boot framework.

## API

As of now, the platform has only one endpoint, `/command`.

Every JSON object must have a command string field and an integer
field, representing the timestamp.

The client can request to do something through a command or can request some
statistics from the database. If this is the case, the user field must be null.

List of posible commands:

* search
* select
* load"
* playPause
* repeat
* forward
* backward
* next
* prev
* like
* follow
* addRemoveInPlaylist
* status
* createPlaylist
* switchVisibility
* followPlaylist
* showPlaylists
* showPreferredSongs
* getTop5Songs
* getTop5Playlists

In addition, the request object can contain one of the following fields:

* username - string
* type - string
* filters - object
* itemNumber - integer
* seed - integer
* playlistName - string
* playlistId - integer

Example of request object:

```JSON
{
    "command": "search",
    "username": "emily30",
    "timestamp": 4015,
    "type": "playlist",
    "filters": {
        "name": "LoFi music"
    }
}
```

In addition, the response object can contain one of the following 
fields, depending if the user field is null or not:

1. user specified
    * message - string
    * results - list of strings
    * stats - object
    * result - array of objects
2. user not specified
    * results - list of strings

Example of response object:

```JSON
{
  "command" : "search",
  "user" : "emily30",
  "timestamp" : 4015,
  "message" : "Search returned 1 results",
  "results" : [ "LoFi music" ]
}
```

## Implementation Details and Classes Overview

The five main classes:

1. User
2. Musicplayer
3. Searchbar
4. DataBase
5. UserManager

### User

The User class represents more of a context in which to group a searchbar and
a musicplayer. Thus every User will have their own musicplayer and searchbar.
In this way the search results will differ from user to user and every user
will be able to play their own song, playlist or podcast.

The user also implements one design pattern. It implements Adapter because it
represents the layer of abstraction between any command and the coresponding
tool within the user.

The Artist and Host also implement one design patter. They implement Observer
because when any action is executed by them the subscribed normal users get
notified of the underlying action.

At the moment the user only has 2 methods available:

* a method which takes a CommandInput argument and returns a CommandOutput 
object and represents the only way to make the user execute the desired command
* a method that returns the message from the last command executed by the user

### Musicplayer

The Musicplayer class is responsible for playing the preffered audio file.
It is basically the workhorse of the application. 80% of the program logic is
implemented in this class. Its main job is to load songs, playlists and podcasts
and play them.

### Searchbar

The Searchbar class represents more of a inteface (not to be confused with a
java interface) between the user and the DataBase. By having one, the user can
search for songs, personal playlists and public playlists created by other and
podcasts in the database.

### DataBase

The DataBase class follows the Singleton design pattern and it is used to store
all the user on the platform, all the songs and all the created playlists and
podcasts.

The database also implements the Factory design pattern as it creates statistics
objects based on the desired needs. The statistics itself implements the
strategy design pattern because the interface basically has only one method
that is implemented differently based on the class. 

Its main purpose is to server the searchbar the requested data. The database can
be queried for the following types:

* users
* artist
* hosts
* songs
* public playlists and playlists owned by a user
* albums
* podcasts

### UserManager

This class keeps track of everything that happens to the user. It runs its
player, makes sure that the songs and playlists are handled properly. It is
implemented using the observer desing pattern but it is also a visitor wanabe.
It takes care of the links made during the execution. 

### PageCreator

While at a first glance it may seem that this class is a Command pattern it is
actually not. It is a strategy because the interface has only one method that
return the content of a user's page, but strigified. It is not a command because
it doesn't have any callback function.
