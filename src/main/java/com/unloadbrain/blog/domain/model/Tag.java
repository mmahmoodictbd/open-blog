package com.unloadbrain.blog.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 */
@Getter
@Setter
@Entity
public class Tag extends BaseEntity {

    @Column
    @Length(min = 1, max = 255)
    private String name;

    @Column
    @Length(min = 1, max = 255)
    private String slug;

}
