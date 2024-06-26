package at.fhv.backend.memory;

import at.fhv.backend.model.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class GameMemory {
    private final HashMap<String, Game> games = new HashMap<>();

    public void save(Game game) {
        games.put(game.getGameCode(), game);
        for (Game g : games.values()) {
            System.out.println("Your Game Code: " + g.getGameCode());
        }
    }

    public Game findByGameCode(String gameCode) {
        return games.get(gameCode);
    }

    public void deleteByGameCode(String gameCode) {
        games.remove(gameCode);
    }

    public boolean existsByGameCode(String gameCode) {
        return games.containsKey(gameCode);
    }

}
