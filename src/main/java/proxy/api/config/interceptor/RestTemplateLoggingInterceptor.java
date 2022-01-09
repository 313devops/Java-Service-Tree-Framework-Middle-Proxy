package proxy.api.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RestTemplateLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        String requestUrl = request.getURI().toString();
        String requestMethod = request.getMethod().toString();
        String requestHeader = request.getHeaders().toString();
        String requestBody = new String(body, StandardCharsets.UTF_8);

        long requestStartTime = System.currentTimeMillis();

        ClientHttpResponse response = execution.execute(request, body);

        long requestEndTime = System.currentTimeMillis();
        double elapsedTime = requestEndTime - requestStartTime;
        double elapsedTimeSec = elapsedTime / 1000.0;
        elapsedTimeSec = Math.round(elapsedTimeSec * 10) / 10.0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
        String responseHeader = response.getHeaders().toString();
        String responseStatusCode = response.getStatusCode().toString();
        String responseBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        log.debug("\n\n[RestTemplate Request Log ( elapsedTime: {}s)]\nRequest URL : {}\nRequest Method : {}\nRequest Header : {}\nRequest Body : {}\n\n" +
                          "[RestTemplate Response Log]\nStatus Code : {}\nResponse Header : {}\nResponse Body : {}\n\n",
                            elapsedTimeSec, requestUrl, requestMethod, requestHeader, requestBody,
                            responseStatusCode, responseHeader, responseBody);



        return response;
    }
}
