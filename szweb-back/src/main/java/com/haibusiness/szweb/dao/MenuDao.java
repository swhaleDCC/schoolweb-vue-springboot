package com.haibusiness.szweb.dao;

import com.haibusiness.szweb.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDao extends JpaRepository<Menu,Long> {
    Page<Menu> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);
    Menu findByName(String name);
    List<Menu> findByParentIsNullOrderByIdAsc();
}
