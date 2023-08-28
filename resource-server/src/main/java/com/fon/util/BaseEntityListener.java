package com.fon.util;

import com.fon.entity.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class BaseEntityListener {

    @PrePersist
    public void prePersist(BaseEntity entity) {
        entity.prePersist();
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.preUpdate();
    }
}