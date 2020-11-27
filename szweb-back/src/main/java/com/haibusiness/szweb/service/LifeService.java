package com.haibusiness.szweb.service;


import com.haibusiness.szweb.entity.Life;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LifeService {
    Life saveLife(Life dynamic);


    void removeLife(Long id);


    void removeLifesInBatch(List<Life> lifes);

    Life updateLife(Life life);

    Life getLifeById(Long id);


    List<Life> listLifes();


    Page<Life> listLifesByTypeAndTitleLike(String type, String title, Pageable pageable);
}
