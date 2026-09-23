package com.jr;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class CourtsideApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourtsideApplication.class, args);
    }
    @Bean
    public CommandLineRunner testAi() {
        return args -> {
            RestTemplate rt = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer sk-475c0c4fa71f4a0dab1ac8104e51ae65");

            Map<String, Object> body = new HashMap<>();
            body.put("model", "deepseek-chat");
            body.put("messages", List.of(
                    Map.of("role", "user", "content", "请写一句篮球比赛的总结")
            ));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> resp = rt.postForEntity(
                    "https://api.deepseek.com/chat/completions",
                    entity,
                    Map.class
            );

            System.out.println("=== AI 返回 ===");
            System.out.println(resp.getBody());
        };
    }

}
