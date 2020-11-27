package com.haibusiness.szweb.dao;


import com.haibusiness.szweb.entity.Download;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadDao extends JpaRepository<Download,Long> {
    Page<Download> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);
}
