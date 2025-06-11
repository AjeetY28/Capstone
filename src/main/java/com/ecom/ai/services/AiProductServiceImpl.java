package com.ecom.ai.services;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiProductServiceImpl implements AiProductService {

    @Value("${gemini.api.key}")
    private String API_KEY;


    @Override
    public String simpleChat(String prompt) {
//        return "";

        try {
            // Construct the Gemini API URL with the API key
//            String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

            String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

            // Create HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construct the request body
            JSONObject requestBody = new JSONObject()
                    .put("contents", new JSONArray()
                            .put(new JSONObject()
                                    .put("parts", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("text", prompt)
                                            )
                                    )
                            )
                    );

            // Create the HTTP entity with headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

            // Make the API call
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(geminiApiUrl, requestEntity, String.class);

            // Process the response
            String responseBody = response.getBody();
            JSONObject jsonResponse = new JSONObject(responseBody);

            // Extract the text from the response
            JSONArray candidates = jsonResponse.getJSONArray("candidates");
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            JSONObject firstPart = parts.getJSONObject(0);

            return firstPart.getString("text");

        } catch (Exception e) {
            // Log the error
            System.err.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();

            // Return an error message
            return "Sorry, I couldn't process your request. Please try again later.";
        }

    }
}
