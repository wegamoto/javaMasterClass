package dev.lpa;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String name;
    private List<Song> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    // Add a song to the playlist
    public void addSong(Song song) {
        songs.add(song);
        System.out.println(song.getTitle() + " added to the playlist " + name + ".");
    }

    // Remove a song from the playlist
    public void removeSong(String title) {
        boolean removed = songs.removeIf(song -> song.getTitle().equalsIgnoreCase(title));
        if (removed) {
            System.out.println(title + " removed from the playlist " + name + ".");
        } else {
            System.out.println(title + " not found in the playlist " + name + ".");
        }
    }

    // List all songs in the playlist
    public void listSongs() {
        if (songs.isEmpty()) {
            System.out.println("The playlist " + name + " is empty.");
        } else {
            System.out.println("Songs in the playlist " + name + ":");
            for (int i = 0; i < songs.size(); i++) {
                System.out.println((i + 1) + ". " + songs.get(i));
            }
        }
    }

    // Play a song by its index
    public void playSong(int index) {
        if (index >= 0 && index < songs.size()) {
            System.out.println("Playing: " + songs.get(index));
        } else {
            System.out.println("Invalid song index.");
        }
    }

    // Get the name of the playlist
    public String getName() {
        return name;
    }
}
