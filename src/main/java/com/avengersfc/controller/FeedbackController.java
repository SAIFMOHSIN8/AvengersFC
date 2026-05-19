package com.avengersfc.controller;

import com.avengersfc.model.Feedback;
import com.avengersfc.service.FeedbackService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        if (feedback.getContent() == null || feedback.getContent().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Feedback createdFeedback = feedbackService.createFeedback(feedback);
        return ResponseEntity
                .created(URI.create("/feedback/" + createdFeedback.getId()))
                .body(createdFeedback);
    }


    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Feedback> getFeedbackByID(@PathVariable String id) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        if (feedback == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(feedback);
    }

    // Delete end point
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteFeedbackByID(@PathVariable String id) {
        boolean deleted = feedbackService.deleteFeedbackById(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> updateFeedback(@PathVariable String id, @RequestBody Feedback updatedFeedback) {
        if (updatedFeedback.getContent() == null || updatedFeedback.getContent().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Feedback feedback = feedbackService.updateFeedback(id, updatedFeedback);
        if (feedback == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(feedback);
    }
}
