package com.fon.entity;

import com.fon.util.BaseEntityListener;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@EntityListeners(BaseEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "created_on", nullable = false)
    @NotNull
    protected LocalDateTime createdOn;

    @Column(name = "modified_on", nullable = false)
    @NotNull
    protected LocalDateTime modifiedOn;

    @PrePersist
    public void prePersist() {
        this.createdOn = LocalDateTime.now();
        this.modifiedOn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedOn = LocalDateTime.now();
    }
}
