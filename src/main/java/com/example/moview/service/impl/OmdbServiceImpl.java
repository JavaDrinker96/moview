package com.example.moview.service.impl;

import com.example.moview.service.OmdbService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OmdbServiceImpl implements OmdbService {

    private static final String OMDB_URL = "http://www.omdbapi.com";
    private static final String API_KEY = "87ecf4e3";
    private static final String OMDB_RATING_PROPERTY = "imdbRating";
    private static final String OMDB_DESCRIPTION_PROPERTY = "Plot";


    @Override
    public Integer getRatingByMovieName(final String name) {
        final String uriString = buildUri(name, false);
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            final JsonNode jsonNode = objectMapper.readTree(execute(uriString));
            return Integer.valueOf(jsonNode.get(OMDB_RATING_PROPERTY).asText().replace(".", ""));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getDescriptionByMovieName(final String name) {
        final String uriString = buildUri(name, true);
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            final JsonNode jsonNode = objectMapper.readTree(execute(uriString));
            return jsonNode.get(OMDB_DESCRIPTION_PROPERTY).asText();
        } catch (Exception e) {
            return null;
        }
    }

    private String execute(final String url) {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    private String buildUri(final String movieName, final boolean fullDescription) {
        final String apiKeyParamName = "apikey";
        final String movieNameParamName = "t";
        final String descriptionParamName = "plot";

        return UriComponentsBuilder.fromHttpUrl(OMDB_URL)
                .queryParam(apiKeyParamName, API_KEY)
                .queryParam(movieNameParamName, movieName)
                .queryParam(descriptionParamName, fullDescription ? "full" : "short")
                .build()
                .toUriString();
    }

}
