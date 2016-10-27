package com.peekaboo.controller.filemanage;

import com.peekaboo.miscellaneous.JavaPropertiesParser;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.enums.FileType;
import com.peekaboo.model.service.StorageService;
import com.peekaboo.model.service.UserService;
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
    UserService userService;
    @Autowired
    StorageService storageService;

    private File rootDir;
    private Logger logger = LogManager.getLogger(this);

    public FileDownload() {
        String rootPath = System.getProperty(JavaPropertiesParser.PARSER.getValue("FilesDestination"));
        rootDir = new File(rootPath + File.separator + "tmp");
        if (!rootDir.exists())
            rootDir.mkdirs();
    }

    @RequestMapping(path = "/{fileType}/{fileName}", method = RequestMethod.GET)
    public void download(HttpServletResponse response, @PathVariable String fileType, @PathVariable String fileName) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User receiver = userService.get(u.getId().toString());

        Storage storage = storageService.findByFileName(fileName);
        try {
            if (storage == null) throw new Exception();
            if (!receiver.getOwnStorages().contains(storage) &&
                    !receiver.getUsesStorages().contains(storage)) throw new Exception();

            Path file = Paths.get(storage.getFilePath());
            logger.error("done");
            logger.error(Files.exists(file));

            if (!fileType.equals(storage.getFileType())) logger.error("Wrong file type in the path");

            if (Files.exists(file)) {
                switch (storage.getFileType()) {
                    case "audio":
                        response.setContentType(FileType.AUDIO.type());
                        break;
                    case "image":
                        response.setContentType(FileType.IMAGE.type());
                        break;
                    case "video":
                        // TODO: Set correct response type for video
//                        response.setContentType("????");
                        logger.error("Video not supported yet");
                        break;
                    case "document":
                        // TODO: Set correct response type for document
//                        response.setContentType("????");
                        logger.error("Document not supported yet");
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
