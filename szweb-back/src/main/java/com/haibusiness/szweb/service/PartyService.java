package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PartyService {
    Party saveParty(Party notice);


    void removeParty(Long id);


    void removePartysInBatch(List<Party> partys);

    Party updateParty(Party party);

    Party getPartyById(Long id);


    List<Party> listPartys();


    Page<Party> listPartysByTitleLike(String title, Pageable pageable);
}
