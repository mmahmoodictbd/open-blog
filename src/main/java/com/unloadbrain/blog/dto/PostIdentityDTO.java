package com.unloadbrain.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class PostIdentityDTO {

    @NotNull
    private Long id;

    @NotNull
    private CurrentPostStatusDTO status;

}
