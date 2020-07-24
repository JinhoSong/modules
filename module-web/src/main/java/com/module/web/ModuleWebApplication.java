package com.module.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@Profile(value = "dev")
@RestController
@SpringBootApplication
public class ModuleWebApplication {
    public static void main(String[] args){
        SpringApplication.run(ModuleWebApplication.class, args);
    }
    @GetMapping("/helloWorld")
    public String HelloWorld(){
        return "hello World";
    }
}
