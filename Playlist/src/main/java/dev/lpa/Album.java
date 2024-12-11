package dev.lpa;

import java.util.ArrayList;
import java.util.List;

public class Album {

    private String name;
    private String artist;
    private List<Song> songs;

    public Album(String name, String artist) {
        this.name = name;
        this.artist = artist;
        this.songs = new ArrayList<>();
    }

    // Add a song to the album
    public void addSong(Song song) {
        if (song.getArtist().equalsIgnoreCase(artist)) {
            songs.add(song);
            System.out.println(song.getTitle() + " added to the album " + name + ".");
        } else {
            System.out.println("The artist of the song does not match the album's artist.");
        }
    }

    // List all songs in the album
    public void listSongs() {
        if (songs.isEmpty()) {
            System.out.println("The album " + name + " is empty.");
        } else {
            System.out.println("Songs in the album " + name + ":");
            for (int i = 0; i < songs.size(); i++) {
                System.out.println((i + 1) + ". " + songs.get(i));
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }
}
