package cn.xdevops.springboot.testdemo.web.user;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/users")
@Api(tags = {"User APIs"})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiOperation(value = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get all users successfully")
    })
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @ApiOperation("Create a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created a new user successfully"),
            @ApiResponse(code = 400, message = "The user already exist")
    })
    public ResponseEntity<Void> createNewUser(@RequestBody @Valid User user, UriComponentsBuilder builder) {
        User addedUser = userService
                .addNewUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("User with id [%s] already exist", user.getId())));

        URI location = builder.path("/api/users/{id}").buildAndExpand(addedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value="User ID", required = true)
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the user by id"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public User getUserById(@PathVariable("id") Long id) {
        return userService
                .getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with id [%s] not found", id)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user by id")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value="User ID", required = true)
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete the user by id successfully")
    })
    public void deleteByUserId(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }
}
