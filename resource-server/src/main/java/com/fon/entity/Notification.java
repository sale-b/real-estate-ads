package com.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Notification extends BaseEntity{

    @Id
    @SequenceGenerator(name = "notification_id_seq",
            sequenceName = "notification_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "notification_id_seq")
    @Column(name = "id", updatable = false, insertable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "real_estate_id", nullable = false)
    private RealEstate realEstate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filter_id")
    @JsonIgnore
    private Filter filter;

    @Column(name = "filter_id", insertable = false, updatable = false, nullable = false)
    private Long filterId;

    private Boolean seen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Notification that = (Notification) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
