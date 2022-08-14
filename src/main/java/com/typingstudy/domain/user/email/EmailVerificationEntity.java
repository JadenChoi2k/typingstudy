package com.typingstudy.domain.user.email;

import com.typingstudy.common.exception.InvalidParameterException;
import lombok.Getter;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class EmailVerificationEntity {
    private String email; // 사용자의 이메일
    private String verifyCode; // 6자리의 무작위 숫자
    private State state; // true: 인증됨, false: 아직 인증 안됨

    public enum State {
        NONE, OVERLAPPED, WAITING, VERIFIED, FAILED
    }

    public EmailVerificationEntity(String email) {
        if (email == null || email.isEmpty()) throw new InvalidParameterException("email must not be empty");
        this.email = email;
        this.verifyCode = Integer.toString(ThreadLocalRandom.current().nextInt(100000, 1000000)).substring(0, 6);
        this.state = State.NONE;
    }

    public State onOverlapped() {
        return this.state = State.OVERLAPPED;
    }

    public State onSend() {
        return this.state = State.WAITING;
    }

    public State onFail() {
        return this.state = State.FAILED;
    }

    public boolean verify(String code) {
        if (verifyCode.equals(code)) {
            this.state = State.VERIFIED;
        }
        return this.state == State.VERIFIED;
    }
}
