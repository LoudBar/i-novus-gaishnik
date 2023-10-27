package ru.loudbar.gaishnik.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.loudbar.gaishnik.services.NumberPlateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/number")
public class NumberPlateController {

    private final NumberPlateService numberPlateService;

    @GetMapping("/random")
    public String getRandomNumber() {
        return numberPlateService.getRandomNumber();
    }

    @GetMapping("/next")
    public String getNextNumber() {
        return numberPlateService.getNextNumber();
    }
}
