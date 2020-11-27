package com.haibusiness.szweb.dao;

import com.haibusiness.szweb.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryDao extends JpaRepository<Story,Long> {
    Page<Story> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);
}
