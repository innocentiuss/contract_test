package utils;

import java.util.Calendar;
import java.util.Date;
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

    public static Date generateRandomDate() {

        // 设置年份范围，假设是 1900 到 2024 年
        int year = 1972 + RANDOM.nextInt(70); // 2024 - 1900 + 1

        // 设置月份范围，1 到 12 月
        int month = 1 + RANDOM.nextInt(12);

        // 设置日期范围，这里简单地假设每月最多有 31 天
        int day = 1 + RANDOM.nextInt(31);

        // 创建 Calendar 对象并设置年月日
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar.MONTH 从 0 开始
        calendar.set(Calendar.DAY_OF_MONTH, day);

        // 返回生成的 Date 对象
        return calendar.getTime();
    }

}
