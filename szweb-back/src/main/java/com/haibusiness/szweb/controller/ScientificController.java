package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Scientific;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.ScientificService;
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
public class ScientificController {
    private ScientificService scientificService;
    private MenuService menuService;

    @GetMapping("/public/scientific/{type}")
    public ResponseEntity list(
            @PathVariable("type") String type,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Scientific> page = scientificService.listScientificsByTypeAndTitleLike(type,title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get(type));
        model.put("type", type);
        model.put("title", menuService.getMenus().get("scientific"));
        return ResponseEntity.ok().body(model);
    }
    @PutMapping(value = "/public/scientific/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Scientific scientific = scientificService.getScientificById(id);
            scientific.setHit( scientific.getHit()+1);
            scientificService.saveScientific(scientific);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
    /**
     * 新建
     *
     * @param scientific
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/scientific/{type}")
    public ResponseEntity<Response> createDaogou(@PathVariable String type,@RequestBody Scientific scientific) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            scientific.setUpdateUser(user);
            scientific.setType(type);
            scientificService.saveScientific(scientific);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param scientific
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/scientific")
    public ResponseEntity<Response> updateScientific(@RequestBody Scientific scientific) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            scientific.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Scientific originalScientific = scientificService.getScientificById(scientific.getId());
            scientific.setHit(originalScientific.getHit());
            scientificService.saveScientific(scientific);
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
    @DeleteMapping(value = "/scientific/{id}")
    public ResponseEntity<Response> deleteScientific(@PathVariable Long id) {
        try {
            scientificService.removeScientific(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
