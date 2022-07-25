package com.typingstudy.common.utils;

import java.util.Random;

public abstract class TokenGenerator {
    static final int LEFT_BOUND = 48; // '0'
    static final int RIGHT_BOUND = 122; // 'z'
    static final int DEFAULT_LENGTH = 30;

    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    public static String generate(int length) {
        return new Random().ints(LEFT_BOUND, RIGHT_BOUND + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
