package com.primenumber.stackoverflow.entity;

import com.primenumber.stackoverflow.entity.util.BasicStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BasicStatus status = BasicStatus.ACTIVE;

    private Tag(String name) {
        this.name = name;
    }

    public static Tag of(String name) {
        return new Tag(name);
    }

    public void setName(String name) {
        this.name = name;
    }
}
