package com.fon.entity;

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
@MappedSuperclass
@Data
@ToString
public abstract class BaseEntity {
    @Column(name = "created_on", nullable = false)
    @NotNull
    protected LocalDateTime createdOn;

    @Column(name = "modified_on", nullable = false)
    @NotNull
    protected LocalDateTime modifiedOn;

}
