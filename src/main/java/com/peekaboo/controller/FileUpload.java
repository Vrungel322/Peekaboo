package com.peekaboo.controller;

import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.repository.impl.StorageRepositoryImpl;
import com.peekaboo.model.service.impl.StorageServiceImpl;
import com.peekaboo.model.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import scala.math.Ordering;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUpload {

    private static final Logger logger = LogManager.getLogger(FileUpload.class);
    private File rootDir;

    @Autowired
    StorageServiceImpl storageService;

    @Autowired
    UserServiceImpl userService;

    public FileUpload() {
        String rootPath = System.getProperty("catalina.home");
        rootDir = new File(rootPath + File.separator + "tmp");
        if (!rootDir.exists())
            rootDir.mkdirs();
    }

    @RequestMapping(path = "/audio/{userId}", method = RequestMethod.POST)
    public String audio(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String fileName = UUID.randomUUID().toString();
                File parent = new File(rootDir.getAbsolutePath() + File.separator + userId);
                if (!parent.exists()) parent.mkdir();
                File uploadedFile = new File(parent.getAbsolutePath() + File.separator + fileName);
                if (!uploadedFile.exists()) uploadedFile.createNewFile();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                stream.write(bytes);
                stream.close();
                StringBuilder fileBaseName = new StringBuilder("");
                fileBaseName.append(userId).append(fileName);
                Storage storage = new Storage(fileName.toString(), uploadedFile.getAbsolutePath());
                storageService.save(storage);
                User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = userService.get(u.getId().toString());
                User receiver = userService.get(userId);
                user.getOwnStorages().add(storage);
                receiver.getUsesStorages().add(storage);
                userService.update(receiver);
                userService.update(user);
                logger.debug("Server File Location=" + uploadedFile.getAbsolutePath());
                //for now just return upload status
                return "{\"result\": \"Ok\", \"name\": \"" + fileName + "\"}";
            } catch (Exception e) {
                return "{\"result\": \"Fail\"}";
            }
        } else {
            return "{\"result\": \"Fail\"}";
        }
    }
}
