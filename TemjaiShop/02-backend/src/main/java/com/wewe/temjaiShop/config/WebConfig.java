package com.wewe.temjaiShop.config;

import com.wewe.temjaiShop.interceptor.CartItemCountInterceptor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Getter
    private final CartItemCountInterceptor cartItemCountInterceptor;

    @Autowired
    private HandlerInterceptor cartInterceptor;

    public WebConfig(CartItemCountInterceptor cartItemCountInterceptor) {
        this.cartItemCountInterceptor = cartItemCountInterceptor;
    }


    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("th"));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Interceptor สำหรับเปลี่ยน locale (เช่น i18n)
        registry.addInterceptor(localeChangeInterceptor())
                .addPathPatterns("/**");

        // Interceptor สำหรับนับจำนวนสินค้าใน cart
        registry.addInterceptor(cartInterceptor)
                .addPathPatterns("/**")
                .order(1); // ตั้งลำดับหลัง localeChangeInterceptor (default คือ 0)
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:///C:/temjaishop/uploads/");
        } else {
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:/opt/temjaishop/uploads/");
        }
    }

}
