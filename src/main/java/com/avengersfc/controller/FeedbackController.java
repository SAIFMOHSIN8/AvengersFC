package com.avengersfc.controller;

import com.avengersfc.model.Feedback;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FeedbackController {

    private List<Feedback> feedbackList = new ArrayList<>();
    private int currentId = 1;

    @PostMapping("/feedback")
    public Feedback createFeedback(@RequestBody Feedback feedback) {

        feedback.setId(currentId);

        currentId++;

        feedbackList.add(feedback);

        return feedback;
    }

    @GetMapping("/feedback")
    public List<Feedback> getAllFeedbacks() {
        return feedbackList;
    }

    @GetMapping("/feedback/{id}")
    public Feedback getFeedbackByID(@PathVariable int id) {
        for (Feedback feedback : feedbackList) {
            if (feedback.getId() == id) {
                return feedback;
            }
        }
        return null;
    }
}
