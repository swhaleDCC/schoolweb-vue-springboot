package com.haibusiness.szweb.dao;

import com.haibusiness.szweb.entity.Culture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CultureDao extends JpaRepository<Culture,Long> {
    Page<Culture> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);
}
