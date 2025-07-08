package com.wewe.chordmate.repository;

import com.wewe.chordmate.model.Chord;
import com.wewe.chordmate.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChordRepository extends JpaRepository<Chord, Long> {
    // หา chord ทั้งหมดที่เกี่ยวกับเพลงนี้
    List<Chord> findBySong(Song song);
}

