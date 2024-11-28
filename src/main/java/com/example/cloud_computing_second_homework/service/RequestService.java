package com.example.cloud_computing_second_homework.service;

import com.example.cloud_computing_second_homework.configuration.AppConfiguration;
import com.example.cloud_computing_second_homework.entity.WordEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.cloud_computing_second_homework.repository.WordRepository;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private static final String DICTIONARY_URL = "https://api.api-ninjas.com/v1/dictionary?word=";
    private static final String RANDOM_WORD_URL = "https://api.api-ninjas.com/v1/randomword";
    private static final String REGEX = "\"word\"\\s*:\\s*\\[\\s*\"([^\"]+)\"\\s*\\]";
    private final RestTemplate restTemplate;
    private final AppConfiguration appConfiguration;
    @Autowired
    private final WordRepository wordRepository;
    private final Environment environment;

    public String getWordDefinition(String word) {
        String finalResponse = "Hostname : " + environment.getProperty("HOSTNAME") + "\n";
        // Check if there was meaning in cache or not
        String source = "API-CALL";
        if (wordRepository.existsById(word)) {
            source = "Redis";
            log.info("Exist in cache");
            Optional<WordEntity> wordEntity = wordRepository.findById(word);
            finalResponse += "Response from : " + source + " - " + wordEntity.get().getWord() + " : " + wordEntity.get().getDefinition();
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
                    finalResponse += "Response from : " + source + " : " + responseBody;
                    WordEntity wordEntity = new WordEntity(word, responseBody, true);
                    wordRepository.save(wordEntity);
                }
            } catch (Exception e) {
                finalResponse += "Error occurred during do service call";
            }
        }

        return finalResponse;
    }

    public String getRandomWordWithDefinition() {
        String finalResponse = "";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", appConfiguration.getApiKey());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        //Get random word
        try {
            ResponseEntity<String> randomWordResponse = restTemplate.exchange(RANDOM_WORD_URL, HttpMethod.GET, httpEntity, String.class);
            if (randomWordResponse.getStatusCode().is2xxSuccessful()) {
                String randomWord = retrieveOriginalWord(randomWordResponse.getBody());
                finalResponse = getWordDefinition(randomWord);
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
