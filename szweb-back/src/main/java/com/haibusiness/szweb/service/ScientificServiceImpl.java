package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.ScientificDao;
import com.haibusiness.szweb.entity.Scientific;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ScientificServiceImpl implements ScientificService {

    private ScientificDao scientificDao;
    @Override
    public Scientific saveScientific(Scientific scientific) {

        return scientificDao.save(scientific);
    }

    @Override
    public void removeScientific(Long id) {

        scientificDao.deleteById(id);
    }

    @Override
    public void removeScientificsInBatch(List<Scientific> scientifics) {

        scientificDao.deleteInBatch(scientifics);
    }

    @Override
    public Scientific updateScientific(Scientific scientific) {

        return scientificDao.save(scientific);
    }

    @Override
    public Scientific getScientificById(Long id) {

        return scientificDao.getOne(id);
    }

    @Override
    public List<Scientific> listScientifics() {

        return scientificDao.findAll();
    }

    @Override
    public Page<Scientific> listScientificsByTypeAndTitleLike(String type,String title, Pageable pageable) {
        title = "%" + title + "%";
        return scientificDao.findByTypeAndTitleLikeOrderByUpdateTimeDesc(type, title, pageable);
    }


}
