package com.peekaboo.controller;

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

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.jsonwebtoken.lang.Classes.getResourceAsStream;

@RestController
public class AvatarController {

    private static final Logger logger = LogManager.getLogger(AvatarController.class);
    private File rootDir;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private StorageServiceImpl storageService;

    public AvatarController(){

    }
    @RequestMapping(path = "/avatar", method = RequestMethod.POST)
    public String avatar( @RequestParam("image") MultipartFile image){
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                User tmp = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = userService.findById(tmp.getId());
                String imageName = user.getUsername();
                File parent = new File(rootDir.getAbsolutePath() + File.separator + user.getId().toString());
                if (!parent.exists()) parent.mkdir();
                File uploadedImage = new File(parent.getAbsolutePath() + File.separator + imageName);
                if (!uploadedImage.exists()) uploadedImage.createNewFile();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedImage));
                stream.write(bytes);
                stream.close();
                Storage profilePhoto = new Storage(imageName, uploadedImage.getAbsolutePath());
                storageService.save(profilePhoto);
                userService.changeProfilePhoto(user, profilePhoto);
                logger.debug("Server Avatar location=" + uploadedImage.getAbsolutePath());
                //for now just return upload status
                return "{\"result\": \"Ok\", \"name\": \"" + imageName + "\"}";
            } catch (Exception e) {
                return "{\"result\": \"Fail\"}";
            }
        } else {
            return "{\"result\": \"Fail\"}";
        }
    }

    @RequestMapping(path = "/avatar/{userId}", method = RequestMethod.GET)
    public void avatar(HttpServletResponse response, @PathVariable String userId) {
        User user = userService.get(userId);
        logger.error(user.toString());
        response.setContentType("image/jpeg");
        Storage avatar = user.getProfilePhoto();



        if (avatar == null) {
            try {

                logger.error("got to null");
                byte[] buffer = new byte[1024];
                int bytesread;


                InputStream input = AvatarController.class.getResourceAsStream("/defaultfiles/default_profile_photo.jpg");
                OutputStream output = response.getOutputStream();
                while ((bytesread = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesread);
                }
                response.getOutputStream().flush();
            } catch (Exception e) {
               logger.error(e.toString());
            }
        } else {
            String avatarpath = avatar.getFilePath();
            Path image = Paths.get(avatarpath);

            //TODO: Set correct content type
            logger.error("image found");
            try {

                Files.copy(image, response.getOutputStream());
                logger.error("image copied to output stream");
                response.getOutputStream().flush();
            } catch (IOException e) {
                try {
                    response.getWriter().print("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


}
