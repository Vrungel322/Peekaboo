package com.peekaboo.controller;

import com.peekaboo.miscellaneous.JavaPropertiesParser;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.enums.FileType;
import com.peekaboo.model.service.impl.StorageServiceImpl;
import com.peekaboo.model.service.impl.UserServiceImpl;
import net.coobird.thumbnailator.Thumbnailator;
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

@RestController
public class AvatarController {

    private static final Logger logger = LogManager.getLogger(AvatarController.class);
    private File rootDir;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private StorageServiceImpl storageService;

    public AvatarController() {
        String rootPath = System.getProperty(JavaPropertiesParser.PARSER.getValue("FilesDestination"));
        rootDir = new File(rootPath + File.separator + "tmp");
        if (!rootDir.exists())
            rootDir.mkdirs();
    }

    @RequestMapping(path = "/avatar", method = RequestMethod.POST)
    public String avatar(@RequestParam("image") MultipartFile image) {
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

                File thumbnails = new File(parent.getAbsolutePath() + File.separator + "thumbnails");
                if (!thumbnails.exists()) thumbnails.mkdir();
                File size1 = new File(thumbnails.getAbsolutePath() + File.separator + "size1");
                File size2 = new File(thumbnails.getAbsolutePath() + File.separator + "size2");

                Thumbnailator.createThumbnail(uploadedImage, size1, 400, 250);

                Thumbnailator.createThumbnail(uploadedImage, size2, 300, 188);

                Storage profilePhoto = new Storage(imageName, uploadedImage.getAbsolutePath(), FileType.IMAGE.name());
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

    // TODO: 19.11.2016 Do parameter size unneccesary
    @RequestMapping(path = "/avatar/{userId}/{size}", method = RequestMethod.GET)
    public void avatar(HttpServletResponse response, @PathVariable String userId, @PathVariable Integer size) {
        User user = userService.get(userId);
        logger.error(user.toString());
        response.setContentType("image/jpeg");
        Storage avatar = user.getProfilePhoto();

        if (avatar == null) {
            try {
                response.getWriter().print("{\"Fuck you\"}");
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String avatarpath = avatar.getFilePath();
            String thumbnails = rootDir.getAbsolutePath() + File.separator + user.getId().toString()
                    + File.separator + "thumbnails";

            Path image;
            switch (size) {
                case 0:
                    image = Paths.get(avatarpath);
                    break;
                case 1:
                    image = Paths.get(thumbnails + File.separator + "size1.jpeg");
                    break;
                case 2:
                    image = Paths.get(thumbnails + File.separator + "size2.jpeg");
                    break;
                default:
                    image = Paths.get(avatarpath);
                    break;
            }

            logger.error("image found");
            try {
                OutputStream stream = response.getOutputStream();
                Files.copy(image, stream);
                logger.error("image copied to output stream");
                stream.flush();
                stream.close();

            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
}
