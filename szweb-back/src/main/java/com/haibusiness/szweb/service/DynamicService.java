package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Dynamic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DynamicService {
    Dynamic saveDynamic(Dynamic dynamic);


    void removeDynamic(Long id);


    void removeDynamicsInBatch(List<Dynamic> dynamics);

    Dynamic updateDynamic(Dynamic dynamic);

    Dynamic getDynamicById(Long id);


    List<Dynamic> listDynamics();


    Page<Dynamic> listDynamicsByTitleLike(String title, Pageable pageable);
}
