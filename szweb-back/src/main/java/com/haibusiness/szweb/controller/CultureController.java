package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Culture;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.MenuService;
import com.haibusiness.szweb.service.CultureService;
import com.haibusiness.szweb.vo.Response;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class CultureController {
    private CultureService cultureService;
    private MenuService menuService;
    @GetMapping("/public/culture")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Culture> page = cultureService.listCulturesByTitleLike(title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get("culture"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/culture/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Culture culture = cultureService.getCultureById(id);
            culture.setHit( culture.getHit()+1);
            cultureService.saveCulture(culture);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param culture
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/culture")
    public ResponseEntity<Response> createDaogou(@RequestBody Culture culture) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            culture.setUpdateUser(user);
            cultureService.saveCulture(culture);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param culture
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/culture")
    public ResponseEntity<Response> updateCulture(@RequestBody Culture culture) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            culture.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Culture originalCollaboration = cultureService.getCultureById(culture.getId());
            culture.setHit(originalCollaboration.getHit());
            cultureService.saveCulture(culture);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "更新失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }

    /**
     * 删除
     *
     * @param id
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @DeleteMapping(value = "/culture/{id}")
    public ResponseEntity<Response> deleteCulture(@PathVariable Long id) {
        try {
            cultureService.removeCulture(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}