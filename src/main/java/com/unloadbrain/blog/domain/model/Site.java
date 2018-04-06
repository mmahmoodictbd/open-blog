package com.unloadbrain.blog.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Getter
@Setter
@Entity
public class Site extends BaseEntity {

    @Column
    @Length(min = 1, max = 255)
    private String name;

    @Column
    @Lob
    private String description;

    @Column
    @Length(min = 1, max = 255)
    private String siteUrl;

    @Column
    @Length(min = 1, max = 255)
    private String homeUrl;

}
