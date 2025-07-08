package com.wewe.chordmate.repository;

import com.wewe.chordmate.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // เพิ่มเมธอด custom ได้ เช่น
    Song findByTitle(String title);
}

