package ir.curlymind.webclient.service;

import ir.curlymind.webclient.DefaultSubscriber;
import ir.curlymind.webclient.Response;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MathService {

    @Autowired
    private WebClient webClient;

    @PostConstruct
    public void test() {
        square();
    }

    public void table() {
        Flux<Response> response = this.webClient
                .get()
                .uri("reactive-math/table/{input}", 5)
                .retrieve()
                .bodyToFlux(Response.class);
        response.subscribe(new DefaultSubscriber());
    }


    public void square() {
        Mono<Response> response = this.webClient
                .get()
                .uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class);
        response.subscribe(new DefaultSubscriber());
    }

}
