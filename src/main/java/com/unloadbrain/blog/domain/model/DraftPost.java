package com.unloadbrain.blog.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Getter
@Setter
@ToString
@Entity
public class DraftPost extends Post {

    @OneToOne(fetch = FetchType.LAZY)
    private PublishedPost publishedPost;

}
