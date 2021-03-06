package com.fin.auth.web;

import com.fin.auth.model.User;
import com.fin.auth.service.SecurityService;
import com.fin.auth.service.UserService;
import com.fin.auth.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;




@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;


    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("userForm",new User());
        return "registration";

    }


    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult){
        userValidator.validate(userForm,bindingResult);

        if (bindingResult.hasErrors()){
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(),userForm.getPassword());
        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String login(Model model, String error,String logout){
        if (error != null){
            model.addAttribute("error","Your username password is invalid.");
        }
        if (logout != null){
            model.addAttribute("message","You have been logged out successfully");
        }

        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }
}
