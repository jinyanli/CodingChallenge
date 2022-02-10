package com.deliverhealth.codingchallenge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String quote;
    private String quoteId;
    private List<Character> characters = new ArrayList<>();

}
