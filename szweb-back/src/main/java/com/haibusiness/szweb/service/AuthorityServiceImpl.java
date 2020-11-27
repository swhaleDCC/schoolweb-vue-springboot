package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.AuthorityDao;
import com.haibusiness.szweb.entity.Authority;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AuthorityServiceImpl implements AuthorityService{

    private AuthorityDao authorityDao;


    public Authority getAuthorityById(Long id) {
        return authorityDao.getOne(id);
    }
}
