package com.example.moview.moview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class OmdbServiceImpl implements OmdbService {

    private static final String OMDB_URL = "http://www.omdbapi.com";
    private static final String API_KEY = "87ecf4e3";
    private static final String OMDB_RATING_PROPERTY = "imdbRating";
    private static final String OMDB_DESCRIPTION_PROPERTY = "Plot";
    private static final String RESPONSE_QUALITY_PROPERTY = "Response";


    @Override
    @SneakyThrows
    public Optional<Integer> getRatingByMovieName(final String name) {
        final String uriString = buildUri(name, false);
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode jsonNode = objectMapper.readTree(execute(uriString));

        if (gotResponse(jsonNode)) {
            return Optional.of(
                    Integer.valueOf(jsonNode.get(OMDB_RATING_PROPERTY).asText().replace(".", ""))
            );
        }
        return Optional.empty();

    }

    @Override
    @SneakyThrows
    public Optional<String> getDescriptionByMovieName(final String name) {
        final String uriString = buildUri(name, true);
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode jsonNode = objectMapper.readTree(execute(uriString));

        if (gotResponse(jsonNode)) {
            return Optional.of(jsonNode.get(OMDB_DESCRIPTION_PROPERTY).asText());
        }
        return Optional.empty();
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

    private String execute(final String url) {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    private boolean gotResponse(final JsonNode jsonNode){
        return Boolean.valueOf(jsonNode.get(RESPONSE_QUALITY_PROPERTY).asText());
    }
}