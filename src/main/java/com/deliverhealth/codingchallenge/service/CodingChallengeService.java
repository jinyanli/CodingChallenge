package com.deliverhealth.codingchallenge.service;

import com.deliverhealth.codingchallenge.model.Character;
import com.deliverhealth.codingchallenge.model.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CodingChallengeService {

    private RestTemplate restTemplate;

    private ObjectMapper mapper;

    @Value("${api-base-url}")
    private String theOneApiUrl;

    public CodingChallengeService(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public void setTheOneApiUrl(String theOneApiUrl) {
        this.theOneApiUrl = theOneApiUrl;
    }

    public List<String> getChapterNamesFromAllOfTheBooks() throws JsonProcessingException {
        UriComponents bookUri = UriComponentsBuilder.fromHttpUrl(theOneApiUrl).path("/book").build();
        ResponseEntity<String> booksResponse = restTemplate.getForEntity(bookUri.toUri(), String.class);
        JsonNode rootNode = mapper.readTree(booksResponse.getBody());
        JsonNode docsNode = rootNode.get("docs");
        List<String> ids = new ArrayList();
        docsNode.iterator().forEachRemaining(node ->{
            ids.add( node.get("_id").asText());
        });

        List<String> chapters = new ArrayList();
        for(String id : ids){
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(theOneApiUrl).path("/book").path("/"+id).path("/chapter").build();
            ResponseEntity<String> chapterResponse = restTemplate.getForEntity(uri.toUri(), String.class);

            JsonNode chapterRoot = mapper.readTree(chapterResponse.getBody());
            JsonNode chapterNode = chapterRoot.get("docs");

            chapterNode.iterator().forEachRemaining(node ->{
                chapters.add(node.get("chapterName").asText());
            });
        }

        return chapters;
    }

    public String searchChapterByName(String name) throws JsonProcessingException, UnsupportedEncodingException {

        UriComponents uri = UriComponentsBuilder.newInstance().fromHttpUrl(theOneApiUrl).path("/chapter").queryParam("chapterName","/"+name+"/i").build();

        ResponseEntity<String> chapterResponse = restTemplate.getForEntity(uri.toUri(), String.class);

        JsonNode chapterRoot = mapper.readTree(chapterResponse.getBody());
        JsonNode chapterNode = chapterRoot.get("docs");


        return chapterNode.findValue("chapterName").asText();
    }

    public List<Question> getQuestions() throws JsonProcessingException{

        UriComponents quoteUri = UriComponentsBuilder.newInstance().fromHttpUrl(theOneApiUrl).path("/quote").build();
        ResponseEntity<String> quotesResponse = restTemplate.getForEntity(quoteUri.toUri().toString(), String.class);

        JsonNode quoteRoot = mapper.readTree(quotesResponse.getBody());
        JsonNode docsNode = quoteRoot.get("docs");
        List<JsonNode> quotes = new ArrayList<>();
        docsNode.iterator().forEachRemaining( node -> quotes.add(node));
        Collections.shuffle(quotes);

        UriComponents allCharactersUri = UriComponentsBuilder.newInstance().fromHttpUrl(theOneApiUrl).path("/character").build();
        ResponseEntity<String> allCharactersResponse = restTemplate.getForEntity(allCharactersUri.toUri(), String.class);

        JsonNode allCharactersRoot = mapper.readTree(allCharactersResponse.getBody());
        JsonNode allCharactersDocsNode = allCharactersRoot.get("docs");
        List<JsonNode> characters = new ArrayList<>();
        allCharactersDocsNode.iterator().forEachRemaining(node -> characters.add(node));
        Collections.shuffle(characters);


        List<Question> fiveRandomQuotes = quotes.stream().limit(5).map(node ->{
            Question question =  new Question();
            question.setQuote(node.get("dialog").asText());
            question.setQuoteId(node.get("_id").asText());

            JsonNode correctCharacter = characters.stream().filter(c -> c.get("_id").asText().equalsIgnoreCase(node.get("character").asText() )).findFirst().get();
            String correctCharName = correctCharacter.findValue("name").asText();
            String charId = correctCharacter.findValue("_id").asText();
            Character character = new Character(correctCharName, charId);
            question.getCharacters().add(character);
            List<JsonNode> randomCharacters = characters.stream().filter(c-> !c.findValue("name").asText().equalsIgnoreCase(correctCharName)).collect(Collectors.toList());

            Collections.shuffle(randomCharacters);
            for (int i = 0; i<4; i++ ){
                String randomCharName = randomCharacters.get(i).findValue("name").asText();
                String randomCharId = randomCharacters.get(i).findValue("_id").asText();
                Character randomCharacter = new Character(randomCharName, randomCharId );
                question.getCharacters().add(randomCharacter);
            }
            Collections.shuffle(question.getCharacters());
            return question;
        }).collect(Collectors.toList());

        return fiveRandomQuotes;
    }

    public Integer getScore(String input) throws JsonProcessingException {
        int score =0;
        JsonNode root = mapper.readTree(input);
        Iterator<JsonNode> iter = root.iterator();
        while (iter.hasNext()){
            JsonNode questionAnswer = iter.next();

            String quoteId = questionAnswer.findValue("quoteId").asText();
            String characterId = questionAnswer.findValue("characterId").asText();
            UriComponents quoteUri = UriComponentsBuilder.newInstance().fromHttpUrl(theOneApiUrl).path("/quote").path("/"+quoteId).build();
            ResponseEntity<String> quoteResponse = restTemplate.getForEntity(quoteUri.toUri(), String.class);
            JsonNode quoteRoot = mapper.readTree(quoteResponse.getBody());
            if(characterId.equalsIgnoreCase(quoteRoot.findValue("character").asText()))
                score++;
        }

        return score;
    }
}
