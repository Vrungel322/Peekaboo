package com.peekaboo.controller;

import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.impl.StorageServiceImpl;
import com.peekaboo.model.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    public FileUpload() {
        //all temporary files are going to be stored in "$CATALINA_HOME/tmp"
        String rootPath = System.getProperty("catalina.home");
        rootDir = new File(rootPath + File.separator + "tmp");
        if (!rootDir.exists())
            rootDir.mkdirs();
    }
    @RequestMapping(path = "/audio/{userId}", method = RequestMethod.POST)
    public String audio(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        logger.error("1");
        StorageServiceImpl storageService = new StorageServiceImpl();
        logger.error("2");
        UserServiceImpl userService = new UserServiceImpl();
        logger.error("3");
        if (!file.isEmpty()) {
            try {
                logger.error("sdfg");
                byte[] bytes = file.getBytes();

                //file name should be unique, so generating UUID for it's name
                String fileName = UUID.randomUUID().toString();

                //file path is "$rootDir/{userId}/fileName"
                logger.error("4");
                File parent = new File(rootDir.getAbsolutePath() + File.separator + userId);

                if (!parent.exists()) parent.mkdir();

                logger.error("5");
                File uploadedFile = new File(parent.getAbsolutePath() + File.separator + fileName);
                if (!uploadedFile.exists()) uploadedFile.createNewFile();

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));

                logger.error("6");
                stream.write(bytes);
                stream.close();

                StringBuilder fileBaseName = new StringBuilder("");
                fileBaseName.append(userId).append(fileName);
                Storage storage = new Storage(fileName.toString(), uploadedFile.getAbsolutePath());

                storageService.save(storage);
                User user = userService.get(userId);
                user.getStorages().add(user.getStorage());
                user.setStorage(storage);
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
