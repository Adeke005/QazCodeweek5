package payment.httpagregator.adapter;



import java.net.http.HttpResponse;

public class JavaHttpResponseAdapter implements HttpResponseAdapter {

    private final HttpResponse<String> response;
    private final long responseTime;

    public JavaHttpResponseAdapter(HttpResponse<String> response, long responseTime) {
        this.response = response;
        this.responseTime = responseTime;
    }

    @Override
    public int getStatusCode() {
        return response.statusCode();
    }

    @Override
    public String getBody() {
        return response.body();
    }

    @Override
    public long getResponseTime() {
        return responseTime;
    }
}
