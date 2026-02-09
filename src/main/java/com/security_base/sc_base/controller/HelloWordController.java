package com.security_base.sc_base.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWordController {

    @GetMapping("/helloseg")
    public String helloWordSeg(){
        return "Hola mundo esto esta securizado";
    }

    @GetMapping("/hellonoseg")
    public String helloWordNoSeg(){
        return "Hola mundo esto no esta securizado";
    }
}
