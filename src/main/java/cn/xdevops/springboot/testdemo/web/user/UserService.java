package cn.xdevops.springboot.testdemo.web.user;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private List<User> userList;

    @PostConstruct
    public void init() {
        userList = new ArrayList<>();
    }

    public Optional<User> addNewUser(User user) {
        if (getUserById(user.getId()).isPresent()) {
            return Optional.empty();
        }
        this.userList.add(user);
        return Optional.of(user);
    }

    public Optional<User> getUserById(Long id) {
        return this.userList
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public void deleteById(Long id) {
        this.userList.removeIf(user -> user.getId().equals(id));
    }

    public List<User> getAllUsers() {
        return this.userList;
    }
}
