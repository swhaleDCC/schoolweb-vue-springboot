package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.GraduateDao;
import com.haibusiness.szweb.entity.Graduate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class GraduateServiceImpl implements GraduateService {

    private GraduateDao graduateDao;
    @Override
    public Graduate saveGraduate(Graduate graduate) {

        return graduateDao.save(graduate);
    }

    @Override
    public void removeGraduate(Long id) {

        graduateDao.deleteById(id);
    }

    @Override
    public void removeGraduatesInBatch(List<Graduate> graduates) {

        graduateDao.deleteInBatch(graduates);
    }

    @Override
    public Graduate updateGraduate(Graduate graduate) {

        return graduateDao.save(graduate);
    }

    @Override
    public Graduate getGraduateById(Long id) {

        return graduateDao.getOne(id);
    }

    @Override
    public List<Graduate> listGraduates() {

        return graduateDao.findAll();
    }

    @Override
    public Page<Graduate> listGraduatesByTitleLike(String title, Pageable pageable) {

        title = "%" + title + "%";
        Page<Graduate> graduates = graduateDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return graduates;
    }
}
