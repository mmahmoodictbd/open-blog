package com.unloadbrain.blog.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Length(min = 1, max = 255)
    private String title;

    @Column
    @Lob
    private String text;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Category> categories;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Tag> tags;

    @Column
    private Date publishDate;

    @Column(updatable = false)
    @NotNull
    @CreatedDate
    private Date createdAt;

    @Column
    @NotNull
    @LastModifiedDate
    private Date updatedAt;
}
