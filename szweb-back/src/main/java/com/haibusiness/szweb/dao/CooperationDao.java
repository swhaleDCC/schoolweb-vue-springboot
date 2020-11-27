package com.haibusiness.szweb.dao;

import com.haibusiness.szweb.entity.Cooperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CooperationDao extends JpaRepository<Cooperation,Long> {
    Page<Cooperation> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);
}
