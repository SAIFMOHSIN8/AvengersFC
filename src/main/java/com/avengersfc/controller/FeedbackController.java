package com.avengersfc.controller;

import com.avengersfc.model.Feedback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private List<Feedback> feedbackList = new ArrayList<>();
    private int currentId = 1;

    @PostMapping
    public Feedback createFeedback(@RequestBody Feedback feedback) {

        feedback.setId(currentId);

        currentId++;

        feedbackList.add(feedback);

        return feedback;
    }


    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackList;
    }

    @GetMapping(path = "/{id}")
    public Feedback getFeedbackByID(@PathVariable int id) {
        for (Feedback feedback : feedbackList) {
            if (feedback.getId() == id) {
                return feedback;
            }
        }
        return null;
    }

    // Delete end point
    @DeleteMapping(path ="{id}")
    public void deleteFeedbackByID(@PathVariable int id) {
        feedbackList.removeIf(feedback -> feedback.getId() == id);
    }
}