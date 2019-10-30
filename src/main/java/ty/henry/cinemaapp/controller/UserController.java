package ty.henry.cinemaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ty.henry.cinemaapp.dto.UserForm;
import ty.henry.cinemaapp.error.UserAlreadyExistsException;
import ty.henry.cinemaapp.model.User;
import ty.henry.cinemaapp.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String showHomePage() {
        return "main";
    }

    @GetMapping("/register")
    public String showRegistrationPage(UserForm userForm) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid UserForm userForm, BindingResult result) {
        if(!result.hasErrors()) {
            try {
                userService.registerNewUserAccount(userForm);
            } catch (UserAlreadyExistsException ex) {
                result.rejectValue("email", "message.userExistsError");
            }
        }
        if(result.hasErrors()) {
            return "register";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @RequestMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }
}
