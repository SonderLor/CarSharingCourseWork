package ru.sonder.carsharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sonder.carsharing.DTOs.UserDTO;
import ru.sonder.carsharing.models.Rent;
import ru.sonder.carsharing.models.RentWithCarDescription;
import ru.sonder.carsharing.models.User;
import ru.sonder.carsharing.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/home"})
    public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<User> topUsers = userService.getTopUsers();
        model.addAttribute("topUsers", topUsers);
        model.addAttribute("userDetails", userDetails);
        return "home";
    }

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User currentUser = userService.getUser(userDetails.getUsername());
        List<Rent> userRents = currentUser.getRents();
        List<RentWithCarDescription> rentsWithDescriptions = userRents.stream()
                .map(rent -> new RentWithCarDescription(rent, rent.getCar().getBrand() + ", " + rent.getCar().getModel() + ", " + rent.getCar().getYear()))
                .collect(Collectors.toList());
        model.addAttribute("rentsWithDescriptions", rentsWithDescriptions);
        model.addAttribute("userDetails", userDetails);
        return "profile";
    }

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userDetails", userDetails);
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("userDetails", userDetails);
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO) {
        try {
            userService.add(userDTO);
        } catch(Exception e) {
            return "redirect:/registration?name_invalid";
        }
        return "redirect:/registration?success";
    }
}
