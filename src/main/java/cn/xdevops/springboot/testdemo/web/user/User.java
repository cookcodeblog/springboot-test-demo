package cn.xdevops.springboot.testdemo.web.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull(message = "Id is mandatory")
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    private Set<String> tags;
}
