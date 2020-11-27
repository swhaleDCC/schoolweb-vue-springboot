package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Download;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.DownloadService;
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
public class DownloadController {

    private DownloadService downloadService;
    private MenuService menuService;
   
    @GetMapping("/public/download")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Download> page = downloadService.listDownloadsByTitleLike(title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get("download"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/download/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Download download = downloadService.getDownloadById(id);
            download.setHit( download.getHit()+1);
            downloadService.saveDownload(download);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param download
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/download")
    public ResponseEntity<Response> createDaogou(@RequestBody Download download) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            download.setUpdateUser(user);
            downloadService.saveDownload(download);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param download
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/download")
    public ResponseEntity<Response> updateDownload(@RequestBody Download download) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            download.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Download originalCollaboration = downloadService.getDownloadById(download.getId());
            download.setHit(originalCollaboration.getHit());
            downloadService.saveDownload(download);
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
    @DeleteMapping(value = "/download/{id}")
    public ResponseEntity<Response> deleteDownload(@PathVariable Long id) {
        try {
            downloadService.removeDownload(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
