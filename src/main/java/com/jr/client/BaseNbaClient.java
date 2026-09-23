package com.jr.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.X509Certificate;
import java.util.Map;

public abstract class BaseNbaClient {

    protected final RestTemplate restTemplate;

    @Value("${nba.api.base-url}")
    protected String BASE_URL;

    @Value("${nba.api.key}")
    protected String API_KEY;

    public BaseNbaClient() {
        this.restTemplate = createUnsafeRestTemplate();
    }

    // 创建完全绕过 SSL 的 RestTemplate
    private RestTemplate createUnsafeRestTemplate() {
        try {
            // 1. 信任所有证书
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // 2. 设置默认的 SSL 工厂和 HostnameVerifier（全局生效）
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            // 3. 创建自定义的 SimpleClientHttpRequestFactory
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
                @Override
                protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                    if (connection instanceof HttpsURLConnection) {
                        ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
                        ((HttpsURLConnection) connection).setHostnameVerifier((hostname, session) -> true);
                    }
                    super.prepareConnection(connection, httpMethod);
                }
            };

            return new RestTemplate(factory);

        } catch (Exception e) {
            e.printStackTrace();
            return new RestTemplate(); // 降级方案
        }
    }

    protected <T> T getWithAuth(String path, Map<String, String> queryParams, Class<T> responseType) {
        String url = buildUrl(BASE_URL + path, queryParams);
        System.out.println("请求 URL: " + url); // ← 加日志方便调试

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", API_KEY);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    responseType
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("请求失败: " + e.getMessage());
            e.printStackTrace();  // ← 打印完整堆栈
            throw e;
        }
    }

    private String buildUrl(String base, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return base;
        }
        StringBuilder sb = new StringBuilder(base);
        char prefix = base.contains("?") ? '&' : '?';
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(prefix).append(entry.getKey()).append("=").append(entry.getValue());
            prefix = '&';
        }
        return sb.toString();
    }
}