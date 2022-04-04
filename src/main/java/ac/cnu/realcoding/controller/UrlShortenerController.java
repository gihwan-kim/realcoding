package ac.cnu.realcoding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ac.cnu.realcoding.models.UrlShortenerRequest;
import ac.cnu.realcoding.models.UrlShortenerResponse;
import ac.cnu.realcoding.service.UrlShortenerService;
import reactor.core.publisher.Mono;

// web service 주소 정보

@RestController
@RequestMapping("/")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    // {n} : 변수로
    @GetMapping("health/{n}")
    public Mono<String> healthCheck(@PathVariable int n) {
        // For basic tutorial

        return fibo(n).map(elem -> {
            return String.valueOf(elem);
        });
    }

    // Mono.just() :
    private  Mono<Integer> fibo(int n) {
        if (n ==0) {
            return Mono.just(0);
        }

        if (n == 1 || n == 2) {
            return Mono.just(1);
        }

        // 정의해 주어도 실행되지 않는다.
        Mono<Integer> f0 = fibo(n-1);
        Mono<Integer> f1 = fibo(n-2);

        return (Mono.zip(f0, f1, (n0, n1) ->  n0 + n1));
    }

    // 우리가 해야할 것
    @GetMapping("{encoded}")
    public Mono<ResponseEntity<Object>> unshorten(@PathVariable String encoded) {
        // Question A: What is difference between HTTPStatus.MOVED_PERMANENTLY and HttpStatus.FOUND (302)
        // Question B: What is the role of location header from HTTP protocol?
        // Question C: Implement unshorten logic
        return urlShortenerService
                .unshortenUrl(encoded)
                .map(uri -> ResponseEntity.status(HttpStatus.FOUND)
                                          .location(uri)
                                          .build())
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 실습 시간에
    @PostMapping("shorten")
    public Mono<ResponseEntity<UrlShortenerResponse>> createUrlShortener(@RequestBody UrlShortenerRequest urlShortenerRequest) {
        return urlShortenerService.shortenUrl(urlShortenerRequest)
                                  .map(ResponseEntity::ok);
    }
}
