package cz.krapmatt.minesweeper.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.Round;
import cz.krapmatt.minesweeper.service.GameService;



@Controller
public class GameController {
    @Autowired
    private GameService gameService;
    
    /*public List<Game> getAllGames() {
        return gameService.getAllGames();
    }*/
    
    
    public void deleteGame(Game game) {
        gameService.deleteGame(game);
    }

    public Game createGame(Game game) {
        return gameService.createGame(game);
        
    }
    
}