package cz.krapmatt.minesweeper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cz.krapmatt.minesweeper.entity.Round;
import cz.krapmatt.minesweeper.service.RoundService;

@Controller
public class RoundController {
    @Autowired
    private RoundService roundService;

    public Round createRound (Round round) {
        return roundService.saveRound(round);
    }
}
