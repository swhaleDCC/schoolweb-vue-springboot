package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Enrollment;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.MenuService;
import com.haibusiness.szweb.service.EnrollmentService;
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
public class EnrollmentController {
    private EnrollmentService enrollmentService;
    private MenuService menuService;
    @GetMapping("/public/enrollment")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Enrollment> page = enrollmentService.listEnrollmentsByTitleLike(title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get("enrollment"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/enrollment/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Enrollment enrollment = enrollmentService.getEnrollmentById(id);
            enrollment.setHit( enrollment.getHit()+1);
            enrollmentService.saveEnrollment(enrollment);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param enrollment
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/enrollment")
    public ResponseEntity<Response> createDaogou(@RequestBody Enrollment enrollment) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            enrollment.setUpdateUser(user);
            enrollmentService.saveEnrollment(enrollment);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param enrollment
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/enrollment")
    public ResponseEntity<Response> updateEnrollment(@RequestBody Enrollment enrollment) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            enrollment.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Enrollment originalCollaboration = enrollmentService.getEnrollmentById(enrollment.getId());
            enrollment.setHit(originalCollaboration.getHit());
            enrollmentService.saveEnrollment(enrollment);
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
    @DeleteMapping(value = "/enrollment/{id}")
    public ResponseEntity<Response> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.removeEnrollment(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}