package com.example.cloud_computing_second_homework.service;

import com.example.cloud_computing_second_homework.configuration.AppConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RequestService {
    private static final String DICTIONARY_URL = "https://api.api-ninjas.com/v1/dictionary?word=";
    private static final String RANDOM_WORD_URL = "https://api.api-ninjas.com/v1/randomword";
    private static final String REGEX = "\"word\"\\s*:\\s*\\[\\s*\"([^\"]+)\"\\s*\\]";
    private final RestTemplate restTemplate;
    private final AppConfiguration appConfiguration;
    private final CacheManager cacheManager;

    public RequestService(RestTemplate restTemplate, AppConfiguration appConfiguration, CacheManager cacheManager) {
        this.restTemplate = restTemplate;
        this.appConfiguration = appConfiguration;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "wordCache", key = "#word", unless = "#result == null")
    public String getWordDefinition(String word) {
        String finalResponse = "";
        // Check if there was meaning in cache or not
        String source = "API-CALL";
        Cache cache = cacheManager.getCache("wordCache");
        String definition = cache != null ? cache.get(word, String.class) : null;
        if (definition != null) {
            source = "REDIS";
            finalResponse = "Response from : " + source + " : " + definition;
        }

        if (source.equals("API-CALL")) {
            String mainUrl = DICTIONARY_URL + word;
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Api-Key", appConfiguration.getApiKey());
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(mainUrl, HttpMethod.GET, httpEntity, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    String responseBody = response.getBody();
                    finalResponse = "Response from : " + source + " : " + responseBody;
                    cache.put(word, responseBody);
                }
            } catch (Exception e) {
                finalResponse = "Error occurred during do service call";
            }
        }

        return finalResponse;
    }

    @CachePut(value = "wordCache", key = "#randomWord")
    public String getRandomWordWithDefinition() {
        String finalResponse = "";
        String source = "API-CALL";

        Cache cache = cacheManager.getCache("wordCache");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", appConfiguration.getApiKey());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        //Get random word
        try {
            ResponseEntity<String> randomWordResponse = restTemplate.exchange(RANDOM_WORD_URL, HttpMethod.GET, httpEntity, String.class);
            if (randomWordResponse.getStatusCode().is2xxSuccessful()) {
                String randomWord = retrieveOriginalWord(randomWordResponse.getBody());
                String definition = cache != null ? cache.get(randomWord, String.class) : null;
                if (definition != null) {
                    source = "REDIS";
                } else {
                    definition = getWordDefinition(randomWord);
                    if (cache != null) {
                        cache.put(randomWord, definition);
                    }
                }
                finalResponse = "Response from : " + source + " -  " + randomWord + " : " + definition;
            }
        } catch (Exception e) {
            finalResponse = "Error occurred during do FIRST service call";
        }

        return finalResponse;
    }

    private String retrieveOriginalWord(String jsonResponse) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(jsonResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "ERROR";
    }

}
