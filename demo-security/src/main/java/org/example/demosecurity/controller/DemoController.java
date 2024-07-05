package org.example.demosecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

//    private final OAuth2AuthorizedClientManager clientManager;

    @GetMapping("/demo")
    public String demo() {
        return "Hello World Demo";
    }

//    @GetMapping("/demo")
//    public Authentication demo(Authentication a) {
//        return a;
//    }


//    @GetMapping("/token")
    // Предоставление конечной точки GET по пути /token
//    public String token() {
//        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
//                .withClientRegistrationId("1")
//                .principal("client")
//                .build();
    // Отправляя запрос, приложение возвращает значение маркера доступа.
//        var client =
//                clientManager.authorize(request);
    // Приложение возвращает значение маркера доступа в тексте ответа
//        return client
//                .getAccessToken().getTokenValue();
//    }
}
