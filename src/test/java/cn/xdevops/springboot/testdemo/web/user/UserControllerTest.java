package cn.xdevops.springboot.testdemo.web.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("should return all users")
    void shouldReturnAllUsers() {
        when(userService.getAllUsers()).thenReturn(null);

        this.webTestClient
                .get()
                .uri("/api/users")
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

    }

    @Test
    @DisplayName("should create new user")
    void shouldCreateNewUser() {
        User newUser = new User(1L, "William", Set.of("java", "openshift"));

        when(userService.addNewUser(newUser)).thenReturn(Optional.of(newUser));

        this.webTestClient
                .post()
                .uri("/api/users")
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Mono.just(newUser), User.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CREATED);

    }

    @Test
    @DisplayName("should get user by id")
    void shouldGetUserById() {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(Optional.of(new User(userId, "mock", Set.of("tag1", "tag2"))));
        this.webTestClient
                .get()
                .uri("/api/users/{id}", userId)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

    }

    @Test
    @DisplayName("should delete user by id")
    void shouldDeleteUserById() {
        Long userId = 1L;
        doNothing().when(userService).deleteById(userId);
        this.webTestClient
                .delete()
                .uri("/api/users/{id}", userId)
                .exchange()
                .expectStatus()
                .isOk();
    }


}
