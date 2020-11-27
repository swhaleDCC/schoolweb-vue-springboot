package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Graduate;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.GraduateService;
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
public class GraduateController {
    private GraduateService graduateService;
    private MenuService menuService;
    @GetMapping("/public/graduate")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Graduate> page = graduateService.listGraduatesByTitleLike(title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get("graduate"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/graduate/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Graduate graduate = graduateService.getGraduateById(id);
            graduate.setHit( graduate.getHit()+1);
            graduateService.saveGraduate(graduate);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param graduate
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/graduate")
    public ResponseEntity<Response> createDaogou(@RequestBody Graduate graduate) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            graduate.setUpdateUser(user);
            graduateService.saveGraduate(graduate);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param graduate
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/graduate")
    public ResponseEntity<Response> updateGraduate(@RequestBody Graduate graduate) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            graduate.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Graduate originalCollaboration = graduateService.getGraduateById(graduate.getId());
            graduate.setHit(originalCollaboration.getHit());
            graduateService.saveGraduate(graduate);
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
    @DeleteMapping(value = "/graduate/{id}")
    public ResponseEntity<Response> deleteGraduate(@PathVariable Long id) {
        try {
            graduateService.removeGraduate(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
