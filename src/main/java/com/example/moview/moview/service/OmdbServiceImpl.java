package com.example.moview.moview.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OmdbServiceImpl implements OmdbService {

    private static final String OMDB_URL = "http://www.omdbapi.com";
    private static final String API_KEY = "87ecf4e3";


    @Override
    public Integer getRatingByMovieName(final String name) throws JsonProcessingException {
        final String uriString = buildUri(name, false);
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode jsonNode = objectMapper.readTree(execute(uriString));
        return Integer.valueOf(jsonNode.get("imdbRating").asText().replace(".", ""));
    }

    private String execute(final String url) {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    private String buildUri(final String movieName, final boolean fullDescription) {
        return UriComponentsBuilder.fromHttpUrl(OMDB_URL)
                .queryParam("apikey", API_KEY)
                .queryParam("t", movieName)
                .queryParam("plot", fullDescription ? "full" : "short")
                .build()
                .toUriString();
    }
}
