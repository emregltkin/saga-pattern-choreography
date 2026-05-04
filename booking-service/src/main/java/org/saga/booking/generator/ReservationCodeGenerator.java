package org.saga.booking.generator;

import java.security.SecureRandom;

public class ReservationCodeGenerator {

    private static final String PREFIX = "RSV_";
    private static final int LENGTH = 8;
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < LENGTH; i++) {
            int index = RANDOM.nextInt(CHARS.length());
            sb.append(CHARS.charAt(index));
        }
        return sb.toString();
    }
}
