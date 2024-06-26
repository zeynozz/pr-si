package at.fhv.backend.model.com;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KillCom {
    private int killerId;
    private int victimId;
    private String gameCode;
}
