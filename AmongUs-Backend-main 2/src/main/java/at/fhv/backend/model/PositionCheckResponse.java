package at.fhv.backend.model;

public class PositionCheckResponse {
    private int playerId;
    private boolean isAdjacent;

    public PositionCheckResponse(int playerId, boolean isAdjacent) {
        this.playerId = playerId;
        this.isAdjacent = isAdjacent;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isAdjacent() {
        return isAdjacent;
    }
}
