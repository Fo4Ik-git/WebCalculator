package com.fo4ik.momcalculator.controllers.settings;

import com.fo4ik.momcalculator.config.Config;
import com.fo4ik.momcalculator.models.Logo;
import com.fo4ik.momcalculator.models.User;
import com.fo4ik.momcalculator.repo.LogoRepo;
import com.fo4ik.momcalculator.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class SettingsController {
    private static final Logger log = LoggerFactory.getLogger(SettingsController.class);
    private final LogoRepo logoRepo;
    private final UserRepo userRepo;

    public SettingsController(LogoRepo logoRepo, UserRepo userRepo) {
        this.userRepo = userRepo;
        this.logoRepo = logoRepo;
    }

    @GetMapping("/settings")
    public String settingsPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("title", "Settings");
        try {
            Config config = new Config(userRepo, logoRepo);
            config.getUserLogo(user, model);

        } catch (Exception e) {
            log.error("Error in settings: " + e.getMessage());
        }
        return "settings/settings";
    }

    @PostMapping("/settings/add_logo")
    public String addLogo(@AuthenticationPrincipal User user, @RequestParam("logoFile") MultipartFile logoFile, Model model) {
        try {
            Logo logos = logoRepo.findById(user.getId());
            if (logos != null && !logoFile.getOriginalFilename().equals("")) {
                logos.setPath(saveLogo(user, logoFile, model));
                logoRepo.save(logos);
            } else {
                Logo logo = new Logo(saveLogo(user, logoFile, model), user);
                logo.setId(user.getId());
                logoRepo.save(logo);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            log.error("Error to add logo: " + e.getMessage());
            return "redirect:/scheme";
        }

        return "redirect:/settings";
    }

    private String saveLogo(User user, MultipartFile logoFile, Model model) {
        try {
            Path folder = Path.of(createUserFolder(user.getId()) + "/");
            String[] fileNameArray = logoFile.getOriginalFilename().split("\\.");
            String format = "logo." + fileNameArray[fileNameArray.length - 1];
            Path path = Path.of( folder + "/" + format);
            byte[] bytes = logoFile.getBytes();
            Files.write(path, bytes);

            return String.valueOf(path);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            log.error("Error to save logo: " + e.getMessage());
        }
        return null;
    }

    public Path createUserFolder(Long userId) {
        File file = new File("files/users/" + userId + "/");
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            return Path.of(file.getPath());
        } catch (Exception e) {
            log.error("Error to create user folder: " + e.getMessage());
        }
        return null;
    }

}
