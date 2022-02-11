package com.deliverhealth.codingchallenge.controller;

import com.deliverhealth.codingchallenge.model.Question;
import com.deliverhealth.codingchallenge.service.CodingChallengeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CodingChallengeController {

    private CodingChallengeService codingChallengeService;

    public CodingChallengeController(CodingChallengeService codingChallengeService) {
        this.codingChallengeService = codingChallengeService;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/showChapterNamesFromAllOfTheBooks")
    public String showChapterNamesFromAllOfTheBooks(Model model) throws JsonProcessingException {
        List<String> chapterNames = codingChallengeService.getChapterNamesFromAllOfTheBooks();
        model.addAttribute("chapterNames", chapterNames);

        return "showChapterNamesFromAllOfTheBooks";
    }

    @GetMapping("/searchChapterByName")
    public String searchChapterByName(@RequestParam(name="chapterName", required=false) String name, Model model) throws JsonProcessingException, UnsupportedEncodingException {
       if(name != null && !"".equalsIgnoreCase(name)){
           String chapterName = codingChallengeService.searchChapterByName(name);
           model.addAttribute("chapterName", chapterName);
       }
        return "searchChapterByName";
    }

    @GetMapping("/getQuestions")
    public String getQuestions( Model model) throws JsonProcessingException, UnsupportedEncodingException {
        List<Question> questions = codingChallengeService.getQuestions();
        model.addAttribute("questions", questions);
        return "getQuestions";
    }

    @PostMapping("/showScore")
    public String showScore(@RequestBody String input, Model model) throws JsonProcessingException, UnsupportedEncodingException {
        String[] arr = input.split("&");
        List<String> inputList = Arrays.asList(arr);
        List<List<Map>> inputMapList = inputList.stream().map(v -> {
            String[] pairArr = v.split("=");
            Map<Object, Object> quote = new HashMap<>();
            Map<Object, Object> character = new HashMap<>();
            quote.put("quoteId",pairArr[0]);
            character.put("characterId",pairArr[1]);
            List<Map> pair = new ArrayList<>();
            pair.add(quote);
            pair.add(character);
            return pair;
        }).collect(Collectors.toList());

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonInput = ow.writeValueAsString(inputMapList);
        int score =codingChallengeService.getScore(jsonInput);
        model.addAttribute("score", score );
        return "showScore";
    }
}
