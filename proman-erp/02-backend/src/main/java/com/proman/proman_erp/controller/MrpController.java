package com.proman.proman_erp.controller;

import com.proman.proman_erp.dto.MrpPlanDTO;
import com.proman.proman_erp.service.MrpService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mrp")
public class MrpController {
    private final MrpService mrpService;

    public MrpController(MrpService mrpService) {
        this.mrpService = mrpService;
    }

    @PostMapping("/generate")
    public List<MrpPlanDTO> generate(@RequestParam Long productId, @RequestParam int quantity) {
        return mrpService.generateMrpForProduct(productId, quantity);
    }
}
