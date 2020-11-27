package com.haibusiness.szweb.dao;

import com.haibusiness.szweb.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentDao extends JpaRepository<Enrollment, Long> {
    Page<Enrollment> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);
}
