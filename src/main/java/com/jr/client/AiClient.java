package com.jr.client;

import com.jr.dto.ai.ChatRequest;
import com.jr.dto.ai.ChatResponse;
import com.jr.dto.ai.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class AiClient {
    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.model}")
    private String model;
    private final RestTemplate restTemplate=new RestTemplate();
    public String generate(String systemPrompt,String userPrompt){
        List<Message>messages=new ArrayList<>();
        messages.add(new Message("system",systemPrompt));
        messages.add(new Message("user",userPrompt));
        ChatRequest req = new ChatRequest();
        req.setModel(model);
        req.setMessages(messages);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+apiKey);
        HttpEntity<ChatRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<ChatResponse> resp = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, ChatResponse.class);
        ChatResponse body = resp.getBody();
        if (body==null||body.getChoices()==null||body.getChoices().isEmpty())return null;
        return body.getChoices().get(0).getMessage().getContent();

    }
}
