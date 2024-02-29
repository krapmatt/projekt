package cz.krapmatt.minesweeper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.Round;
import cz.krapmatt.minesweeper.repository.GameRepository;
import jakarta.transaction.Transactional;


@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    
    @Transactional
    public Game createGame(Game game) {
        //game.setId(null == gameRepository.findMaxId()? 0 : gameRepository.findMaxId() + 1);
        return gameRepository.save(game);
    }
    
    @Transactional
    public void deleteGame(Game game) {
        int id = game.getId();
        gameRepository.deleteById(id);    
    }


    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }
    
    // Add other methods for CRUD operations
}

