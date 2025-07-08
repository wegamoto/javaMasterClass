package com.wewe.chordmate.service;

import com.wewe.chordmate.model.Chord;
import com.wewe.chordmate.model.Song;
import com.wewe.chordmate.repository.ChordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChordService {

    private final ChordRepository chordRepository;

    public ChordService(ChordRepository chordRepository) {
        this.chordRepository = chordRepository;
    }

    public List<Chord> getAllChords() {
        return chordRepository.findAll();
    }

    public Optional<Chord> getChordById(Long id) {
        return chordRepository.findById(id);
    }

    public List<Chord> getChordsBySong(Song song) {
        return chordRepository.findBySong(song);
    }

    public Chord createChord(Chord chord) {
        return chordRepository.save(chord);
    }

    public void deleteChord(Long id) {
        chordRepository.deleteById(id);
    }

    public Chord updateChord(Long id, Chord updatedChord) {
        return chordRepository.findById(id)
                .map(chord -> {
                    chord.setChordName(updatedChord.getChordName());
                    chord.setTime(updatedChord.getTime());
                    chord.setSong(updatedChord.getSong());
                    return chordRepository.save(chord);
                })
                .orElseThrow(() -> new RuntimeException("Chord not found with ID: " + id));
    }
}

