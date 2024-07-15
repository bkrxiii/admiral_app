package uk.ac.cardiff.phoenix.ilm.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/test")
@Profile({"dev"})
@Controller
public class TestSecurityController {

// Just return simple text "test succeed" to show security worked for role
    @GetMapping("/tutor")
    @ResponseBody
    public String testTutor() {
        return "test succeed";
    }

}
