package com.haibusiness.szweb.dao;

import com.haibusiness.szweb.entity.Graduate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GraduateDao extends JpaRepository<Graduate,Long> {
    Page<Graduate> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);

}
