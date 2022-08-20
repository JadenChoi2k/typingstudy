package com.typingstudy.common.config.jwt.refresh;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    private Long id; // userId

    @Setter // set when requested refresh token
    @Column(unique = true, length = 500)
    private String token;

    public RefreshToken(Long id, String token) {
        this.id = id;
        this.token = token;
    }
}
