package com.fo4ik.momcalculator.controllers;

import com.fo4ik.momcalculator.config.Config;
import com.fo4ik.momcalculator.models.Elements;
import com.fo4ik.momcalculator.models.User;
import com.fo4ik.momcalculator.repo.ElementsRepo;
import com.fo4ik.momcalculator.repo.LogoRepo;
import com.fo4ik.momcalculator.repo.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {
    String[] elements;

    private final ElementsRepo elementsRepo;

    private final UserRepo userRepo;
    private final LogoRepo logoRepo;

    public MainController(ElementsRepo elementsRepo, UserRepo userRepo, LogoRepo logoRepo) {
        this.elementsRepo = elementsRepo;
        this.userRepo = userRepo;
        this.logoRepo = logoRepo;
    }

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user, Model model) {
        try {

            List<Elements> elements = elementsRepo.findAll();
            model.addAttribute("elementsGet", elements);
            model.addAttribute("title", "Main Page");
            System.out.println();
            Config config = new Config(userRepo, logoRepo);
            config.getUserLogo(user, model);
            return "index";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "index";
        }
    }


    @PostMapping("/clear")
    public String clear(Model model) {
        List<Elements> elements = elementsRepo.findAll();
        try {
            for (int i = 0; i < elements.size(); i++) {
                elements.get(i).setVariable(0f);
            }
        } catch (Exception e) {
            System.out.println("Error to clear");
        }
        return "redirect:/";
    }

    @PostMapping("/calculate")

    public String getElements(@AuthenticationPrincipal User user,@RequestParam(name = "elements", required = false) Float[] elements, Model model) {
        main(user, model);
        float count = 0;
        float cost = 0;
        try {
        List<Elements> namesDB = elementsRepo.findAll();
        if (elements.length > 0) {
            for (int i = 0; i < elements.length; i++) {
                namesDB.get(i).setVariable(elements[i]);
            }
        }
            for (int i = 0; i < elements.length; i++) {
                if (!namesDB.get(i).getName().equals("Time")) {
                    count = count + elementsRepo.findByName(namesDB.get(i).getName()).getValue() * namesDB.get(i).getVariable();
                    cost = cost + elementsRepo.findByName(namesDB.get(i).getName()).getCost() * namesDB.get(i).getVariable();
                }
            }
            /*count = count + elements[elements.length - 1];
            cost = cost + elements[elements.length - 1];*/

        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("linkBack", "/");
        }

        try {
            Elements el = elementsRepo.findByName("Time");
            Elements el2 = elementsRepo.findByName("Рассходники");
            float matherial = count + el2.getVariable();
            float time = el.getVariable() * el.getValue();
            float sum = matherial + time;
            String result = "Собівартість = " + cost + "\n" +
                    "Матеріали = " + matherial + "\n" +
                    "Час = " + time + "\n" +
                    "Вартість = " + sum;
            List<String> str = new ArrayList<>(Arrays.asList(result.split("\n")));
            model.addAttribute("count", str);
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("linkBack", "/");
        }
        return "index";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/tmp")
    public String tmp(@AuthenticationPrincipal User user,Model model) {
        Config config = new Config(userRepo, logoRepo);
        config.getUserLogo(user, model);
        model.addAttribute("hello", "tmp");
        model.addAttribute("title", "tmp");
        return "tmp";
    }
}
