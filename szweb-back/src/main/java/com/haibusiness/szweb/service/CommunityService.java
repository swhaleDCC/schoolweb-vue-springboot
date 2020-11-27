package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommunityService{
    Community saveCommunity(Community community);


    void removeCommunity(Long id);


    void removeCommunitysInBatch(List<Community> communitys);

    Community updateCommunity(Community community);

    Community getCommunityById(Long id);


    List<Community> listCommunitys();


    Page<Community> listCommunitysByTypeAndTitleLike(String type, String title, Pageable pageable);
    long count();
}
