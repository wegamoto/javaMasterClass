package dev.lpa;

import java.util.ArrayList;
import java.util.LinkedList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Playlist myPlaylist = new Playlist("My Favorite Songs");

        Song song1 = new Song("Bohemian Rhapsody", "Queen", 5.55);
        Song song2 = new Song("Imagine", "John Lennon", 3.03);
        Song song3 = new Song("Hotel California", "Eagles", 6.30);

        myPlaylist.addSong(song1);
        myPlaylist.addSong(song2);
        myPlaylist.addSong(song3);

        myPlaylist.listSongs();

        myPlaylist.playSong(1);

        myPlaylist.removeSong("Imagine");
        myPlaylist.listSongs();

        Album album = new Album("Greatest Hits", "Queen");
        album.addSong(song1);
//        album.addSong("We Will Rock You", 2.15);
        album.listSongs();
    }
}