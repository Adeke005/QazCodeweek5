package payment.httpagregator.service;

import payment.httpagregator.model.Result;

import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AggregatorService {

    private static final Logger logger =
            Logger.getLogger(AggregatorService.class.getName());

    private final ExecutorService executor;
    private final HttpClientService httpClientService;

    public AggregatorService(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
        this.executor = Executors.newFixedThreadPool(5);
    }

    public Result aggregate(List<String> urls) throws InterruptedException {

        Result result = new Result();

        List<Callable<Void>> tasks = urls.stream()
                .map(url -> (Callable<Void>) () -> {

                    logger.info("Start request: " + url);

                    try {
                        var response = httpClientService.sendGet(url);

                        if (response.getStatusCode() == 200) {
                            result.addSuccess(response.getResponseTime());
                            logger.info("Success request: " + url);
                        } else {
                            result.addError();
                            logger.warning("Non-200 response for: " + url);
                        }

                    } catch (Exception e) {
                        result.addError();
                        logger.log(Level.SEVERE,
                                "Error request: " + url, e);
                    }

                    return null;
                })
                .toList();

        List<Future<Void>> futures = executor.invokeAll(tasks);


        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                logger.log(Level.SEVERE,
                        "Execution exception", e);
            }
        }

        shutdownExecutor();

        return result;
    }

    private void shutdownExecutor() throws InterruptedException {

        executor.shutdown();

        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            logger.warning("Forcing executor shutdown...");
            executor.shutdownNow();
        }

        logger.info("ExecutorService shut down correctly.");
    }
}