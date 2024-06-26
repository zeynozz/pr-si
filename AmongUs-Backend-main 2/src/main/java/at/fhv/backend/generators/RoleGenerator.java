package at.fhv.backend.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoleGenerator {
    public static List<Integer> imposters = new ArrayList<>();

    public static List<Integer> generatePlayerRole(int numPlayers, int numImpostors) {
        imposters = new ArrayList<>();
        Random random = new Random();

        while (imposters.size() < numImpostors) {
            int randomIndex = random.nextInt(numPlayers);
            if (!imposters.contains(randomIndex)) {
                imposters.add(randomIndex);
            }
        }
        return imposters;
    }

    public static List<Integer> getImpostorsIndices() {
        return imposters;
    }

}