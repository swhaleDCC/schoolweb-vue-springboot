package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.security.SecurityContextHolder;
import com.haibusiness.szweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class FileUploadController {
    // 图片的存储路径：

    @Value("${upload-file-server}")
    private String serverUrl;
    public static final String ROOT = "upload-dir";//上传文件的根目录--相对地址
    private final ResourceLoader resourceLoader;
    private static final HashMap<String, String> FILE_PATH_MAP = getFilePathMap();
    private static final HashMap<String, String> FILE_EXT_MAP = getFileExtMap();
    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userDetailsServicee;
    @Autowired
    private UserService userService;
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @RequestMapping("uploadFile")  //默认get请求   HttpServletRequest类型的对象是前端传到后端的请求数据   dir上传文件的类型
    public UploadReturnJson uploadFile(@RequestParam(value = "dir", required = false, defaultValue = "image")String dir, HttpServletRequest request) {
        UploadReturnJson errorResult = filterRequest(dir, request);  //先对文件数据做验证
        if (errorResult != null) {    //==null证明数据正确
            return errorResult;
        }

        Map<String, MultipartFile> multipartFileMap = ((MultipartHttpServletRequest) request).getFileMap();
        MultipartFile file = getFirstOrNull(multipartFileMap);
        String fileName = UUID.randomUUID().toString() + getFileExtension(file);
        String fileDir = FILE_PATH_MAP.get(dir);
        String filePath = Paths.get(ROOT, fileDir).toString();
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream = new BufferedOutputStream(getFileOutputStream(filePath, fileName));
            stream.write(bytes);
            stream.close();
            String fileUrl = serverUrl+"/upload/file/" + dir + "/" + fileName;
            //把上传成功的图像的url保存到user表里
            if(dir.equals("avatar")){
                UserDetails userDetails = SecurityContextHolder.getUserDetails();
                User user = (User)userDetailsServicee.loadUserByUsername(userDetails.getUsername());
                user.setAvatar(fileUrl);
                userService.updateUser(user);
            }
            return new UploadReturnJson(fileUrl);

        } catch (Exception e) {
            return new UploadReturnJson(1, e.getMessage());

        }

    }

    /**
     * 图片读取
     */
    @RequestMapping(method = RequestMethod.GET, value = "/upload/file/{dir}/{filename:.+}")
    public ResponseEntity<?> readFile(@PathVariable String dir, @PathVariable String filename) {


        String fileDir = FILE_PATH_MAP.get(dir);
        if (fileDir == null) {
            return new ResponseEntity<String>("目录不存在.", HttpStatus.NOT_FOUND);
        }
        if (filename == null) {
            return new ResponseEntity<String>("文件名不能为空.", HttpStatus.NOT_FOUND);
        }
        String filePath = Paths.get(ROOT, fileDir, filename).toString();
        try {
            Resource resource = resourceLoader.getResource("file:" + filePath);
            if (!resource.exists()) {
                return new ResponseEntity<String>("File Not found.", HttpStatus.NOT_FOUND);
            }
            //如果是文件则是下载
            if (dir.equals("file")) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(resource);
            }
            return ResponseEntity.ok(resourceLoader.getResource("file:" + filePath));
        } catch (Exception e) {
            return new ResponseEntity<String>("Exception Not found.", HttpStatus.NOT_FOUND);
        }
    }

    //过滤请求   私有  验证数据
    private UploadReturnJson filterRequest(String dir, HttpServletRequest request) {
        //
        if (!FILE_EXT_MAP.containsKey(dir)) {
            return new UploadReturnJson(1, "目录不存在");
        }
        //
        Boolean isMultipartFile = request instanceof StandardMultipartHttpServletRequest;
        if (!isMultipartFile) {
            //过滤非MultipartFile的请求
            return new UploadReturnJson(1, "request is not StandardMultipartHttpServletRequest object");
        }
        //
        Map<String, MultipartFile> multipartFileMap = ((MultipartHttpServletRequest) request).getFileMap();
        MultipartFile file = getFirstOrNull(multipartFileMap);
        if (file == null) {
            return new UploadReturnJson(1, "当前文件为空，请选择文件");
        }
        //
        String fileExt = getFileExtension(file).replace(".", "");
        if (!Arrays.asList(FILE_EXT_MAP.get(dir).split(",")).contains(fileExt)) {
            String msg = "上传文件扩展名" + fileExt + "是不允许的扩展名,目前只允许" + FILE_EXT_MAP.get(dir) + "格式";
            return new UploadReturnJson(1, msg);
        }
        //
        return null;
    }

    private FileOutputStream getFileOutputStream(String filePath, String fileName) throws FileNotFoundException {
        String fileFullPath = Paths.get(filePath, fileName).toString();
        try {
            return new FileOutputStream(new File(fileFullPath));
        } catch (FileNotFoundException ex) {
            File parent = new File(filePath);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            return new FileOutputStream(new File(fileFullPath));
        }
    }

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf("."), fileName.length()) : "";
        fileExtension = fileExtension.toLowerCase();
        return fileExtension;
    }

    private static HashMap<String, String> getFileExtMap() {
        // 定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<String, String>();
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("avatar", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,pptx,zip,rar,pdf");
        return extMap;
    }

    private static HashMap<String, String> getFilePathMap() {
        // 定义允许上传的文件保存路径
        HashMap<String, String> pathMap = new HashMap<String, String>();
        pathMap.put("image", "image"+File.separator+"p0");
        pathMap.put("avatar", "avatar"+File.separator+"p0");
        pathMap.put("file", "file"+File.separator+"p0");
        return pathMap;
    }

    private static <K, V> V getFirstOrNull(Map<K, V> map) {
        V obj = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }

    //上传返回结果
    public class UploadReturnJson {
        public UploadReturnJson() {

        }

        //上传成功
        public UploadReturnJson(String url) {
            setError(0);
            setUrl(url);
        }

        //上传失败
        public UploadReturnJson(int error, String message) {
            if (error < 1) {
                try {
                    throw new Exception("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            setError(error);
            setMessage("[上传失败]" + message);
        }

        private int error;
        private String message;
        private String url;

        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
