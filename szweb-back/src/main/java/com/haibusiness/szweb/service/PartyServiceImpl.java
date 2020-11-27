package com.haibusiness.szweb.service;


import com.haibusiness.szweb.dao.PartyDao;
import com.haibusiness.szweb.entity.Party;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PartyServiceImpl implements PartyService {

    private PartyDao partyDao;
    @Override
    public Party saveParty(Party party) {
        return partyDao.save(party);
    }

    @Override
    public void removeParty(Long id) {
        partyDao.deleteById(id);
    }

    @Override
    public void removePartysInBatch(List<Party> partys) {
        partyDao.deleteInBatch(partys);
    }

    @Override
    public Party updateParty(Party party) {
        return partyDao.save(party);
    }

    @Override
    public Party getPartyById(Long id) {
        return partyDao.getOne(id);
    }

    @Override
    public List<Party> listPartys() {
        return partyDao.findAll();
    }

    @Override
    public Page<Party> listPartysByTitleLike(String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Party> partys = partyDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return partys;
    }
}
