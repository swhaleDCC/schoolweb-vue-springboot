package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Building;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.BuildingService;
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
public class BuildingController {
    private BuildingService buildingService;
    private MenuService menuService;

    @GetMapping("/public/building/{type}")
    public ResponseEntity list(
            @PathVariable("type") String type,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
           ) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Building> page = buildingService.listBuildingsByTypeAndTitleLike(type,title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get(type));
        model.put("type", type);
        model.put("title", menuService.getMenus().get("building"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/building/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Building building = buildingService.getBuildingById(id);
            building.setHit( building.getHit()+1);
            buildingService.saveBuilding(building);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param building
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/building/{type}")
    public ResponseEntity<Response> createDaogou(@PathVariable String type,@RequestBody Building building) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            building.setUpdateUser(user);
            building.setType(type);
            buildingService.saveBuilding(building);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param building
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/building")
    public ResponseEntity<Response> updateBuilding(@RequestBody Building building) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            building.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Building originalBuilding = buildingService.getBuildingById(building.getId());
            building.setHit(originalBuilding.getHit());
            buildingService.saveBuilding(building);
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
    @DeleteMapping(value = "/building/{id}")
    public ResponseEntity<Response> deleteBuilding(@PathVariable Long id) {
        try {
            buildingService.removeBuilding(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }

}
