package com.example.msa_backend.llm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class OpenAIClient {

    @Value("${openai.api.key}")
    private String apiKey;

    public String sendPrompt(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> request = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(
                            Map.of("role", "system", "content", "운영 조언 제공자"),
                            Map.of("role", "user", "content", prompt)
                    )
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions", entity, Map.class
            );

            Map firstChoice = ((List<Map>) Objects.requireNonNull(response.getBody()).get("choices")).get(0);
            Map message = (Map) firstChoice.get("message");
            return (String) message.get("content");

        } catch (HttpClientErrorException.TooManyRequests e) {
            // ❗ 요금제 초과 시 사용자에게 명확한 메시지 반환
            return "❗ 사용량 제한을 초과했습니다. OpenAI 대시보드에서 사용량 또는 요금제를 확인하세요.";
        } catch (HttpClientErrorException.Unauthorized e) {
            return "❗ OpenAI 인증 실패: API 키가 잘못되었거나 만료되었습니다.";
        } catch (Exception e) {
            // 그 외 에러
            System.err.println("❌ OpenAIClient 예외: " + e.getMessage());
            e.printStackTrace();
            return "❌ OpenAI 호출 중 알 수 없는 오류가 발생했습니다.";
        }
    }
}