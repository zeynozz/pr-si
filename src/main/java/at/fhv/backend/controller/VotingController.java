package at.fhv.backend.controller;

import at.fhv.backend.model.VoteMessage;
import at.fhv.backend.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class VotingController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GameService gameService;

    private Map<String, Map<String, String>> votes = new HashMap<>();

    @MessageMapping("/castVote")
    public void castVote(@Payload VoteMessage voteMessage) {
        String gameCode = voteMessage.getGameCode();
        String votedPlayer = voteMessage.getVotedPlayer();
        String voter = voteMessage.getVoter();

        votes.putIfAbsent(gameCode, new HashMap<>());
        Map<String, String> gameVotes = votes.get(gameCode);
        gameVotes.put(voter, votedPlayer);

        messagingTemplate.convertAndSend("/topic/" + gameCode + "/voteConfirmation", "Vote casted by: " + voter);
    }

    @MessageMapping("/collectVotes")
    public void collectVotes(@Payload String gameCode) {
        Map<String, Integer> voteCount = new HashMap<>();

        if (votes.containsKey(gameCode)) {
            for (String votedPlayer : votes.get(gameCode).values()) {
                voteCount.put(votedPlayer, voteCount.getOrDefault(votedPlayer, 0) + 1);
            }

            String playerToRemove = determinePlayerToRemove(voteCount);

            if (playerToRemove != null) {
                gameService.removePlayer(gameCode, playerToRemove);
                messagingTemplate.convertAndSend("/topic/" + gameCode + "/votingResults", playerToRemove);
            }

            votes.remove(gameCode);
        }
    }

    private String determinePlayerToRemove(Map<String, Integer> voteCount) {
        return voteCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
