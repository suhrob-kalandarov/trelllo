package org.exp.trello.controllers.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "home/home";
    }


}
