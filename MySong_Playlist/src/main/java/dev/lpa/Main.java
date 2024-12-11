package dev.lpa;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Playlist wewePlaylist = new Playlist("My Favorite Songs");

        Song song1 = new Song("Bohemian Rhapsody", "Queen");
        Song song2 = new Song("Imagine", "John Lennon");
        Song song3 = new Song("Hotel California", "Eagles");

        wewePlaylist.addSong(song1);
        wewePlaylist.addSong(song2);
        wewePlaylist.addSong(song3);

        wewePlaylist.listSongs();

        wewePlaylist.playSong(1);

        wewePlaylist.removeSong("Imagine");
        wewePlaylist.listSongs();
    }
}