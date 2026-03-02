package payment.httpagregator.service;



import payment.httpagregator.model.Result;

import java.util.List;
import java.util.concurrent.*;

public class AggregatorService {

    private final ExecutorService executor =
            Executors.newFixedThreadPool(5);

    private final HttpClientService httpClientService;

    public AggregatorService(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    public Result aggregate(List<String> urls) throws InterruptedException {

        Result result = new Result();

        List<Callable<Void>> tasks = urls.stream()
                .map(url -> (Callable<Void>) () -> {
                    try {
                        var response = httpClientService.sendGet(url);

                        if (response.getStatusCode() == 200) {
                            result.addSuccess(response.getResponseTime());
                        } else {
                            result.addError();
                        }

                    } catch (Exception e) {
                        result.addError();
                    }
                    return null;
                })
                .toList();

        executor.invokeAll(tasks);
        shutdown();

        return result;
    }

    private void shutdown() {
        executor.shutdown();
    }
}
