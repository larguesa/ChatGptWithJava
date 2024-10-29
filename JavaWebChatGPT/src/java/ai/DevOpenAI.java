/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author rlarg
 */
public class DevOpenAI {
    private static final String API_KEY = "[SUA CHAVE]";
    
    private static final HttpClient httpClient = createHttpClient();
    
    private static HttpClient createHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar HttpClient: " + e.getMessage());
        }
    }
    
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

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na requisição: " + response.statusCode() + " - " + response.body());
        } else {
            return new JSONObject(response.body())
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .get("content").toString();
        }
    }
    
    public static JSONObject getCompletion(JSONObject data) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na requisição: " + response.statusCode() + " - " + response.body());
        } else {
            return new JSONObject(response.body());
        }
    }

}
