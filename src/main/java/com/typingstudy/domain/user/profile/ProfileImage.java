package com.typingstudy.domain.user.profile;

import com.typingstudy.domain.BaseTimeEntity;
import com.typingstudy.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProfileImage extends BaseTimeEntity {

    @Column(name = "profile_image_id")
    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private String extension;

    private byte[] data;

    public ProfileImage(User user, String extension, byte[] data) {
        this.user = user;
        this.extension = extension;
        this.data = data;
    }
}
