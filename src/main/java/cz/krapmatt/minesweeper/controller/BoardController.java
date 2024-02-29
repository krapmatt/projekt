package cz.krapmatt.minesweeper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.service.BoardService;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    
    public Board createBoard(Board board) {
        return boardService.createBoard(board);
    }
    
    public void deleteBoard(Board board) {
        boardService.deleteBoard(board);
    }
}
