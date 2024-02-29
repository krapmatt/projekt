package cz.krapmatt.minesweeper.entity;

import jakarta.persistence.*;
import java.util.List;
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="game_id")
    private Integer id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Round> rounds;

    // Add other fields as needed

    // Getters and setters
    

    /**
     * @return Long return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param i the id to set
     */
    public void setId(Integer i) {
        this.id = i;
    }

    /**
     * @return List<Round> return the rounds
     */
    public List<Round> getRounds() {
        return rounds;
    }

    /**
     * @param rounds the rounds to set
     */
    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

}
