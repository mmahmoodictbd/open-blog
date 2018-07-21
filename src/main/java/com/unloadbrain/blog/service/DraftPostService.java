package com.unloadbrain.blog.service;

import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;

public interface DraftPostService {

    PostIdentityDTO draftPost(PostDTO postDTO);
}
