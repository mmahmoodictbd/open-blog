package com.unloadbrain.blog.service;

import com.unloadbrain.blog.dto.PostDTO;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AbstractPostService {


    protected void setDefaultSummary(PostDTO postDTO) {
        if (postDTO.getSummary() == null || postDTO.getSummary().trim().length() == 0) {
            Document content = Jsoup.parse(postDTO.getContent());
            Element firstParagraph = content.getElementsByTag("p").first();
            if (firstParagraph != null) {
                postDTO.setSummary(firstParagraph.text());
            }
        }
    }

    protected void setDefaultFeatureImageLink(PostDTO postDTO) {
        if (postDTO.getFeatureImageLink() == null || postDTO.getFeatureImageLink().trim().length() == 0) {
            Optional<String> firstImage = extractFirstImageUrl(postDTO.getContent());
            firstImage.ifPresent(fImage -> postDTO.setFeatureImageLink(fImage));
        }
    }

    protected Optional<String> extractFirstImageUrl(String html) {

        Document content = Jsoup.parse(html);
        Element firstImage = content.getElementsByTag("img").first();
        if (firstImage == null) {
            return Optional.empty();
        }

        String absUrl = firstImage.attr("src");
        if (absUrl != null && absUrl.trim().length() > 0) {
            int index = absUrl.indexOf("/files/");
            if (index > 0) {
                String relativeUrl = absUrl.substring(index);
                return Optional.of(relativeUrl);
            }
        }

        return Optional.empty();
    }

}
