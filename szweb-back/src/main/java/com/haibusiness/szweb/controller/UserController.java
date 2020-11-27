package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.Authority;
import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.exception.BadRequestException;
import com.haibusiness.szweb.security.PBKDF2Encoder;
import com.haibusiness.szweb.security.SecurityContextHolder;
import com.haibusiness.szweb.service.AuthorityService;
import com.haibusiness.szweb.service.UserService;
import com.haibusiness.szweb.vo.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private PBKDF2Encoder pbkdf2Encoder;
    private AuthorityService authorityService;
    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userDetailsServicee;
    /**
     * 查询所用用户
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity list(
                               @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                               @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                               @RequestParam(value="name",required=false,defaultValue="") String name
                             ) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<User> page = userService.listUsersByNameLike(name, pageable);
        Map model=new HashMap<>();
        model.put("page", page);
        model.put("name","用户管理");
        model.put("url", "user");
        return ResponseEntity.ok().body(model);
    }


    /**
     * 新建/更新用户
     * @param user


     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> update(@RequestBody User user) {
        Long id=user.getId();
        if(id == null) {
            user.setPassword(pbkdf2Encoder.encode("e10adc3949ba59abbe56e057f20f883e")); // 加密密码
        }else{
            User oldUser=userService.getUserById(id);
            oldUser.setEmail(user.getEmail());
            oldUser.setEnabled(user.isEnabled());
            oldUser.setAuthorities((List<Authority>)user.getAuthorities());
            user=oldUser;
        }
        try {
            userService.saveUser(user);

        }  catch (Exception e)  {
            return ResponseEntity.ok().body(new Response(false, e.toString()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", user));
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id) {
        try {
            userService.removeUser(id);
        } catch (Exception e) {
            return  ResponseEntity.ok().body( new Response(false, e.getMessage()));
        }
        return  ResponseEntity.ok().body( new Response(true, "处理成功"));
    }
    /**
     * 验证密码
     * @param pass
     * @return
     */
    @GetMapping(value = "/validPass/{pass}")
    public ResponseEntity validPass(@PathVariable String pass){
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        User user = (User)userDetailsServicee.loadUserByUsername(userDetails.getUsername());
        Map map = new HashMap();
        map.put("status",200);
        if(!user.getPassword().equals(pbkdf2Encoder.encode(pass))){
            map.put("status",400);
        }
        return new ResponseEntity(map, HttpStatus.OK);
    }

    /**
     * 修改密码
     * @param pass
     * @return
     */
    @GetMapping(value = "/updatePass/{pass}")
    public ResponseEntity updatePass(@PathVariable String pass){
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        User user = (User)userDetailsServicee.loadUserByUsername(userDetails.getUsername());
        if(user.getPassword().equals(pbkdf2Encoder.encode(pass))){
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        user.setPassword(pbkdf2Encoder.encode(pass));
        userService.updateUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 修改邮箱
     * @param email

     * @return
     */
    @GetMapping(value = "/updateEmail/{email}")
    public ResponseEntity updateEmail(@PathVariable String email){
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        User user = (User)userDetailsServicee.loadUserByUsername(userDetails.getUsername());
        user.setEmail(email);
        userService.updateUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

}
