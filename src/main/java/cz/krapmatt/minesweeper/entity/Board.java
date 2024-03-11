package cz.krapmatt.minesweeper.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    //nějaký způsob uložení board
    @OneToMany(mappedBy = "board", cascade = CascadeType.MERGE)
    private List<Square> squares;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state")
    private GameState gameState = GameState.ONGOING;

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    /**
     * @return Integer return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return List<Squares> return the squares
     */
    public List<Square> getSquares() {
        return squares;
    }

    /**
     * @param squares the squares to set
     */
    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    /**
     * @return GameState return the gameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @param gameState the gameState to set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public String toString() {
        return "Board [id=" + id + ", game=" + game + ", squares=" + squares + ", gameState=" + gameState + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((game == null) ? 0 : game.hashCode());
        result = prime * result + ((squares == null) ? 0 : squares.hashCode());
        result = prime * result + ((gameState == null) ? 0 : gameState.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (game == null) {
            if (other.game != null)
                return false;
        } else if (!game.equals(other.game))
            return false;
        if (squares == null) {
            if (other.squares != null)
                return false;
        } else if (!squares.equals(other.squares))
            return false;
        if (gameState != other.gameState)
            return false;
        return true;
    }

    @Override
    public Board clone() {
        Board board = new Board();
        board.game = this.game;
        board.gameState = this.gameState;
        board.squares = new ArrayList<>();
        //Tohle to samé jako for jen stream
        //board.squares = this.squares.stream().map(x -> x.clone()).collect(Collectors.toList());
        for (int i = 0; i < this.squares.size(); i++) {
            Square clonesquare = this.squares.get(i).clone();
            clonesquare.setBoard(board);
            board.squares.add(clonesquare);
        }

        return board;
    }
}
