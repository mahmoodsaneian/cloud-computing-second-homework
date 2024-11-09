package com.example.cloud_computing_second_homework.controller;

import com.example.cloud_computing_second_homework.service.RequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/get-word-definition")
    public String getWordDefinition(@RequestParam("word") String word) {
        return requestService.getWordDefinition(word);
    }

    @GetMapping("get-random-word-with-definition")
    public String getRandomWordWithDefinition() {
        return requestService.getRandomWordWithDefinition();
    }
}

// api key = ninja -> lOF4jbtIhG1QrJ9EZCOi6g==Kk9WlyeOjj8dUT1D