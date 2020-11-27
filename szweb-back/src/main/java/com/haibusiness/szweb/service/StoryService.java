package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface StoryService {
    Story saveStory(Story story);
    void removeStory(Long id);
    void removeStorysInBatch(List<Story> storys);
    Story updateStory(Story story);
    Story getStoryById(Long id);
    List<Story> listStorys();
    Page<Story> listStorysByTitleLike(String title, Pageable pageable);
}