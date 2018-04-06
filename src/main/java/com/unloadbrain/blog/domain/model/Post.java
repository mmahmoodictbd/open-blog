package com.unloadbrain.blog.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Post extends BaseEntity {

    @Column
    @Length(min = 1, max = 255)
    private String title;

    @Column
    @Lob
    private String content;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Category> categories;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Tag> tags = new LinkedHashSet<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus status;

    @Column
    private Date publishDate;

}
