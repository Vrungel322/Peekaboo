package com.peekaboo.controller.filemanage;

import com.peekaboo.miscellaneous.JavaPropertiesParser;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.impl.StorageServiceImpl;
import com.peekaboo.model.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUpload {

    private static final Logger logger = LogManager.getLogger(FileUpload.class);
    @Autowired
    StorageServiceImpl storageService;
    @Autowired
    UserServiceImpl userService;
    private File rootDir;

    public FileUpload() {
        String rootPath = System.getProperty(JavaPropertiesParser.PARSER.getValue("FilesDestination"));
        rootDir = new File(rootPath + File.separator + "tmp");
        if (!rootDir.exists())
            rootDir.mkdirs();
    }

    @RequestMapping(path = "/{fileType}/{userId}", method = RequestMethod.POST)
    public String upload(@PathVariable String fileType, @PathVariable String userId, @RequestParam("file") MultipartFile file) {
        logger.error("got to upload");
        if (!file.isEmpty()) {
            try {
                logger.error("file is not empty");
                byte[] bytes = file.getBytes();
                String fileName = UUID.randomUUID().toString();
                File parent = new File(rootDir.getAbsolutePath() + File.separator + userId + File.separator + fileType);
                if (!parent.exists()) parent.mkdirs();
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
