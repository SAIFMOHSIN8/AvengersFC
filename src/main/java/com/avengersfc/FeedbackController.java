package com.avengersfc;

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
}