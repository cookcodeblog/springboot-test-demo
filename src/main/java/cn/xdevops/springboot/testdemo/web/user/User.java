package cn.xdevops.springboot.testdemo.web.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class User {
    @NotNull(message = "Id is mandatory")
    @ApiModelProperty(value = "User Id", required = true)
    private Long id;
    @NotBlank(message = "Name is mandatory")
    @ApiModelProperty(value = "Name", required = true)
    private String name;
    @ApiModelProperty(value = "Tags", required = false)
    private Set<String> tags;
}
