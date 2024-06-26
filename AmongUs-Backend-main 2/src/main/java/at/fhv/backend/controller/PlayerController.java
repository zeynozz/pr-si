package at.fhv.backend.controller;

import at.fhv.backend.services.GameService;
import at.fhv.backend.services.PlayerService;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController {

    private final PlayerService playerService;
    private final GameService gameService;

    public PlayerController(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }
}