package com.wewe.promptinvoice.model;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Entity
@Data
@Table(name = "settings")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`key`", unique = true, nullable = false)  // ใส่ backtick ครอบชื่อ column เพื่อหลีกเลี่ยงปัญหา
    private String key;

    @Column(nullable = false)
    private String value;

    public Setting() {}

    public Setting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // getters, setters

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Component
    public class RequestInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            request.setAttribute("requestURI", request.getRequestURI());
            return true;
        }
    }
}
