package cz.krapmatt.minesweeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.repository.BoardRepository;
import jakarta.transaction.Transactional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    
    @Transactional
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }
    
    @Transactional
    public void deleteBoard(Board board) {
        Integer id = board.getId();
        boardRepository.deleteById(id);
    }
}
