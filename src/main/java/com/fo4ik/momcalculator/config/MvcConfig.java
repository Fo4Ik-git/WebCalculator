package com.fo4ik.momcalculator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public static String uploadDirectory = System.getProperty("user.dir");

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("uploadDirectory = " + uploadDirectory);
        registry.addResourceHandler("/images/**").addResourceLocations("file:" + uploadDirectory + "\\");
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
