package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Cooperation;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.MenuService;
import com.haibusiness.szweb.service.CooperationService;
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
public class CooperationController {
    private CooperationService cooperationService;
    private MenuService menuService;
    @GetMapping("/public/cooperation")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Cooperation> page = cooperationService.listCooperationsByTitleLike(title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get("cooperation"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/cooperation/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Cooperation cooperation = cooperationService.getCooperationById(id);
            cooperation.setHit( cooperation.getHit()+1);
            cooperationService.saveCooperation(cooperation);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param cooperation
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/cooperation")
    public ResponseEntity<Response> createDaogou(@RequestBody Cooperation cooperation) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            cooperation.setUpdateUser(user);
            cooperationService.saveCooperation(cooperation);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param cooperation
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/cooperation")
    public ResponseEntity<Response> updateCooperation(@RequestBody Cooperation cooperation) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            cooperation.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Cooperation originalCollaboration = cooperationService.getCooperationById(cooperation.getId());
            cooperation.setHit(originalCollaboration.getHit());
            cooperationService.saveCooperation(cooperation);
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
    @DeleteMapping(value = "/cooperation/{id}")
    public ResponseEntity<Response> deleteCooperation(@PathVariable Long id) {
        try {
            cooperationService.removeCooperation(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
