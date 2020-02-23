package spring.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spring.scheduler.dto.UserDTO;
import spring.scheduler.entity.User;
import spring.scheduler.exception.ExceptionKind;
import spring.scheduler.exception.ExtendedRuntimeException;
import spring.scheduler.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(u -> userService.userToDTO(u))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/users/{email}", method = RequestMethod.GET)
    @ResponseBody
    public UserDTO getUser(@PathVariable("email") String email) {
        return userService.userToDTO(userService.findByEmail(email)
                .orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.USER_NOT_FOUND)));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    @ResponseBody
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(userDTO);

        return userService.userToDTO(updatedUser);
    }

    @RequestMapping(value = "/api/users/{email}", method = RequestMethod.DELETE)
    public boolean deleteUser(@PathVariable("email") String email) {
        return userService.deleteUser(email);
    }
}