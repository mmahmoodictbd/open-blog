package com.unloadbrain.blog.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
public class PublishedPost extends Post {

    @Column
    private Date publishDate;

}
