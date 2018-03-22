package pl.sokn.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sokn.dto.UserDTO;
import pl.sokn.entity.User;
import pl.sokn.service.UserService;

@Api(tags = "User")
@RestController
@RequestMapping(path = "/user")
public class UserController extends AbstractGenericController<UserDTO, User, Long>{
    private UserService userService;

    @Autowired
    public UserController(@pl.sokn.annotation.qualifier.UserService UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    protected UserDTO convertToDTO(final User user) {
        return User.convertTo(user);
    }

    @Override
    protected User convertToEntity(final UserDTO user) {
        return User.convertFrom(user);
    }
}
