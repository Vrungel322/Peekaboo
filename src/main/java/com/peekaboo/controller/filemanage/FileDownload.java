package com.peekaboo.controller.filemanage;

import com.peekaboo.miscellaneous.JavaPropertiesParser;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/download")
public class FileDownload {

    @Autowired
    UserServiceImpl userService;
    private File rootDir;
    private Logger logger = LogManager.getLogger(this);

    public FileDownload() {
        String rootPath = System.getProperty(JavaPropertiesParser.PARSER.getValue("FilesDestination"));
        rootDir = new File(rootPath + File.separator + "tmp");
        if (!rootDir.exists())
            rootDir.mkdirs();
    }

    @RequestMapping(path = "/{fileType}/{fileName}", method = RequestMethod.GET)
    public void audio(HttpServletResponse response, @PathVariable String fileType, @PathVariable String fileName) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User receiver = userService.get(u.getId().toString());

        try {
            Storage storage = receiver.getUsesStorages().stream()
                    .filter(x -> x.getFileName().equals(fileName))
                    .findFirst().get();


            Path file = Paths.get(storage.getFilePath());
            logger.error("done");
            logger.error(Files.exists(file));

            if (Files.exists(file)) {
                switch (fileType) {
                    case "audio":
                        response.setContentType("audio/wav");
                        break;
                    case "image":
                        response.setContentType("image/jpeg");
                        break;
                    case "video":
                        // TODO: Set correct response type for video
                        response.setContentType("????");
                        break;
                    case "document":
                        // TODO: Set correct response type for document
                        response.setContentType("????");
                        break;
                    default:
                        logger.error("Enter not correct file type");
                }

                logger.error("file found");
                try {
                    Files.copy(file, response.getOutputStream());
                    logger.error("file copied");
                    response.getOutputStream().flush();
                } catch (IOException e) {
                    try {
                        response.getWriter().print("");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
