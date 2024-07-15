package uk.ac.cardiff.phoenix.ilm.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MyErrorController implements ErrorController {

    @GetMapping("/notauthorised")
    public String accessDenied() {
        return "notauthorised";
    }

    @RequestMapping("/error")
    public String handleError() {
        // Provide the name of your custom error page
        return "error";
    }



}


