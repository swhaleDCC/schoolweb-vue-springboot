package com.haibusiness.szweb.dao;

import com.haibusiness.szweb.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeDao extends JpaRepository<Notice,Long> {
    Page<Notice> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);
}
