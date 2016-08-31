package com.peekaboo.controller;

import com.peekaboo.model.entity.User;
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

@RestController
@RequestMapping("/download")
public class FileDownload {

    private File rootDir;

    public FileDownload() {
        String rootPath = System.getProperty("catalina.home");
        rootDir = new File(rootPath + File.separator + "tmp");
        if (!rootDir.exists())
            rootDir.mkdirs();
    }

    @RequestMapping(path = "/audio/{fileName}", method = RequestMethod.GET)
    public void audio(HttpServletResponse response, @PathVariable String fileName) {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = u.getId().toString();
        Path file = Paths.get(rootDir.getAbsolutePath(), userId, fileName);
        if (Files.exists(file)) {
            response.setContentType("audio/wav");
            try {
                Files.copy(file, response.getOutputStream());
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
