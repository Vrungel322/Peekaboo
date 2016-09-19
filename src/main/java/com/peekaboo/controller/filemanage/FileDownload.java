package com.peekaboo.controller.filemanage;

import com.peekaboo.miscellaneous.PropertiesParser;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/download")
public class FileDownload {

    private File rootDir;

    @Autowired
    UserServiceImpl userService;

    public FileDownload() {
        String rootPath = System.getProperty("catalina.home");
        rootDir = new File(rootPath + File.separator + "tmp");
        if (!rootDir.exists())
            rootDir.mkdir();
    }

    @RequestMapping(path = "/audio/{fileName}", method = RequestMethod.GET)
    public void audio(HttpServletResponse response, @PathVariable String fileName) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = u.getId().toString();
        User receiver = userService.get(userId);
//        Storage storage = receiver.getUsesStorages().stream()
//                .filter(x -> x.getFileName().equals(fileName))
//                .findFirst().get();
        String rootPath = System.getProperty(PropertiesParser.getValue("FilesDestination"));
        //!!!!!!!!!!!!!!!!!!!!!!!!!!FUCKING NOTRIGHT
       File rootDir = new File(rootPath + File.separator + "tmp"+ File.separator + userId);
        if (!rootDir.exists()) rootDir.mkdirs();
        File uploadedFile = new File(rootDir.getAbsolutePath() + File.separator+fileName );
        logger.error(uploadedFile.getAbsolutePath());
        Path file = Paths.get(uploadedFile.getAbsolutePath());
        logger.error("done");
        logger.error(Files.exists(file));
        if (Files.exists(file)) {
            response.setContentType("audio/wav");
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
    }
    private Logger logger = LogManager.getLogger(this);
}
