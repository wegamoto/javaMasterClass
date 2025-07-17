package com.wewe.springlance.controller;

import com.wewe.springlance.model.Review;
import com.wewe.springlance.model.User;
import com.wewe.springlance.service.ProjectRequestService;
import com.wewe.springlance.service.ReviewService;
import com.wewe.springlance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProjectRequestService projectRequestService;

    @Autowired
    private UserService userService;

    @GetMapping("/project/{projectId}/new")
    public String showReviewForm(@PathVariable Long projectId, Model model) {
        Review review = new Review();
        review.setProject(projectRequestService.findById(projectId).orElse(null));
        model.addAttribute("review", review);
        return "review/create"; // /templates/review/create.html
    }

    @PostMapping("/save")
    public String saveReview(@ModelAttribute Review review) {
        review.setReviewDate(LocalDateTime.now());

        // จำลองการกำหนด reviewer จริง (ในระบบจริงควรดึงจาก session)
        User reviewer = userService.findById(1L).orElse(null); // ตัวอย่าง reviewer ID 1
        review.setReviewer(reviewer);

        reviewService.save(review);
        return "redirect:/projects/" + review.getProject().getId();
    }

    @GetMapping("/project/{projectId}")
    public String viewReview(@PathVariable Long projectId, Model model) {
        Review review = reviewService.findByProjectId(projectId);
        model.addAttribute("review", review);
        return "review/view"; // /templates/review/view.html
    }
}
