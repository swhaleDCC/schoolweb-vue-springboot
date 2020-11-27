package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Story;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.MenuService;
import com.haibusiness.szweb.service.StoryService;
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
public class StoryController {
    private StoryService storyService;
    private MenuService menuService;
    @GetMapping("/public/story")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Story> page = storyService.listStorysByTitleLike(title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get("story"));
        return ResponseEntity.ok().body(model);
    }
    @PutMapping(value = "/public/story/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Story story = storyService.getStoryById(id);
            story.setHit( story.getHit()+1);
            storyService.saveStory(story);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
    /**
     * 新建
     * @param story
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/story")
    public ResponseEntity<Response> createDaogou(@RequestBody Story story) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            story.setUpdateUser(user);
            storyService.saveStory(story);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }
    /**
     * 更新
     * @param story
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/story")
    public ResponseEntity<Response> updateStory(@RequestBody Story story) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            story.setUpdateUser(user);
            Story originalCollaboration = storyService.getStoryById(story.getId());
            story.setHit(originalCollaboration.getHit());
            storyService.saveStory(story);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "更新失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
    /**
     * 删除
     * @param id
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @DeleteMapping(value = "/story/{id}")
    public ResponseEntity<Response> deleteStory(@PathVariable Long id) {
        try {
            storyService.removeStory(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
