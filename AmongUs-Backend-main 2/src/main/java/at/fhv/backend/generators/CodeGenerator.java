package at.fhv.backend.generators;

import java.security.SecureRandom;

public class CodeGenerator {
    private static final String chr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int len = 6;

    public static String generateGameCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chr.length());
            code.append(chr.charAt(randomIndex));
        }

        return code.toString();
    }

    public static void main(String[] args) {
        String gameCode = generateGameCode();
        System.out.println("Code Generator: " + gameCode);
    }
}
