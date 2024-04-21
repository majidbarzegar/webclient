package ir.curlymind.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:1001")
//                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth("username", "password"))
                .filter(this::sessionToken)
                .build();
    }

    /*private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex){
        System.out.println("generating session token");
        ClientRequest clientRequest = ClientRequest.from(request).headers(header -> header.setBearerAuth("bearer auth")).build();
        return ex.exchange(clientRequest);
    }*/

    private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
        System.out.println("generating session token");
        ClientRequest clientRequest = request
                .attribute("auth")
                .map(v -> v.equals("basic") ? withBasicAuth(request) : withOAuth(request))
                .orElse(request);
        return ex.exchange(clientRequest);
    }

    private ClientRequest withBasicAuth(ClientRequest request) {
        System.out.println("generating BasicAuth");
        return ClientRequest
                .from(request)
                .headers(header -> header.setBasicAuth("username", "password"))
                .build();
    }

    private ClientRequest withOAuth(ClientRequest request) {
        System.out.println("generating OAuth");
        return ClientRequest
                .from(request)
                .headers(header -> header.setBearerAuth("OAuth token"))
                .build();
    }
}
