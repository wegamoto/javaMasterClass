package com.wewe.chordmate.controller;

import com.wewe.chordmate.model.Song;
import com.wewe.chordmate.service.SongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final SongService songService;

    public HomeController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Song> songs = songService.getAllSongs();  // ดึงเพลงทั้งหมดจาก DB
        model.addAttribute("songs", songs);
        return "index";  // ชื่อไฟล์ Thymeleaf: index.html
    }
}
