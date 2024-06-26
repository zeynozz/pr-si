package at.fhv.backend.services;

import at.fhv.backend.model.Game;
import at.fhv.backend.generators.CodeGenerator;
import at.fhv.backend.model.Player;
import at.fhv.backend.memory.GameMemory;
import at.fhv.backend.model.VotingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameMemory gameRepository;
    private final PlayerService playerService;
    private final MapService mapService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameService(GameMemory gameRepository, PlayerService playerService, MapService mapService, SimpMessagingTemplate messagingTemplate) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.mapService = mapService;
        this.messagingTemplate = messagingTemplate;
    }

    public Game host(Player player, int numberOfPlayers, int numberOfImpostors, String map) throws FileNotFoundException {
        Game game = new Game(gameCodeGenerator(), numberOfPlayers, numberOfImpostors, map, mapService);

        System.out.println("Game Code: " + game.getGameCode());
        Player p = playerService.createPlayer(player.getUsername(), player.getPosition(),game, player.getColor());

        p = playerService.setInitialRandomRole(game.getNumberOfPlayers(), game.getNumberOfImpostors(), p);
        game.getPlayers().add(p);
        game.setHostId(p.getId());
        gameRepository.save(game);

        for (int i = 0; i < game.getPlayers().size(); i++) {
            System.out.println("Player ID: " + game.getPlayers().get(i).getId() + " Game Role: " + game.getPlayers().get(i).getRole());
            System.out.println("Host ID: " + game.getHostId());
        }

        return game;
    }

    private String gameCodeGenerator() {
        return CodeGenerator.generateGameCode();
    }

    public Game getGameByCode(String gameCode) {
        return gameRepository.findByGameCode(gameCode);
    }

    public Game startGame(String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            gameRepository.save(game);
        }
        return game;
    }

    public Game setGameAttributes(String gameCode, List<Player> players) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            game.setPlayers(players);
            gameRepository.save(game);
        }
        return game;
    }

    public void triggerEmergency(String gameCode) {
        messagingTemplate.convertAndSend("/topic/" + gameCode + "/emergency", "emergency");
    }

    public void triggerSabotage(String gameCode) {
        messagingTemplate.convertAndSend("/topic/" + gameCode + "/sabotage", "sabotage");
    }
    public void removePlayer(String gameCode, String playerName) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            List<Player> players = game.getPlayers();
            players.removeIf(player -> player.getUsername().equals(playerName));
            game.setPlayers(players);
            gameRepository.save(game);
            messagingTemplate.convertAndSend("/topic/" + gameCode + "/updatedPlayers", game.getPlayers());
        }
    }

    public void handleReport(String gameCode) {
        messagingTemplate.convertAndSend("/topic/" + gameCode + "/report", "report");
    }

    public void collectVotes(String gameCode, Map<String, String> votes) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            Map<String, Long> voteCount = votes.values().stream()
                    .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

            String playerToEliminate = voteCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            if (playerToEliminate != null) {
                Player eliminatedPlayer = game.getPlayers().stream()
                        .filter(p -> p.getUsername().equals(playerToEliminate))
                        .findFirst()
                        .orElse(null);

                if (eliminatedPlayer != null) {
                    boolean isImpostor = "IMPOSTOR".equals(eliminatedPlayer.getRole());
                    boolean gameShouldContinue = game.getPlayers().stream()
                            .anyMatch(p -> "IMPOSTOR".equals(p.getRole()) && !p.getUsername().equals(playerToEliminate));

                    if (isImpostor) {
                        messagingTemplate.convertAndSend("/topic/" + gameCode + "/votingResults",
                                new VotingResult(eliminatedPlayer.getUsername(), eliminatedPlayer.getColor(), true, !gameShouldContinue));
                        if (!gameShouldContinue) {
                            endGame(gameCode);
                        }
                    } else {
                        removePlayer(gameCode, playerToEliminate);
                        messagingTemplate.convertAndSend("/topic/" + gameCode + "/votingResults",
                                new VotingResult(eliminatedPlayer.getUsername(), eliminatedPlayer.getColor(), false, false));
                    }
                }
            }
        }
    }


    public void endGame(String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null) {
            messagingTemplate.convertAndSend("/topic/" + gameCode + "/gameEnd");
            gameRepository.deleteByGameCode(gameCode);
        }
    }
}


