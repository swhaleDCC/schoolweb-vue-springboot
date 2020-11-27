package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Authority;
import com.haibusiness.szweb.entity.Menu;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.MenuService;
import com.haibusiness.szweb.util.ConstraintViolationExceptionHandler;
import com.haibusiness.szweb.vo.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class MenuController {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userDetailsServicee;
    private MenuService menuService;
    @GetMapping("/menu")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Menu> page = menuService.listMenusByTitleLike(title, pageable);
        List<Menu> list = page.getContent();
        Map model = new HashMap<>();
        model.put("page", page);
        model.put("menuList", list);
        model.put("name", menuService.getMenus().get("menu"));
        return ResponseEntity.ok().body(model);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @GetMapping(value = "/backMenus")
    public ResponseEntity getBackMenus() {
        UserDetails userDetails = com.haibusiness.szweb.security.SecurityContextHolder.getUserDetails();
        User user = (User)userDetailsServicee.loadUserByUsername(userDetails.getUsername());
        List<Authority> authorities=(List<Authority>)user.getAuthorities();
        boolean isAdmin=false;
        for (Authority auth:authorities) {
            if(auth.getName().equals("ROLE_ADMIN")){
                isAdmin=true;
            }
        }
        return ResponseEntity.ok(menuService.getAdminMenus(isAdmin));
    }

    @GetMapping("/public/frontMenus")
    public ResponseEntity getFrontMenus() {
        return ResponseEntity.ok(menuService.getNonAdminMenus());
    }
    /**
     * 新建和更新
     *
     * @param menu
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/menu")
    public ResponseEntity<Response> create(@RequestBody Menu menu, HttpServletRequest request) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            menu.setUpdateUser(user);
            menu.setUpdateTime(new Date());
            String parentName = request.getParameter("parentName");
            System.err.println("parentName= " + parentName);
            if (parentName != null && !parentName.equals("")) {
                Menu parentMenu = menuService.getMenuByName(parentName);
                menu.setParent(parentMenu);
            } else {
                menu.setParent(null);
            }
            menu.setUpdateTime(new Date());
            menu.setUpdateUser(user);
            menuService.saveMenu(menu);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }


    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/menu/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id) {
        try {
            menuService.removeMenu(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
