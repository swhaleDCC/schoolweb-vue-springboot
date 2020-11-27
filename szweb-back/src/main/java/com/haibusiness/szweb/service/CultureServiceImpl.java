package com.haibusiness.szweb.service;


import com.haibusiness.szweb.dao.CultureDao;
import com.haibusiness.szweb.entity.Culture;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CultureServiceImpl implements CultureService {

    private CultureDao cultureDao;
    @Override
    public Culture saveCulture(Culture culture) {
        return cultureDao.save(culture);
    }

    @Override
    public void removeCulture(Long id) {
        cultureDao.deleteById(id);
    }

    @Override
    public void removeCulturesInBatch(List<Culture> cultures) {
        cultureDao.deleteInBatch(cultures);
    }

    @Override
    public Culture updateCulture(Culture culture) {
        return cultureDao.save(culture);
    }

    @Override
    public Culture getCultureById(Long id) {
        return cultureDao.getOne(id);
    }

    @Override
    public List<Culture> listCultures() {
        return cultureDao.findAll();
    }

    @Override
    public Page<Culture> listCulturesByTitleLike(String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Culture> cultures = cultureDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return cultures;
    }
}
