package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.LifeDao;
import com.haibusiness.szweb.entity.Life;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class LifeServiceImpl implements LifeService {

    private LifeDao lifeDao;
    @Override
    public Life saveLife(Life life) {

        return lifeDao.save(life);
    }

    @Override
    public void removeLife(Long id) {

        lifeDao.deleteById(id);
    }

    @Override
    public void removeLifesInBatch(List<Life> lifes) {

        lifeDao.deleteInBatch(lifes);
    }

    @Override
    public Life updateLife(Life life) {

        return lifeDao.save(life);
    }

    @Override
    public Life getLifeById(Long id) {

        return lifeDao.getOne(id);
    }

    @Override
    public List<Life> listLifes() {

        return lifeDao.findAll();
    }
    @Override
    public Page<Life> listLifesByTypeAndTitleLike(String type, String title, Pageable pageable) {
        title = "%" + title + "%";
        return lifeDao.findByTypeAndTitleLikeOrderByUpdateTimeDesc(type, title, pageable);
    }


}
