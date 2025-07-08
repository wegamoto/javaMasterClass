package com.wewe.chordmate.controller;

import com.wewe.chordmate.model.Song;
import com.wewe.chordmate.repository.SongRepository;
import com.wewe.chordmate.service.SongService;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;
    private final SongRepository songRepository;

    public SongController(SongService songService, SongRepository songRepository) {
        this.songService = songService;
        this.songRepository = songRepository;
    }

    @GetMapping
    public String listSongs(Model model) {
        model.addAttribute("songs", songService.getAllSongs());
        return "songs/list"; // src/main/resources/templates/songs/list.html
    }

    @GetMapping("/{id}")
    public String viewSong(@PathVariable Long id, Model model) {
        Optional<Song> song = songService.getSongById(id);
        if (song.isPresent()) {
            model.addAttribute("song", song.get());
            return "songs/view"; // templates/songs/view.html
        }
        return "redirect:/songs";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("song", new Song());
        return "songs/create";
    }

    @PostMapping("/delete/{id}")
    public String deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return "redirect:/songs";
    }

    @PostMapping("/songs")
    public String saveSong(@ModelAttribute Song song) {
        songRepository.save(song);
        return "redirect:/songs";
    }
}

