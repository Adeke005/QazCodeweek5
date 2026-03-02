package payment.httpagregator.config;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientSingleton {

    private static final HttpClient INSTANCE = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .version(HttpClient.Version.HTTP_2)
            .build();

    private HttpClientSingleton() {}

    public static HttpClient getInstance() {
        return INSTANCE;
    }
}
