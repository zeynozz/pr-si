package at.fhv.backend.model;

import lombok.Getter;
import lombok.Setter;

public class VotingResult {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String color;
    @Getter
    @Setter
    private boolean isImpostor;
    @Getter
    @Setter
    private boolean isLastImpostor;

    public VotingResult(String username, String color, boolean isImpostor, boolean isLastImpostor) {
        this.username = username;
        this.color = color;
        this.isImpostor = isImpostor;
        this.isLastImpostor = isLastImpostor;
    }
}
