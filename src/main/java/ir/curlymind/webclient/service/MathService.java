package ir.curlymind.webclient.service;

import ir.curlymind.webclient.DefaultSubscriber;
import ir.curlymind.webclient.Response;
import ir.curlymind.webclient.model.MultiplyRequestDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MathService {

    @Autowired
    private WebClient webClient;

    private static final int A = 10;
    private static final String FORMAT = "%d %s %d = %s";

    @PostConstruct
    public void test() {
        Flux
                .range(1, 5)
                .flatMap(b ->
                        Flux
                                .just("+", "-", "*", "/")
                                .flatMap(op -> send(b, op))
                )
                .subscribe(new DefaultSubscriber());


    }

    private Mono<String> send(int b, String op) {
        return this.webClient
                .get()
                .uri("calculator/{a}/{b}", A, b)
                .headers(header -> header.set("OP", op))
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> String.format(FORMAT, A, op, b, s));
    }


    public void multiply() {
        Mono<Response> response = this.webClient
                .post()
                .uri("reactive-math/multiply")
                .bodyValue(new MultiplyRequestDto(5, 6))
                .attribute("auth", "basic")
                .retrieve()
                .bodyToMono(Response.class);
        response.subscribe(new DefaultSubscriber());
    }

    public void tableStream() {
        Flux<Response> response = this.webClient
                .get()
                .uri("reactive-math/table/{input}/stream", 5)
                .attribute("auth", "oauth")
                .retrieve()
                .bodyToFlux(Response.class);
        response.subscribe(new DefaultSubscriber());
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

    public void squareThrow() {
        Mono<Response> response = this.webClient
                .get()
                .uri("reactive-math/square/{input}/throw", 5)
                .retrieve()
                .bodyToMono(Response.class);
        response.subscribe(new DefaultSubscriber());
    }

    public void searchParam() {
        /*URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:1001/reactive-param/search?count={count}&page={page}")
                .build(10, 20);*/

        Map<String, Integer> map = Map.of(
                "count", 10,
                "page", 20
        );

        Flux<Response> responseFlux = this.webClient
                .get()
//                .uri(uri)
//                .uri(uriBuilder -> uriBuilder.path("reactive-param/search").query("count={count}&page={page}").build(10, 20))
                .uri(uriBuilder -> uriBuilder.path("reactive-param/search").query("count={count}&page={page}").build(map))
                .retrieve()
                .bodyToFlux(Response.class);
        responseFlux.subscribe(new DefaultSubscriber());
    }

    public void attribute() {
        Mono<Response> response = this.webClient
                .get()
                .uri("reactive-math/square/{input}/throw", 5)
                .retrieve()
                .bodyToMono(Response.class);
        response.subscribe(new DefaultSubscriber());
    }

}
