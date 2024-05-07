package ru.sonder.carsharing.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sonder.carsharing.DTOs.CarDTO;
import ru.sonder.carsharing.DTOs.RentDTO;
import ru.sonder.carsharing.models.User;
import ru.sonder.carsharing.services.CarService;
import ru.sonder.carsharing.services.RentService;
import ru.sonder.carsharing.services.UserService;

@RequestMapping("/rents")
@Controller
@RequiredArgsConstructor
public class RentController {
    private final RentService rentService;
    private final CarService carService;
    private final UserService userService;

    @GetMapping
    public String setRent(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("carId") Long carId, Model model) {
        User currentUser = userService.getUser(userDetails.getUsername());
        RentDTO rentDTO;
        if (carService.getCar(carId).isAvailable()) {
            rentDTO = rentService.setRent(currentUser.getId(), carId);
        }
        else {
            rentDTO = rentService.getRentByCarIdAndUserId(carId, currentUser.getId());
        }
        CarDTO carDTO = carService.getCar(rentDTO.getCarId());
        model.addAttribute("rent", rentDTO);
        model.addAttribute("car", carDTO);
        model.addAttribute("userDetails", userDetails);
        return "rent";
    }

    @GetMapping("/finish")
    public String finishRent(@AuthenticationPrincipal UserDetails userDetails, Model model, @RequestParam("rentId") Long rentId) {
        rentService.finishRent(rentId);
        User currentUser = userService.getUser(userDetails.getUsername());
        RentDTO rentDTO = rentService.getRent(currentUser.getRents().getLast().getId());
        CarDTO carDTO = carService.getCar(rentDTO.getCarId());
        model.addAttribute("rent", rentDTO);
        model.addAttribute("car", carDTO);
        model.addAttribute("userDetails", userDetails);
        return "rent";
    }
}
