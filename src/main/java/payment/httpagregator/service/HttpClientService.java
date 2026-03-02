package payment.httpagregator.service;




import payment.httpagregator.adapter.HttpResponseAdapter;
import payment.httpagregator.adapter.JavaHttpResponseAdapter;
import payment.httpagregator.config.HttpClientSingleton;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

public class HttpClientService {

    public HttpResponseAdapter sendGet(String url) throws Exception {

        var client = HttpClientSingleton.getInstance();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        long start = System.currentTimeMillis();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        long end = System.currentTimeMillis();

        return new JavaHttpResponseAdapter(response, end - start);
    }
}
