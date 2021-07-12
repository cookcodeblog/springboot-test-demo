package cn.xdevops.springboot.testdemo.web.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @AfterEach
    void cleanUp() {
        List<User> userList =  this.webTestClient
                .get()
                .uri("/api/users")
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .expectBodyList(User.class)
                .returnResult()
                .getResponseBody();
        userList.forEach(user -> this.webTestClient
                .delete()
                .uri("/api/users/{id}", user.getId())
                .exchange()
                .expectStatus()
                .isOk());
    }

    @Test
    @DisplayName("should create new user")
    void shouldCreateNewUser() {
        User newUser = new User(1L, "William", Set.of("java", "openshift"));

        // create a new user
        FluxExchangeResult<Void> result = this.webTestClient
                .post()
                .uri("/api/users")
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Mono.just(newUser), User.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CREATED)
                .returnResult(Void.class);

        String newUserURI = result.getResponseHeaders().get(LOCATION).get(0);

        // check new created user
        this.webTestClient
                .get()
                .uri(newUserURI)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(User.class)
                .isEqualTo(newUser);

        // delete new created user
        this.webTestClient
                .delete()
                .uri(newUserURI)
                .exchange()
                .expectStatus()
                .isOk();

        // check if delete
        this.webTestClient
                .get()
                .uri(newUserURI)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should return not found for get unknown user")
    void shouldReturnNotFoundForGetUnknownUser() {
        Long unknownId = 9999L;
        this.webTestClient
                .get()
                .uri("/api/users/{id}", unknownId)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should return bad request for create existing user")
    void shouldReturnBadRequestForCreateExistingUser() {
        Long existingId = 11L;
        User user1 = new User(existingId, "John", Set.of("python", "web"));
        User user2 = new User(existingId, "Tom", Set.of("mysql", "redis"));

        // create a new user
        createNewUserWithStatus(user1, HttpStatus.CREATED);

        // create another user with existing id
        createNewUserWithStatus(user2, HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("should get all users")
    void shouldGetAllUsers() {

        this.webTestClient
                .get()
                .uri("/api/users")
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .expectBodyList(User.class)
                .hasSize(0);

        User user1 = new User(1L, "user1", Set.of("python", "web"));
        User user2 = new User(2L, "user2", Set.of("mysql", "redis"));
        User user3 = new User(3L, "user3", Set.of("java", "jenkins"));

        createNewUserWithStatus(user1, HttpStatus.CREATED);
        createNewUserWithStatus(user2, HttpStatus.CREATED);
        createNewUserWithStatus(user3, HttpStatus.CREATED);

        this.webTestClient
                .get()
                .uri("/api/users")
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .expectBodyList(User.class)
                .hasSize(3)
                .isEqualTo(List.of(user1, user2, user3));

    }

    @Test
    @DisplayName("should return bad request for invalid parameters")
    void shouldReturnBadRequestForInvalidParameters() {
        User emptyUser = new User(null, "", Set.of("python", "web"));
        createNewUserWithStatus(emptyUser, HttpStatus.BAD_REQUEST);
    }

    private void createNewUserWithStatus(User user, HttpStatus httpStatus) {
        this.webTestClient
                .post()
                .uri("/api/users")
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus()
                .isEqualTo(httpStatus);
    }

}
