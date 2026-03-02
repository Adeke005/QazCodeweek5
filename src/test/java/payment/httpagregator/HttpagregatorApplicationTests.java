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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AggregatorServiceTest {

    @Mock
    HttpClientService httpClientService;

    @Test
    void testSuccessAggregation() throws Exception {

        HttpResponseAdapter response = mock(HttpResponseAdapter.class);
        when(response.getStatusCode()).thenReturn(200);
        when(response.getResponseTime()).thenReturn(100L);

        when(httpClientService.sendGet(anyString()))
                .thenReturn(response);

        AggregatorService service =
                new AggregatorService(httpClientService);

        Result result =
                service.aggregate(List.of("url1", "url2"));

        assertEquals(2, result.getSuccessCount());
        assertEquals(0, result.getErrorCount());
    }
}