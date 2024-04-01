package cz.krapmatt.minesweeper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.service.GameService;
@RestController
public class RestAPIGameController {
    
    private final GameService gameService;
    
    @Autowired
    public RestAPIGameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping("/api/game/{game_id}/click")
    @ResponseBody
    private GameState ClickOnSquare(@PathVariable(value="game_id") int id, @RequestBody NewGameData newGameData) {
        int row = newGameData.getRows();
        int column = newGameData.getColumns();
        return gameService.ClickOnSquare(id, row, column);
    }

    @RequestMapping("/api/game/{game_id}/toggleflag")
    @ResponseBody
    private GameState ToggleFlagBomb(@PathVariable(value="game_id") int id, @RequestBody NewGameData newGameData) {
        int row = newGameData.getRows();
        int column = newGameData.getColumns();
        return gameService.FlagBomb(id, row, column);
    }
    
    @RequestMapping("/api/game/{game_id}/getstate")
    @ResponseBody
    private GameState GetGameState(@PathVariable(value="game_id") int id) {
        return gameService.getGamestate(id);
    }
    
    @PostMapping("/api/game/newgame")
    @ResponseBody
    public void createNewGame(@RequestBody NewGameData newGameData) {
        int rows = newGameData.getRows();
        int columns = newGameData.getColumns();
        int numOfMines = newGameData.getNumOfMines();
        gameService.createGame(rows, columns, numOfMines);
        
    }
    
    @RequestMapping("/api/game/listofgames")
    @ResponseBody
    private List<Integer> getListOfGames() {
        return gameService.getPlayebleGames();
    }
    
    @RequestMapping("/api/game/{game_id}")
    @ResponseBody
    public String showGameAsString(@PathVariable(value="game_id") int id) {
        return gameService.generateGameString(id);
    }
}
