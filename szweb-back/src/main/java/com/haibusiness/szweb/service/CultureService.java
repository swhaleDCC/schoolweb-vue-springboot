package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Culture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CultureService {
    Culture saveCulture(Culture culture);


    void removeCulture(Long id);


    void removeCulturesInBatch(List<Culture> cultures);

    Culture updateCulture(Culture culture);

    Culture getCultureById(Long id);


    List<Culture> listCultures();


    Page<Culture> listCulturesByTitleLike(String title, Pageable pageable);
}
