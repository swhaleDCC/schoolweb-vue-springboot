package com.haibusiness.szweb.dao;

import com.haibusiness.szweb.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityDao extends JpaRepository<Community,Long> {
    Page<Community> findByTypeAndTitleLikeOrderByUpdateTimeDesc(String type, String title, Pageable pageable);

}
