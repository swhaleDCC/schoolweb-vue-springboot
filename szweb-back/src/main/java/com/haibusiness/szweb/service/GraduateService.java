package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Graduate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GraduateService {

    Graduate saveGraduate(Graduate graduate);


    void removeGraduate(Long id);


    void removeGraduatesInBatch(List<Graduate> graduates);

    Graduate updateGraduate(Graduate graduate);

    Graduate getGraduateById(Long id);


    List<Graduate> listGraduates();


    Page<Graduate> listGraduatesByTitleLike(String title, Pageable pageable);
}
