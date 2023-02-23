package com.fo4ik.momcalculator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public static String uploadDirectory = System.getProperty("user.dir");

    private static String OS = null;
    public static String getOsName()
    {
        if(OS == null) { OS = System.getProperty("os.name"); }
        return OS;
    }
    public static boolean isWindows()
    {
        return getOsName().startsWith("Windows");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        getOsName();
        if(isWindows()) {
            registry.addResourceHandler("/**").addResourceLocations("file:" + uploadDirectory + "\\");
        } else {
            registry.addResourceHandler("/**").addResourceLocations("file://" + uploadDirectory + "/");
        }


    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
