package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Authority;
import org.springframework.stereotype.Repository;


public interface AuthorityService {
    Authority getAuthorityById(Long id);
}
