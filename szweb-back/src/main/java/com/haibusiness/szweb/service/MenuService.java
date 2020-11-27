package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface MenuService {

    Menu saveMenu(Menu menu);


    void removeMenu(Long id);


    void removeMenusInBatch(List<Menu> menus);

    Menu updateMenu(Menu menu);


    Menu getMenuById(Long id);


    List<Menu> listMenus();

    Page<Menu> listMenusByTitleLike(String title, Pageable pageable);

    Map<String,String> getMenus();
    Menu getMenuByName(String name);
    List<Menu> getNonAdminMenus();
    List<Menu> getAdminMenus(boolean isAdmin);
}
