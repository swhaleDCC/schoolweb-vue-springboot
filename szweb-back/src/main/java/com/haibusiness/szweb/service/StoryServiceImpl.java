package com.haibusiness.szweb.service;


import com.haibusiness.szweb.dao.StoryDao;
import com.haibusiness.szweb.entity.Story;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class StoryServiceImpl implements StoryService {
    private StoryDao storyDao;
    @Override
    public Story saveStory(Story story) {
        return storyDao.save(story);
    }
    @Override
    public void removeStory(Long id) {
        storyDao.deleteById(id);
    }
    @Override
    public void removeStorysInBatch(List<Story> storys) {
        storyDao.deleteInBatch(storys);
    }
    @Override
    public Story updateStory(Story story) {
        return storyDao.save(story);
    }
    @Override
    public Story getStoryById(Long id) {
        return storyDao.getOne(id);
    }
    @Override
    public List<Story> listStorys() {
        return storyDao.findAll();
    }
    @Override
    public Page<Story> listStorysByTitleLike(String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Story> storys = storyDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return storys;
    }
}
