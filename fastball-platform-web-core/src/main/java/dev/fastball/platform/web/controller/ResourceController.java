package dev.fastball.platform.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ResourceController {
    @GetMapping("/web/")
    public String index() {
        return "/web/index.html";
    }

    @GetMapping("/assets-web/{path}")
    public String assets(@PathVariable String path) {
        return "/assets-web/" + path;
    }
}
