package com.wewe.chordmate.controller;

import com.wewe.chordmate.model.Chord;
import com.wewe.chordmate.model.Song;
import com.wewe.chordmate.service.ChordService;
import com.wewe.chordmate.service.SongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/chords")
public class ChordController {

    private final ChordService chordService;
    private final SongService songService;

    public ChordController(ChordService chordService, SongService songService) {
        this.chordService = chordService;
        this.songService = songService;
    }

    @GetMapping("/song/{songId}")
    public String getChordsBySong(@PathVariable Long songId, Model model) {
        Optional<Song> song = songService.getSongById(songId);
        if (song.isPresent()) {
            model.addAttribute("song", song.get());
            model.addAttribute("chords", chordService.getChordsBySong(song.get()));
            return "chords/list"; // templates/chords/list.html
        }
        return "redirect:/songs";
    }

    @GetMapping("/create/{songId}")
    public String showCreateChordForm(@PathVariable Long songId, Model model) {
        Optional<Song> song = songService.getSongById(songId);
        if (song.isPresent()) {
            Chord chord = new Chord();
            chord.setSong(song.get());
            model.addAttribute("chord", chord);
            return "chords/create";
        }
        return "redirect:/songs";
    }

    @PostMapping("/create")
    public String createChord(@ModelAttribute Chord chord) {
        chordService.createChord(chord);
        return "redirect:/chords/song/" + chord.getSong().getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteChord(@PathVariable Long id) {
        Optional<Chord> chord = chordService.getChordById(id);
        if (chord.isPresent()) {
            Long songId = chord.get().getSong().getId();
            chordService.deleteChord(id);
            return "redirect:/chords/song/" + songId;
        }
        return "redirect:/songs";
    }
}

