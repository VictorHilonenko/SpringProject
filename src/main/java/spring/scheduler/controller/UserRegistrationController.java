package spring.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.scheduler.dto.UserDTO;
import spring.scheduler.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
    private UserService userService;

    @Autowired
    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserDTO userRegistrationDTO() {
        return new UserDTO();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result) {
        userService.findByEmail(userDTO.getEmail())
                .ifPresent(u -> result.rejectValue("email", null, "i18n.emailIsTaken"));

        if (result.hasErrors()) {
            return "registration";
        }

        userService.addUser(userDTO);

        return "redirect:/login";
    }
}