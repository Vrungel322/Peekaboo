package com.peekaboo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                //file name should be unique, so generating UUID for it's name
                String fileName = UUID.randomUUID().toString();

                //file path is "$rootDir/{userId}/fileName"
                File parent = new File(rootDir.getAbsolutePath() + File.separator + userId);
                if (!parent.exists()) parent.mkdir();

                File uploadedFile = new File(parent.getAbsolutePath() + File.separator + fileName);
                if (!uploadedFile.exists()) uploadedFile.createNewFile();

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                stream.write(bytes);
                stream.close();

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
