package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.UserDao;
import com.haibusiness.szweb.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserDao userDao;
    @Override
    public User saveUser(User user) {
        return userDao.save(user);
    }
    @Override
    public void removeUser(Long id) {
        userDao.deleteById(id);
    }
    @Override
    public void removeUsersInBatch(List<User> users) {
        userDao.deleteInBatch(users);
    }
    @Override
    public User updateUser(User user) {
        return userDao.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getOne(id);
    }

    @Override
    public List<User> listUsers() {
        return userDao.findAll();
    }

    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        // 模糊查询
        name = "%" + name + "%";
        Page<User> users = userDao.findByNameLike(name, pageable);
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userDao.findByUsername(username);
    }
}
