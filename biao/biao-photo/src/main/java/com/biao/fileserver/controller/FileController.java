package com.biao.fileserver.controller;

import com.biao.fileserver.config.AliYunOOSClientConfig;
import com.biao.fileserver.domain.Card;
import com.biao.fileserver.domain.File;
import com.biao.fileserver.service.CardService;
import com.biao.fileserver.service.FileService;
import com.biao.fileserver.util.AliyunOOSUtils;
import com.biao.fileserver.util.MD5Util;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600) // 允许所有域名访问
@Controller
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private CardService cardService;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;
    @Autowired
    private AliYunOOSClientConfig clientConfig;

    @RequestMapping(value = "/")
    public String index(Model model) {
        // 展示最新二十条数据
        model.addAttribute("files", fileService.listFilesByPage(0, 20));
        return "index";
    }

    /**
     * 分页查询文件
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("files/{pageIndex}/{pageSize}")
    @ResponseBody
    public List<File> listFilesByPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
        return fileService.listFilesByPage(pageIndex, pageSize);
    }

    /**
     * 获取文件片信息
     *
     * @param id
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("files/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFile(@PathVariable String id) throws UnsupportedEncodingException {

        Optional<File> file = fileService.getFileById(id);

        if (file.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; fileName=" + new String(file.get().getName().getBytes("utf-8"), "ISO-8859-1"))
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "").header("Connection", "close")
                    .body(file.get().getContent().getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }

    }

    /**
     * 上传
     *
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        try {
            File f = new File(file.getOriginalFilename(), file.getContentType(), file.getSize(),
                    new Binary(file.getBytes()));
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            fileService.saveFile(f);
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Your " + file.getOriginalFilename() + " is wrong!");
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    /**
     * 测试图片服务ok 整合webuploader
     *
     * @return
     */
    @GetMapping("/form")
    public String form() {
        return "form";
    }


    /**
     * 在线显示文件
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unlikely-arg-type")
    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFileOnline(@PathVariable String id) {
        Map<String, Object> map = AliyunOOSUtils.downloadImageObject(clientConfig, id);
        if (map != null) {
            try {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + map.get("simpleImageName") + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, map.get("contentType") + "")
                        .header(HttpHeaders.CONTENT_LENGTH, map.get("contentLength") + "")
                        .header("Connection", "close").body(map.get((byte[]) map.get("imageData")));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }

    }

    /**
     * 上传接口
     *
     * @param
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String contentType = "png";
            int index = Objects.requireNonNull(fileName).lastIndexOf(".");
            if (index != -1) {
                contentType = fileName.substring(index + 1, fileName.length());
            }
            String imageName = UUID.randomUUID().toString().replaceAll("-", "") + "." + contentType;
            String idcardBucketName = AliyunOOSUtils.getBucketName("image", imageName);
            Map<String, String> userMetadata = new HashMap<>();
            userMetadata.put("simpleImageName", imageName);
            AliyunOOSUtils.uploadToAliyun(clientConfig, inputStream, idcardBucketName, AliyunOOSUtils.createObjectMetadata("application/" + contentType, file.getSize(), "utf-8", userMetadata));
            return ResponseEntity.status(HttpStatus.OK).body(idcardBucketName + "?x-oss-process=style/uesstyle");

        } catch (IOException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }

    /**
     * 身份证上传接口
     *
     * @param
     * @return
     */
    @PostMapping("/card/upload")
    @ResponseBody
    public ResponseEntity<String> handleCardFileUpload(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String contentType = "png";
            int index = Objects.requireNonNull(fileName).lastIndexOf(".");
            if (index != -1) {
                contentType = fileName.substring(index + 1, fileName.length());
            }
            String imageName = UUID.randomUUID().toString().replaceAll("-", "") + "." + contentType;
            String idcardBucketName = AliyunOOSUtils.getIdcardBucketName(imageName);
            Map<String, String> userMetadata = new HashMap<>();
            userMetadata.put("simpleImageName", imageName);
            AliyunOOSUtils.uploadToAliyun(clientConfig, inputStream, idcardBucketName, AliyunOOSUtils.createObjectMetadata("application/" + contentType, file.getSize(), "utf-8", userMetadata));
            return ResponseEntity.status(HttpStatus.OK).body(idcardBucketName + "?x-oss-process=style/uesstyle");
        } catch (IOException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 在线显示身份证文件
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unlikely-arg-type")
    @GetMapping("/card/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveCardOnline(@PathVariable String id) {
        int suffix = id.indexOf(".");
        if (suffix == -1) {
            Optional<Card> file = cardService.getCardById(id);

            if (file.isPresent()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + file.get().getName() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, file.get().getContentType())
                        .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "").header("Connection", "close")
                        .body(file.get().getContent().getData());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
            }
        }
        Map<String, Object> map = AliyunOOSUtils.downloadImageObject(clientConfig, id);
        if (map != null) {
            try {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + map.get("simpleImageName") + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, map.get("contentType") + "")
                        .header(HttpHeaders.CONTENT_LENGTH, map.get("contentLength") + "")
                        .header("Connection", "close").body(map.get((byte[]) map.get("imageData")));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }
    }

    /**
     * 在线显示图片
     *
     * @param id id
     * @return
     */
    @SuppressWarnings("unlikely-arg-type")
    @GetMapping("/image/view/{imageName}")
    @ResponseBody
    public ResponseEntity<Object> selectImageOnline(@PathVariable String imageName) {
        Map<String, Object> map = AliyunOOSUtils.downloadImageObject(clientConfig, imageName);
        if (map != null) {
            try {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + map.get("simpleImageName") + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, map.get("contentType") + "")
                        .header(HttpHeaders.CONTENT_LENGTH, map.get("contentLength") + "")
                        .header("Connection", "close").body(map.get((byte[]) map.get("imageData")));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }
    }

}
