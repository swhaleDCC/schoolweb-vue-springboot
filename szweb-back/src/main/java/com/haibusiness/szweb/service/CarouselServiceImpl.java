package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.CarouselDao;
import com.haibusiness.szweb.entity.Carousel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CarouselServiceImpl implements CarouselService {

    private CarouselDao carouselDao;
    @Override
    public Carousel saveCarousel(Carousel carousel)
    {
        return carouselDao.save(carousel);
    }

    @Override
    public void removeCarousel(Long id) {
        carouselDao.deleteById(id);
    }

    @Override
    public void removeCarouselsInBatch(List<Carousel> carousels) {
        carouselDao.deleteInBatch(carousels);
    }

    @Override
    public Carousel updateCarousel(Carousel carousel) {

        return carouselDao.save(carousel);
    }

    @Override
    public Carousel getCarouselById(Long id)
    {
        return carouselDao.getOne(id);
    }

    @Override
    public List<Carousel> listCarousels() {

        return carouselDao.findAll();
    }

    @Override
    public Page<Carousel> listCarouselsByTitleLike(String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Carousel> carousels = carouselDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return carousels;
    }
}
