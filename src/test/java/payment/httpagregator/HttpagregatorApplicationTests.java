package payment.httpagregator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import payment.httpagregator.adapter.HttpResponseAdapter;
import payment.httpagregator.model.Result;
import payment.httpagregator.service.AggregatorService;
import payment.httpagregator.service.HttpClientService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AggregatorServiceTest {

    @Mock
    HttpClientService httpClientService;


    @Test
    void testSuccessfulRequests() throws Exception {

        HttpResponseAdapter response = mock(HttpResponseAdapter.class);
        when(response.getStatusCode()).thenReturn(200);
        when(response.getResponseTime()).thenReturn(150L);

        when(httpClientService.sendGet(anyString()))
                .thenReturn(response);

        AggregatorService service =
                new AggregatorService(httpClientService);

        Result result =
                service.aggregate(List.of("url1", "url2"));

        assertEquals(2, result.getSuccessCount());
        assertEquals(0, result.getErrorCount());
        assertEquals(150.0, result.getAverageTime());
    }


    @Test
    void testNon200Response() throws Exception {

        HttpResponseAdapter response = mock(HttpResponseAdapter.class);
        when(response.getStatusCode()).thenReturn(500);


        when(httpClientService.sendGet(anyString()))
                .thenReturn(response);

        AggregatorService service =
                new AggregatorService(httpClientService);

        Result result =
                service.aggregate(List.of("url1"));

        assertEquals(0, result.getSuccessCount());
        assertEquals(1, result.getErrorCount());
        assertEquals(0.0, result.getAverageTime());
    }


    @Test
    void testExceptionScenario() throws Exception {

        when(httpClientService.sendGet(anyString()))
                .thenThrow(new RuntimeException("Connection error"));

        AggregatorService service =
                new AggregatorService(httpClientService);

        Result result =
                service.aggregate(List.of("url1"));

        assertEquals(0, result.getSuccessCount());
        assertEquals(1, result.getErrorCount());
        assertEquals(0.0, result.getAverageTime());
    }


    @Test
    void testAverageTimeCalculation() throws Exception {

        HttpResponseAdapter response1 = mock(HttpResponseAdapter.class);
        when(response1.getStatusCode()).thenReturn(200);
        when(response1.getResponseTime()).thenReturn(100L);

        HttpResponseAdapter response2 = mock(HttpResponseAdapter.class);
        when(response2.getStatusCode()).thenReturn(200);
        when(response2.getResponseTime()).thenReturn(300L);

        when(httpClientService.sendGet("url1")).thenReturn(response1);
        when(httpClientService.sendGet("url2")).thenReturn(response2);

        AggregatorService service =
                new AggregatorService(httpClientService);

        Result result =
                service.aggregate(List.of("url1", "url2"));

        assertEquals(2, result.getSuccessCount());
        assertEquals(0, result.getErrorCount());
        assertEquals(200.0, result.getAverageTime());
    }
}