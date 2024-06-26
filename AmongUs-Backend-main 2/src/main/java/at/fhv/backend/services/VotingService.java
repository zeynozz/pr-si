package at.fhv.backend.services;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class VotingService {

    private Map<String, Map<String, Integer>> gameVotes = new HashMap<>();
    private Map<String, String[]> gamePlayers = new HashMap<>();

    public void startVoting(String gameCode, String[] players) {
        gamePlayers.put(gameCode, players);
        gameVotes.put(gameCode, new HashMap<>());
    }

    public void castVote(String gameCode, String votedPlayer) {
        gameVotes.computeIfAbsent(gameCode, k -> new HashMap<>());
        gameVotes.get(gameCode).merge(votedPlayer, 1, Integer::sum);
    }

    public String endVoting(String gameCode) {
        Map<String, Integer> votes = gameVotes.get(gameCode);
        if (votes == null) {
            return null;
        }
        String votedOutPlayer = votes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        gameVotes.remove(gameCode);
        return votedOutPlayer;
    }
}

