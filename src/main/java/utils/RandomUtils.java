package utils;

import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = new Random();
    private static final String RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final char[] RANDOM_CHARS_ARRAY;
    private static final int BOUND;
    static {
        BOUND = RANDOM_CHARS.length();
        RANDOM_CHARS_ARRAY = RANDOM_CHARS.toCharArray();
    }

    public static String generateRandomString(int targetLength) {
        StringBuilder builder = new StringBuilder();
        for (int generateCount = 0; generateCount < targetLength; generateCount++) {
            int index = RANDOM.nextInt(BOUND);
            builder.append(RANDOM_CHARS_ARRAY[index]);
        }
        return builder.toString();
    }

    public static int randomInt() {
        return RANDOM.nextInt();
    }

    public static int randomInt(int maxNum) {
        return RANDOM.nextInt(maxNum);
    }

}
