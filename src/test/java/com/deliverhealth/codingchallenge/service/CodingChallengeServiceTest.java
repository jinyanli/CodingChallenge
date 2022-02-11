package com.deliverhealth.codingchallenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
class CodingChallengeServiceTest {

    @Mock
    private RestTemplate restTemplateMock;

    private CodingChallengeService codingChallengeService;

    private String jason = "{\n" +
            "    \"docs\": [\n" +
            "        {\n" +
            "            \"_id\": \"6091b6d6d58360f988133b8d\",\n" +
            "            \"chapterName\": \"Three is Company\",\n" +
            "            \"book\": \"5cf5805fb53e011a64671582\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"total\": 1,\n" +
            "    \"limit\": 1000,\n" +
            "    \"offset\": 0,\n" +
            "    \"page\": 1,\n" +
            "    \"pages\": 1\n" +
            "}";

    private String url = "https://the-one-api.dev/v2";

    @BeforeEach
    void setUp(){
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> chapterResponse = new ResponseEntity(jason, HttpStatus.OK);
        lenient().when(restTemplateMock.getForEntity(any(URI.class), any(Class.class))).thenReturn(chapterResponse);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        codingChallengeService = new CodingChallengeService(restTemplateMock, mapper);
        codingChallengeService.setTheOneApiUrl(url);
    }

    @Test
    @Ignore
    void getChapterNamesFromAllOfTheBooks() {
    }

    @Test
    void searchChapterByName() throws UnsupportedEncodingException, JsonProcessingException {
       assertEquals(codingChallengeService.searchChapterByName("Three"), "Three is Company");
    }

    @Test
    @Ignore
    void getQuestions() {
    }

    @Test
    @Ignore
    void getScore() {
    }
}