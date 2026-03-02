package payment.httpagregator.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Result {

    private final AtomicInteger successCount = new AtomicInteger();
    private final AtomicInteger errorCount = new AtomicInteger();
    private final AtomicLong totalTime = new AtomicLong();

    public void addSuccess(long time) {
        successCount.incrementAndGet();
        totalTime.addAndGet(time);
    }

    public void addError() {
        errorCount.incrementAndGet();
    }

    public int getSuccessCount() {
        return successCount.get();
    }

    public int getErrorCount() {
        return errorCount.get();
    }

    public double getAverageTime() {
        if (successCount.get() == 0) return 0;
        return totalTime.get() / (double) successCount.get();
    }
}
