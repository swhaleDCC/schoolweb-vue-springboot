package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Carousel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarouselService {
    Carousel saveCarousel(Carousel carousel);


    void removeCarousel(Long id);


    void removeCarouselsInBatch(List<Carousel> carousels);

    Carousel updateCarousel(Carousel carousel);

    Carousel getCarouselById(Long id);


    List<Carousel> listCarousels();


    Page<Carousel> listCarouselsByTitleLike(String name, Pageable pageable);
}
