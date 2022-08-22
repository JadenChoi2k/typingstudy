package com.typingstudy.common.config.jwt;

public class JwtProperty {
    public static final int TIMEOUT = 60000 * 15; // 15분
    public static final String SECRET = "}P3j6BG;`s/&@acE"; // jwt 비밀키. 절대 누출돼서는 안됨.
    public static final String JWT_HEADER = "Authorization";

    public static final String REFRESH_HEADER = "Refresh-Jwt";
    public static final String JWT_PREFIX = "Bearer ";
}
