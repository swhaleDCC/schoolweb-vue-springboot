package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Scientific;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScientificService {
    Scientific saveScientific(Scientific scientific);


    void removeScientific(Long id);


    void removeScientificsInBatch(List<Scientific> scientifics);

    Scientific updateScientific(Scientific scientific);

    Scientific getScientificById(Long id);


    List<Scientific> listScientifics();


    Page<Scientific> listScientificsByTypeAndTitleLike(String type, String title, Pageable pageable);
}
