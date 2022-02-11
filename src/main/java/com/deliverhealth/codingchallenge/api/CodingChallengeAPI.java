package com.deliverhealth.codingchallenge.api;

import com.deliverhealth.codingchallenge.model.Question;
import com.deliverhealth.codingchallenge.service.CodingChallengeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@RequestMapping(path = "/api")
public class CodingChallengeAPI {


    private CodingChallengeService codingChallengeService;

    public CodingChallengeAPI(CodingChallengeService codingChallengeService) {
        this.codingChallengeService = codingChallengeService;
    }

    @GetMapping(path = "/getChapterNamesFromAllOfTheBooks")
    public ResponseEntity<List<String>> getChapterNamesFromAllOfTheBooks() throws JsonProcessingException {

        ResponseEntity<List<String>> responseEntity = new ResponseEntity(codingChallengeService.getChapterNamesFromAllOfTheBooks(), HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(path = "/searchChapterByName/{name}")
    public ResponseEntity<String> searchChapterByName(@PathVariable String name) throws JsonProcessingException, UnsupportedEncodingException {

        ResponseEntity<String> responseEntity = new ResponseEntity(codingChallengeService.searchChapterByName(name), HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(path = "/getQuestions")
    public ResponseEntity<List<Question>> getQuestions() throws JsonProcessingException{
        ResponseEntity<List<Question>> responseEntity = new ResponseEntity(codingChallengeService.getQuestions(), HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping(path = "/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody String input) throws JsonProcessingException{
        ResponseEntity<Integer> responseEntity = new ResponseEntity(codingChallengeService.getScore(input), HttpStatus.OK);
        return responseEntity;
    }
}
