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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ElementsController {

    private final ElementsRepo elementsRepo;

    private final UserRepo userRepo;
    private final LogoRepo logoRepo;

    public ElementsController(ElementsRepo elementsRepo, UserRepo userRepo, LogoRepo logoRepo) {
        this.elementsRepo = elementsRepo;
        this.userRepo = userRepo;
        this.logoRepo = logoRepo;
    }

    @GetMapping("/elements")
    public String elements(@AuthenticationPrincipal User user, Model model) {
        try {
            Iterable<Elements> posts = elementsRepo.findAll();
            model.addAttribute("elements", posts);
            model.addAttribute("title", "Elements");
            Config config = new Config(userRepo, logoRepo);
            config.getUserLogo(user, model);
            return "elements";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "elements";
        }
    }

    @GetMapping("/elements/{id}/edit")
    public String elementsEdit(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id, Model model) {
        try {
            if (!elementsRepo.existsById(id)) {
                return "redirect:/elements";
            }
            Config config = new Config(userRepo, logoRepo);
            config.getUserLogo(user, model);

            Optional<Elements> post = elementsRepo.findById(id);
            List<Elements> result = new ArrayList<>();
            post.ifPresent(result::add);
            model.addAttribute("elements", result);
            model.addAttribute("title", "Edit Element");
            return "elements-edit";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "elements-edit";
        }
    }

    @PostMapping("/elements/{id}/edit")
    public String elementsUpdate(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id, @RequestParam String name, @RequestParam Float value, @RequestParam Float cost, Model model) {
        try {
            Config config = new Config(userRepo, logoRepo);
            config.getUserLogo(user, model);
            Elements elements = elementsRepo.findById(id).orElseThrow();
            elements.setName(name);
            elements.setCost(cost);
            elements.setValue(value);
            //Save data from request to DB
            elementsRepo.save(elements);
            //redirect to blog page
            return "redirect:/elements";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/elements";
        }
    }

    @GetMapping("/elements/{id}/remove")
    public String elementsDelete(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id, Model model) {
        try {
            Config config = new Config(userRepo, logoRepo);
            config.getUserLogo(user, model);
            Elements elements = elementsRepo.findById(id).orElseThrow();
            elementsRepo.delete(elements);
            return "redirect:/elements";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/elements";
        }
    }
}
