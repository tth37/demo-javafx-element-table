package demo.ui.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import com.alibaba.fastjson2.JSON;

/**
 * Http Session
 */
public class Session {
    private String bearerToken;
    private final HttpClient client;
    private final String base;

    public Session() {
        this.bearerToken = null;
        this.client = HttpClient.newBuilder().build();
        this.base = "http://localhost:8080";
    }

    public <T> Response<T> request(String url, String method, Object body, Map<String, String> params) throws Exception {
        String bodyStr = JSON.toJSONString(body);

        StringBuilder urlBuilder = new StringBuilder(base + url);
        if (!params.isEmpty()) {
            urlBuilder.append("?");
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        url = urlBuilder.toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.bearerToken)
                .method(method, HttpRequest.BodyPublishers.ofString(bodyStr))
                .timeout(Duration.ofSeconds(5))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return new Response<>(response.statusCode(), null, response.body());
        }

        return new Response<>(response.statusCode(), JSON.parseObject(response.body()), "success");
    }

    public <T> Response<T> get(String url, Map<String, String> params) {
        try {
            return this.request(url, "GET", null, params);
        } catch (Exception e) {
            return new Response<>(600, null, "Connection Error");
        }
    }

    public <T> Response<T> post(String url, Object body) throws Exception {
        try {
            return this.request(url, "POST", body, Map.of());
        } catch (Exception e) {
            return new Response<>(600, null, "Connection Error");
        }
    }

    public <T> Response<T> patch(String url, Object body) throws Exception {
        try {
            return this.request(url, "PATCH", body, Map.of());
        } catch (Exception e) {
            return new Response<>(600, null, "Connection Error");
        }
    }

    public <T> Response<T> delete(String url, Object body) throws Exception {
        try {
            return this.request(url, "DELETE", body, Map.of());
        } catch (Exception e) {
            return new Response<>(600, null, "Connection Error");
        }
    }

    public void setToken(String token) {
        this.bearerToken = token;
    }
}
