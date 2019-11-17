package ty.henry.cinemaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ty.henry.cinemaapp.dto.UserForm;
import ty.henry.cinemaapp.error.EntityAlreadyExistsException;
import ty.henry.cinemaapp.model.User;
import ty.henry.cinemaapp.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
    public String registerUser(@Validated({UserForm.EditableDataValidation.class,
            UserForm.UneditableDataValidation.class}) UserForm userForm, BindingResult result) {
        if(!result.hasErrors()) {
            try {
                userService.registerNewUserAccount(userForm);
            } catch (EntityAlreadyExistsException ex) {
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

    @GetMapping("/edit-user")
    public String showEditUserPage(Principal principal, UserForm userForm) {
        User user = userService.findUserByEmail(principal.getName());
        userForm.setName(user.getName());
        userForm.setSurname(user.getSurname());
        userForm.setDateOfBirth(user.getDateOfBirth());
        return "editUser";
    }

    @PostMapping("/edit-user")
    public String editUser(@Validated(UserForm.EditableDataValidation.class) UserForm userForm,
                           BindingResult result, Principal principal) {
        if(!result.hasErrors()) {
            User user = userService.findUserByEmail(principal.getName());
            userService.editUser(user, userForm);
            return "redirect:/profile";
        }
        return "editUser";
    }

    @PostMapping("/delete-user")
    public String deleteUser(Principal principal, HttpServletRequest request) throws ServletException {
        userService.deleteUser(principal.getName());
        request.logout();
        return "redirect:/login?deleted=true";
    }
}
