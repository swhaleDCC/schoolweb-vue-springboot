package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Dynamic;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.DynamicService;
import com.haibusiness.szweb.service.MenuService;
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
public class DynamicController {

    private DynamicService dynamicService;
    private MenuService menuService;

    @GetMapping("/public/dynamic")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Dynamic> page = dynamicService.listDynamicsByTitleLike(title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get("dynamic"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/dynamic/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Dynamic dynamic = dynamicService.getDynamicById(id);
            dynamic.setHit( dynamic.getHit()+1);
            dynamicService.saveDynamic(dynamic);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param dynamic
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/dynamic")
    public ResponseEntity<Response> createDaogou(@RequestBody Dynamic dynamic) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            dynamic.setUpdateUser(user);
            dynamicService.saveDynamic(dynamic);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param dynamic
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/dynamic")
    public ResponseEntity<Response> updateDynamic(@RequestBody Dynamic dynamic) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            dynamic.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Dynamic originalCollaboration = dynamicService.getDynamicById(dynamic.getId());
            dynamic.setHit(originalCollaboration.getHit());
            dynamicService.saveDynamic(dynamic);
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
    @DeleteMapping(value = "/dynamic/{id}")
    public ResponseEntity<Response> deleteDynamic(@PathVariable Long id) {
        try {
            dynamicService.removeDynamic(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }

}
