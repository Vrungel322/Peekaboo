package com.peekaboo.controller.confirmation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by Oleksii on 21.10.2016.
 */
@RestController
@RequestMapping("/stable")
public class IsStable {
    @RequestMapping(path = "/")
    public void stable(HttpServletResponse response){
       try{ PrintWriter w=response.getWriter();


        w.write("OK");}
       catch(Throwable a){}
    }
}
