package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Party;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.service.MenuService;
import com.haibusiness.szweb.service.PartyService;
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
public class PartyController {
    private PartyService partyService;
    private MenuService menuService;
    @GetMapping("/public/party")
    public ResponseEntity list(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "title", required = false, defaultValue = "") String title
    ) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Party> page = partyService.listPartysByTitleLike(title,pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name", menuService.getMenus().get("party"));
        return ResponseEntity.ok().body(model);
    }


    @PutMapping(value = "/public/party/{id}")
    public ResponseEntity addHit(@PathVariable Long id) {
        try {
            Party party = partyService.getPartyById(id);
            party.setHit( party.getHit()+1);
            partyService.saveParty(party);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "修改浏览次数失败！"));
        }
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    /**
     * 新建
     *
     * @param party
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping(value = "/party")
    public ResponseEntity<Response> createDaogou(@RequestBody Party party) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            party.setUpdateUser(user);
            partyService.saveParty(party);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 更新
     *
     * @param party
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PutMapping(value = "/party")
    public ResponseEntity<Response> updateParty(@RequestBody Party party) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            party.setUpdateUser(user);
            //更新,对于表单中没有出现的字段要把原来的值付给它们
            Party originalCollaboration = partyService.getPartyById(party.getId());
            party.setHit(originalCollaboration.getHit());
            partyService.saveParty(party);
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
    @DeleteMapping(value = "/party/{id}")
    public ResponseEntity<Response> deleteNotice(@PathVariable Long id) {
        try {
            partyService.removeParty(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "删除失败"));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
