package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Notice;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.MenuService;
import com.haibusiness.szweb.service.NoticeService;
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
public class NoticeController {
    private NoticeService noticeService;
    private MenuService menuService;
    @GetMapping("/public/notice")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Notice> page = noticeService.listNoticesByTitleLike(title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get("notice"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/notice/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Notice notice = noticeService.getNoticeById(id);
            notice.setHit( notice.getHit()+1);
            noticeService.saveNotice(notice);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param notice
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/notice")
    public ResponseEntity<Response> createDaogou(@RequestBody Notice notice) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            notice.setUpdateUser(user);
            noticeService.saveNotice(notice);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param notice
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/notice")
    public ResponseEntity<Response> updateNotice(@RequestBody Notice notice) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            notice.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Notice originalCollaboration = noticeService.getNoticeById(notice.getId());
            notice.setHit(originalCollaboration.getHit());
            noticeService.saveNotice(notice);
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
    @DeleteMapping(value = "/notice/{id}")
    public ResponseEntity<Response> deleteNotice(@PathVariable Long id) {
        try {
            noticeService.removeNotice(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
