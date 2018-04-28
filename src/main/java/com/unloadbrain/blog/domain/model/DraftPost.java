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

    @OneToOne(optional=true, fetch = FetchType.LAZY)
    private Post publishedPost;

}
