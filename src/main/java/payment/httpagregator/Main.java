package payment.httpagregator;

import payment.httpagregator.service.AggregatorService;
import payment.httpagregator.service.HttpClientService;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        var httpClientService = new HttpClientService();
        var aggregator = new AggregatorService(httpClientService);

        var urls = List.of(
                "https://google.com",
                "https://github.com",
                "https://invalid-url.com"
        );

        var result = aggregator.aggregate(urls);

        System.out.println("Success: " + result.getSuccessCount());
        System.out.println("Errors: " + result.getErrorCount());
        System.out.println("Avg time: " + result.getAverageTime());
    }
}
