package payment.httpagregator.adapter;


public interface HttpResponseAdapter {
    int getStatusCode();
    String getBody();
    long getResponseTime();
}
