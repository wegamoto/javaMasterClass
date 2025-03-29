package com.wewe.earthquake.controller;

import com.wewe.earthquake.service.EarthquakeService;
import com.wewe.earthquake.model.Earthquake;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EarthquakeController {

    private final EarthquakeService earthquakeService;

    public EarthquakeController(EarthquakeService earthquakeService) {
        this.earthquakeService = earthquakeService;
    }

    @GetMapping("/")
    public String showEarthquakes(Model model) {
        List<Earthquake> earthquakes = earthquakeService.getEarthquakes();
        model.addAttribute("earthquakes", earthquakes);
        return "earthquake";
    }
}

