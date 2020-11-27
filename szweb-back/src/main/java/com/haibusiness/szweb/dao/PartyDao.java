package com.haibusiness.szweb.dao;

import com.haibusiness.szweb.entity.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyDao extends JpaRepository<Party,Long> {
    Page<Party> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);
}
