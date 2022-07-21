package com.example.moview.moview.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Column(updatable = false)
    protected LocalDateTime created;

    @Column
    protected LocalDateTime modified;

    @PrePersist
    public void prePersist() {
        created = now();
    }

    @PreUpdate
    public void preUpdate() {
        modified = now();
    }
}