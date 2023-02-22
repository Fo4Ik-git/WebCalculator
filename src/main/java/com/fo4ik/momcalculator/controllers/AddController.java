package com.fo4ik.momcalculator.controllers;

import com.fo4ik.momcalculator.config.Config;
import com.fo4ik.momcalculator.models.Elements;
import com.fo4ik.momcalculator.models.User;
import com.fo4ik.momcalculator.repo.ElementsRepo;
import com.fo4ik.momcalculator.repo.LogoRepo;
import com.fo4ik.momcalculator.repo.UserRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddController {
    private final ElementsRepo elementsRepo;

    private final UserRepo userRepo;
    private final LogoRepo logoRepo;

    public AddController(ElementsRepo elementsRepo, UserRepo userRepo, LogoRepo logoRepo) {
        this.elementsRepo = elementsRepo;
        this.userRepo = userRepo;
        this.logoRepo = logoRepo;
    }

    @GetMapping("/add")
    public String createElement(@AuthenticationPrincipal User user, Model model) {
        try{
            Config config = new Config(userRepo, logoRepo);
            config.getUserLogo(user, model);
            model.addAttribute("title", "Add Element");
            return "create-elements";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "create-elements";
        }

    }

    @PostMapping("/add")
    public String createElement(@AuthenticationPrincipal User user,@RequestParam("name") String name, @RequestParam("value") String  value, @RequestParam("cost") String cost, Model model) {
        try{
            Config config = new Config(userRepo, logoRepo);
            config.getUserLogo(user, model);

            if(name.isEmpty() || value.isEmpty()  || cost.isEmpty()) {
                model.addAttribute("error", "Please fill all fields");
                return "create-elements";
            }
            Elements elements = new Elements(name, Float.valueOf(value), Float.valueOf(cost));
            elementsRepo.save(elements);
            return "redirect:/add";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/add";
        }
    }
}
