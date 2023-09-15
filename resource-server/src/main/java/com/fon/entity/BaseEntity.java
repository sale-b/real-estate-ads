package com.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    protected LocalDateTime modifiedOn;

    @PrePersist
    public void prePersist() {
        this.createdOn = LocalDateTime.now();
        this.modifiedOn = this.createdOn;
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedOn = LocalDateTime.now();
    }
}
