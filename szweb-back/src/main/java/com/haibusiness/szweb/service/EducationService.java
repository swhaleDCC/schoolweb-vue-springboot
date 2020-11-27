package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Education;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EducationService {
    Education saveEducation(Education education);


    void removeEducation(Long id);


    void removeEducationsInBatch(List<Education> educations);

    Education updateEducation(Education education);

    Education getEducationById(Long id);


    List<Education> listEducations();


    Page<Education> listEducationsByTypeAndTitleLike(String type, String title, Pageable pageable);
}
