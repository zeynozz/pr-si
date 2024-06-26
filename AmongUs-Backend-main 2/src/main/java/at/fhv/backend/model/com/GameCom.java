package at.fhv.backend.model.com;

import at.fhv.backend.model.Player;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameCom {
    private Player player;
    private String numberOfPlayers;
    private String numberOfImpostors;
    private String map;

    public GameCom(Player player, String numberOfPlayers, String numberOfImpostors, String map) {
        this.player = player;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfImpostors = numberOfImpostors;
        this.map = map;
    }

    public GameCom() {}
}
