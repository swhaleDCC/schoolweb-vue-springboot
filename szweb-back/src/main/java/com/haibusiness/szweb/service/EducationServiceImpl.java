package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.EducationDao;
import com.haibusiness.szweb.entity.Education;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class EducationServiceImpl implements EducationService {

    private EducationDao educationDao;
    @Override
    public Education saveEducation(Education education) {

        return educationDao.save(education);
    }

    @Override
    public void removeEducation(Long id) {

        educationDao.deleteById(id);
    }

    @Override
    public void removeEducationsInBatch(List<Education> educations) {

        educationDao.deleteInBatch(educations);
    }

    @Override
    public Education updateEducation(Education education) {

        return educationDao.save(education);
    }

    @Override
    public Education getEducationById(Long id) {

        return educationDao.getOne(id);
    }

    @Override
    public List<Education> listEducations() {

        return educationDao.findAll();
    }

    @Override
    public Page<Education> listEducationsByTypeAndTitleLike(String type, String title, Pageable pageable) {
        title = "%" + title + "%";
        return educationDao.findByTypeAndTitleLikeOrderByUpdateTimeDesc(type, title, pageable);
    }


}
