package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.DynamicDao;
import com.haibusiness.szweb.entity.Dynamic;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class DynamicServiceImpl implements DynamicService {


    private DynamicDao dynamicDao;
    @Override
    public Dynamic saveDynamic(Dynamic dynamic) {
        return dynamicDao.save(dynamic);
    }

    @Override
    public void removeDynamic(Long id) {
        dynamicDao.deleteById(id);
    }

    @Override
    public void removeDynamicsInBatch(List<Dynamic> dynamics) {
        dynamicDao.deleteInBatch(dynamics);
    }

    @Override
    public Dynamic updateDynamic(Dynamic dynamic) {
        return dynamicDao.save(dynamic);
    }

    @Override
    public Dynamic getDynamicById(Long id) {
        return dynamicDao.getOne(id);
    }

    @Override
    public List<Dynamic> listDynamics() {
        return dynamicDao.findAll();
    }

    @Override
    public Page<Dynamic> listDynamicsByTitleLike(String title, Pageable pageable) {

        title = "%" + title + "%";
        Page<Dynamic> dynamics = dynamicDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return dynamics;
    }
}
