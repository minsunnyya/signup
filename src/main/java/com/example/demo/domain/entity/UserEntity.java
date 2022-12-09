package com.example.demo.domain.entity;

//import jdk.internal.util.StaticProperty;
import com.example.demo.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;
    private String userName;
    private String password;

    private UserRole role = UserRole.USER;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;


    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static UserEntity of(String userName, String encodedPwd) {
        UserEntity entity = new UserEntity();
        entity.setUserName(userName);
        entity.setPassword(encodedPwd);
        return entity;
    }
}
