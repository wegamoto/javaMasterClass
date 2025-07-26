package com.wewe.swiftaid.config;

import com.wewe.swiftaid.converter.StringToAmbulanceTeamConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private StringToAmbulanceTeamConverter ambulanceTeamConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(ambulanceTeamConverter);
    }
}

