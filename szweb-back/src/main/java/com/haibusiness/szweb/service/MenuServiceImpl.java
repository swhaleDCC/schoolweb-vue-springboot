package com.haibusiness.szweb.service;


import com.haibusiness.szweb.dao.MenuDao;
import com.haibusiness.szweb.entity.Authority;
import com.haibusiness.szweb.entity.Menu;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class MenuServiceImpl implements MenuService {

    private MenuDao menuDao;
    @Override
    public Menu saveMenu(Menu menu) {
        return menuDao.save(menu);
    }

    @Override
    public void removeMenu(Long id) {
        menuDao.deleteById(id);
    }

    @Override
    public void removeMenusInBatch(List<Menu> menus) {
        menuDao.deleteInBatch(menus);
    }

    @Override
    public Menu updateMenu(Menu menu) {
        return menuDao.save(menu);
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuDao.getOne(id);
    }

    @Override
    public List<Menu> listMenus() {
        return menuDao.findAll(new Sort(Sort.Direction.ASC,"id"));
    }
    @Override
    public Page<Menu> listMenusByTitleLike(String title, Pageable pageable) {

        title = "%" + title + "%";
        Page<Menu> menus = menuDao.findByTitleLikeOrderByUpdateTimeDesc(title, pageable);
        return menus;
    }

    @Override
    public Menu getMenuByName(String name) {
        return menuDao.findByName(name);
    }

    @Override

    public Map<String, String> getMenus() {
        return menuDao.findAll(new Sort(Sort.Direction.ASC,"id")).stream().collect(Collectors.toMap(Menu::getName,Menu::getTitle));
    }

    @Override
    public List<Menu> getNonAdminMenus() {

        return menuDao.findByParentIsNullOrderByIdAsc()
                .stream().filter((menu)->(menu.getRoles()
                        .contains(new Authority("ROLE_PUBLIC"))))
                .collect(Collectors.toList());
    }
    @Override
    public List<Menu> getAdminMenus(boolean isAdmin) {
        List<Menu> menus=menuDao.findByParentIsNullOrderByIdAsc();
        if(isAdmin){
            return menus;
        }else{
           return menus.stream().filter((menu)->(!menu.getRoles()
                    .contains(new Authority("ROLE_ADMIN"))))
                    .collect(Collectors.toList());
        }

    }
}