package com.example.customuser2.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseByEntity {

    @CreatedBy
    @Column(name = "created_by", length = 20, nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 20, nullable = false)
    private String updatedBy;
}
