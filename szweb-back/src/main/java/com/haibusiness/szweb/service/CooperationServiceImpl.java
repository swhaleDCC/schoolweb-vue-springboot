package com.haibusiness.szweb.service;


import com.haibusiness.szweb.dao.CooperationDao;
import com.haibusiness.szweb.entity.Cooperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CooperationServiceImpl implements CooperationService {

    private CooperationDao cooperationDao;
    @Override
    public Cooperation saveCooperation(Cooperation cooperation) {
        return cooperationDao.save(cooperation);
    }

    @Override
    public void removeCooperation(Long id) {
        cooperationDao.deleteById(id);
    }

    @Override
    public void removeCooperationsInBatch(List<Cooperation> cooperations) {
        cooperationDao.deleteInBatch(cooperations);
    }

    @Override
    public Cooperation updateCooperation(Cooperation cooperation) {
        return cooperationDao.save(cooperation);
    }

    @Override
    public Cooperation getCooperationById(Long id) {
        return cooperationDao.getOne(id);
    }

    @Override
    public List<Cooperation> listCooperations() {
        return cooperationDao.findAll();
    }

    @Override
    public Page<Cooperation> listCooperationsByTitleLike(String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Cooperation> cooperations = cooperationDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return cooperations;
    }
}
