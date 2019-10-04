package com.bramgussekloo.projects.controller;

import com.bramgussekloo.projects.DataClasses.Address;
import com.bramgussekloo.projects.Database.Statements;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/world")
    public String world() {
        return "Hello World";
    }

    @GetMapping("/address")
    public Address address() {
        return Statements.getAllAddresses().get(0);
    }
}
