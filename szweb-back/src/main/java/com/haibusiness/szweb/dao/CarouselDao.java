package com.haibusiness.szweb.dao;


import com.haibusiness.szweb.entity.Carousel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarouselDao extends JpaRepository<Carousel,Long> {
    Page<Carousel> findByTitleLikeOrderByUpdateTimeDesc(String title, Pageable pageable);

}
