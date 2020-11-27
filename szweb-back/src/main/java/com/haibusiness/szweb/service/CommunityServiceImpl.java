package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.CommunityDao;
import com.haibusiness.szweb.entity.Community;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor

public class CommunityServiceImpl  implements CommunityService {


    private CommunityDao communityDao;
    @Override
    public Community saveCommunity(Community community) {
        return communityDao.save(community);
    }

    @Override
    public void removeCommunity(Long id) {
        communityDao.deleteById(id);
    }

    @Override
    public void removeCommunitysInBatch(List<Community> communitys) {
        communityDao.deleteInBatch(communitys);
    }

    @Override
    public Community updateCommunity(Community community) {
        return communityDao.save(community);
    }

    @Override
    public Community getCommunityById(Long id) {
        return communityDao.getOne(id);
    }

    @Override
    public List<Community> listCommunitys() {
        return communityDao.findAll();
    }

    @Override
    public Page<Community> listCommunitysByTypeAndTitleLike(String type,String title, Pageable pageable) {

        title = "%" + title + "%";
        return communityDao.findByTypeAndTitleLikeOrderByUpdateTimeDesc(type, title, pageable);
    }

    @Override
    public long count(){
        return communityDao.count();
    }
}
