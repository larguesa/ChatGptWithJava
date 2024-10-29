/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author rlarg
 */
public class OpenAI {
    private static final String API_KEY = "[SUA CHAVE]";

    public static String getCompletion(String prompt) throws Exception {
        JSONObject data = new JSONObject();
        data.put("model", "gpt-3.5-turbo");
        data.put("messages", new JSONArray()
            .put(new JSONObject()
                .put("role", "user")
                .put("content", prompt)
            )
        );
        data.put("max_tokens", 4000);
        data.put("temperature", 1.0);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("https://api.openai.com/v1/chat/completions"))
            .header("Authorization", "Bearer " + API_KEY)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na requisição: " + response.statusCode() + " - " + response.body());
        }else{
            return new JSONObject(response.body())
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .get("content").toString();
        }
    }
    
    public static void main(String[] args) {
        try{
            System.out.println(OpenAI.getCompletion("Em uma frase, o palmeiras tem mundial?"));
        }catch(Exception ex){
            System.out.println("ERRO: "+ex.getLocalizedMessage());
        }
    }
}