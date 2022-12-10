package com.example.demo.domain.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.example.demo.domain.entity.UserEntity;

import javax.persistence.*;


@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    private String title;

    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public static PostEntity of(String title, String body, UserEntity user) {
        PostEntity entity = new PostEntity();
        entity.setTitle(title);
        entity.setBody(body);
        entity.setUser(user);
        return entity;
    }
}
