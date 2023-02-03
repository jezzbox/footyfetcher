package com.jezzbox.footyfetcher.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class FixturesController {

    @RequestMapping("/fixtures")
    @ResponseBody
    public String getFixtures(
            @RequestParam("live") Optional<String> live) {
       return "fixtures with live =" + live;
    }
}
