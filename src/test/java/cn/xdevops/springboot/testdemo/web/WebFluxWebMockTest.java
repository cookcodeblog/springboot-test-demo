package cn.xdevops.springboot.testdemo.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

@WebFluxTest(GreetingController.class)
public class WebFluxWebMockTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GreetingService service;

    @Test
    @DisplayName("greeting should return message from service")
    void greetingShouldReturnMessageFromService() throws Exception {
        // mock here
        when(service.greet()).thenReturn("Hello, Mock");

        this.webTestClient
                .get()
                .uri("/greeting")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("Hello, Mock");
    }
}
