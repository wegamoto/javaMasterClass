package com.wewe.chordmate.model;

import jakarta.persistence.*;

@Entity
public class Chord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chordName;
    private double time; // เวลา (เช่น วินาทีที่ 5.0 จะเล่นคอร์ดนี้)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;

    // Constructors
    public Chord() {}

    public Chord(String chordName, double time, Song song) {
        this.chordName = chordName;
        this.time = time;
        this.song = song;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getChordName() {
        return chordName;
    }

    public double getTime() {
        return time;
    }

    public Song getSong() {
        return song;
    }

    public void setChordName(String chordName) {
        this.chordName = chordName;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}

