package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Community;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.MenuService;
import com.haibusiness.szweb.service.CommunityService;
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
public class CommunityController {
    private CommunityService communityService;
    private MenuService menuService;

    @GetMapping("/public/community/{type}")
    public ResponseEntity list(
            @PathVariable("type") String type,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Community> page = communityService.listCommunitysByTypeAndTitleLike(type,title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get(type));
        model.put("type", type);
        model.put("title", menuService.getMenus().get("community"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/community/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Community community = communityService.getCommunityById(id);
            community.setHit( community.getHit()+1);
            communityService.saveCommunity(community);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param community
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/community/{type}")
    public ResponseEntity<Response> createDaogou(@PathVariable String type,@RequestBody Community community) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            community.setUpdateUser(user);
            community.setType(type);
            communityService.saveCommunity(community);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param community
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/community")
    public ResponseEntity<Response> updateCommunity(@RequestBody Community community) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            community.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Community originalCommunity = communityService.getCommunityById(community.getId());
            community.setHit(originalCommunity.getHit());
            communityService.saveCommunity(community);
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
    @DeleteMapping(value = "/community/{id}")
    public ResponseEntity<Response> deleteCommunity(@PathVariable Long id) {
        try {
            communityService.removeCommunity(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }

}
