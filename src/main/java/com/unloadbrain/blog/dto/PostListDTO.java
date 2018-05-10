package com.unloadbrain.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PostListDTO {

    @NotNull
    private String id;

    @NotNull
    private String title;

    @NotNull
    private String status;

    @NotNull
    private String updated_at;

}
