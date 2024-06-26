package at.fhv.backend.model.com;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveCom {
    private int id;
    private String keyCode;
    private String gameCode;
    private String action;


    public MoveCom(int id, String keyCode, String gameCode) {
        this.id = id;
        this.keyCode = keyCode;
        this.gameCode = gameCode;
    }
}