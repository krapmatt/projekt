package cz.krapmatt.minesweeper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.krapmatt.minesweeper.entity.Round;
import cz.krapmatt.minesweeper.repository.RoundRepository;
import jakarta.transaction.Transactional;

@Service
public class RoundService {
    @Autowired
    private RoundRepository roundRepository;
    @Transactional
    public Round saveRound(Round round) {
        // uložit kolo do database
        return roundRepository.save(round);
        
    }

    public List<Round> readRounds() {
        return roundRepository.findAll();
    }

   
}
