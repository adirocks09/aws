package com.arisglobal.aws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ssm")
public class SystemParamController {

    @Autowired
    Environment env;

    @Value("${first.name}")
    private String firstName;

    @Value("${last.name}")
    private String lastName;

    @GetMapping("/value")
    public String getValues(){
        //firstName = env.getProperty("first.name");
        //lastName = env.getProperty("last.name");
        return firstName+" $$ "+lastName;
    }
}
