package com.wewe.weweShop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ชี้ /uploads/** ไปที่ c:/weweshop/uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/C:/weweshop/uploads/"); // Windows path
                // หรือ "file:/weweshop/uploads/" บน Linux/macOS
    }
}

