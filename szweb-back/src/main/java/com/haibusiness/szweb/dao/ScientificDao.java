package com.haibusiness.szweb.dao;


import com.haibusiness.szweb.entity.Scientific;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScientificDao extends JpaRepository<Scientific,Long> {
    Page<Scientific> findByTypeAndTitleLikeOrderByUpdateTimeDesc(String type, String title, Pageable pageable);
}
