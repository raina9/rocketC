package com.weblearnex.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MainController {
    ObjectMapper mapper = new ObjectMapper();


    @GetMapping("/entry")
    public String applicationRunning(HttpServletRequest request) {

        return "Application up and running";
    }

}
