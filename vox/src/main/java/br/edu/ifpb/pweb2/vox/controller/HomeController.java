package br.edu.ifpb.pweb2.vox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
<<<<<<< HEAD
  @RequestMapping({ "/home", "/" })
  public String showHomePage() {
    return "home/index";
  }
}
=======

    @RequestMapping("/home")
    public String showHomePage() {
        return "home/index";
    }
}

>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
