package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;

public class VoteMessage {

    @Getter
    @Setter
    private String gameCode;
    @Getter
    @Setter
    private String votedPlayer;
    @Getter
    @Setter
    private String voter;

    public VoteMessage() {
    }

    public VoteMessage(String gameCode, String votedPlayer, String voter) {
        this.gameCode = gameCode;
        this.votedPlayer = votedPlayer;
        this.voter = voter;
    }
}
