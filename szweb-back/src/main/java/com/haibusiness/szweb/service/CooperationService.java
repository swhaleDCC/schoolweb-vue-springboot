package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Cooperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CooperationService {
    Cooperation saveCooperation(Cooperation cooperation);


    void removeCooperation(Long id);


    void removeCooperationsInBatch(List<Cooperation> cooperations);

    Cooperation updateCooperation(Cooperation cooperation);

    Cooperation getCooperationById(Long id);


    List<Cooperation> listCooperations();


    Page<Cooperation> listCooperationsByTitleLike(String title, Pageable pageable);
}
