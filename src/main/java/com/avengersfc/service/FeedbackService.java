package com.avengersfc.service;

import com.avengersfc.model.Feedback;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class FeedbackService {
    private final List<Feedback> feedbackList = new CopyOnWriteArrayList<>();

    public Feedback createFeedback(Feedback feedback) {
        feedback.setId(UUID.randomUUID().toString());
        feedbackList.add(feedback);
        return feedback;
    }

    public List<Feedback> getAllFeedbacks() {
        return List.copyOf(feedbackList);
    }

    public Feedback getFeedbackById(String id) {
        for (Feedback feedback : feedbackList) {
            if (Objects.equals(feedback.getId(), id)) {
                return feedback;
            }
        }
        return null;
    }

    public boolean deleteFeedbackById(String id) {
        return feedbackList.removeIf(feedback -> Objects.equals(feedback.getId(), id));
    }

    public Feedback updateFeedback(String id, Feedback updatedFeedback) {
        for (Feedback feedback : feedbackList) {
            if (Objects.equals(feedback.getId(), id)) {
                feedback.setContent(updatedFeedback.getContent());
                return feedback;
            }
        }
        return null;
    }
}
